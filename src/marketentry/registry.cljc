(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `rci-clearance-satisfied?` / `rci-clearance-missing?` are the SAME
  discipline applied to a genuinely Monaco-specific mechanism:
  monentreprise.gouv.mc's own RCI (Répertoire du Commerce et de
  l'Industrie) registration-procedure text, fetched directly this
  iteration, states registration 'est subordonnée à la délivrance du
  récépissé de la déclaration d'exercer, ou de l'autorisation
  administrative d'exercer' -- registration is conditioned on EITHER a
  declaration-of-exercise receipt OR administrative authorization,
  never both, and WHICH of the two is required depends on whether the
  declared activity is on Monaco's own list of activities 'soumises à
  l'obtention d'une autorisation'.

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: FRA's eu-establishment-missing and BTN's
  tpn-registration-unverified are single-boolean gates, EST's
  vat-record-unverified is likewise a single boolean, CAF's
  reserved-market-ineligible-claim? is a multi-criterion OR-of-
  thresholds ELIGIBILITY test, BTN's fdi-sector-restricted is a
  set-membership SECTOR-EXCLUSION gate. Monaco's RCI-clearance
  mechanism is none of these: it is a BRANCHING gate that first
  determines WHICH evidence type the engagement's own declared
  activity category requires, then checks THAT ONE ground-truth field
  -- the first in this family to gate on 'which requirement applies'
  rather than on a fixed field.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(defn rci-clearance-satisfied?
  "The ground-truth RCI (Répertoire du Commerce et de l'Industrie)
  clearance state for `engagement`, independently recomputed:
  monentreprise.gouv.mc's own RCI registration-procedure text
  (fetched directly) conditions registration on EITHER a
  declaration-of-exercise receipt (`:has-declaration-receipt?`) OR
  administrative authorization (`:has-administrative-authorization?`),
  and WHICH ONE is required is determined by the engagement's own
  declared `:requires-administrative-authorization?` flag (true for
  activities on Monaco's own 'soumises à obtention d'une autorisation'
  list -- e.g. conseil juridique, enseignement, hôtelier). A missing
  ground-truth field on the required branch simply fails -- this
  function never falls back to the OTHER clearance type as a
  substitute for the one the activity actually requires."
  [{:keys [requires-administrative-authorization?
           has-administrative-authorization?
           has-declaration-receipt?]}]
  (boolean
   (if requires-administrative-authorization?
     has-administrative-authorization?
     has-declaration-receipt?)))

(defn rci-clearance-missing?
  "Does `engagement` lack the RCI clearance its own declared activity
  category requires? The flagship check this vertical adds -- see
  namespace docstring."
  [engagement]
  (not (rci-clearance-satisfied? engagement)))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
