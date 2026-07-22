(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of Monaco market-entry law, whether an engagement's own
  declared activity category actually requires RCI declaration-receipt
  clearance or administrative-authorization clearance (and whether
  THAT ONE is on file), whether a claimed engagement fee actually
  equals base + months x rate, whether a Direction des Services
  Fiscaux tax declaration has been verified for a filing that requires
  it, or when a draft stops being a draft and becomes a real-world RCI
  registration submission, so this MUST be a separate system able to
  *reject* a proposal and fall back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints; this is another running
  implementation of that governor for the iso3166 family).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual RCI registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off'; 'a false or fabricated regulatory-requirement claim
  is a HARD hold') names exactly the checks below.

  Six checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. RCI clearance missing       -- for `:filing/submit`,
                                       INDEPENDENTLY recompute WHICH of
                                       the two RCI clearance types
                                       (récépissé de la déclaration
                                       d'exercer, OR autorisation
                                       administrative d'exercer) the
                                       engagement's own declared
                                       activity category requires, and
                                       verify THAT ONE is on file.
                                       FLAGSHIP genuinely new check for
                                       the iso3166 family (grep-
                                       verified absent as a governor
                                       check function name fleet-wide
                                       at build time) -- a BRANCHING
                                       gate (which evidence type
                                       applies is itself derived from
                                       the engagement's own ground
                                       truth, not fixed), a check SHAPE
                                       genuinely different from every
                                       prior sibling's (single-boolean
                                       flag / fee recompute / set-
                                       membership sector exclusion /
                                       multi-criterion OR-of-thresholds
                                       eligibility). Grounded in
                                       monentreprise.gouv.mc's own RCI
                                       registration-procedure text (see
                                       `marketentry.facts`).
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5. Fiscal declaration
       unverified                    -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-fiscal-declaration?
                                       true`, INDEPENDENTLY check
                                       `:fiscal-declaration-verified?`.
                                       CONDITIONAL on the engagement's
                                       own ground truth. Grounded in
                                       the Direction des Services
                                       Fiscaux's own stated mission
                                       (assiette/recouvrement/contrôle
                                       des impôts, dont l'Impôt sur les
                                       Bénéfices) -- see
                                       `marketentry.facts`.
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real portal package and submitting a real portal
  registration are the two real-world actuation events this actor
  performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(RCI登録/déclaration・autorisation/税務申告/登記住所等)が充足していない状態での提案"}]))))

(defn- rci-clearance-missing-violations
  "For `:filing/submit`, INDEPENDENTLY recompute WHICH RCI clearance
  type (déclaration d'exercer receipt, OR autorisation administrative
  d'exercer) the engagement's own declared activity category requires,
  and verify THAT ONE is on file -- the flagship check this vertical
  adds."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (registry/rci-clearance-missing? e)
        [{:rule :rci-clearance-missing
          :detail (str subject " はRCI(Répertoire du Commerce et de l'Industrie)登録に必要な"
                      "déclaration d'exercerの受領証、またはautorisation administrative d'exercerの"
                      "いずれか(活動区分に応じて要求される方)が未確認 -- 提出提案は進められない")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- fiscal-declaration-unverified-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-fiscal-declaration? true`, INDEPENDENTLY check
  `:fiscal-declaration-verified?` -- CONDITIONAL on the engagement's
  own ground truth. Grounded in the Direction des Services Fiscaux's
  own stated mission (see `marketentry.facts`)."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-fiscal-declaration? e))
                 (not (true? (:fiscal-declaration-verified? e))))
        [{:rule :fiscal-declaration-unverified
          :detail (str subject " はDirection des Services Fiscauxへの企業申告(Impôt sur les Bénéfices)確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (rci-clearance-missing-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (fiscal-declaration-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
