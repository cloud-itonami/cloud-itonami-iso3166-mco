(ns marketentry.facts
  "Per-jurisdiction market-entry regulatory catalog -- the G2-style
  spec-basis table the Market-Entry Compliance Governor checks every
  `:jurisdiction/assess` proposal against ('did the advisor cite an
  OFFICIAL public source for this jurisdiction's requirements, or did
  it invent one?').

  Monaco's real market-entry surface (WebFetch-verified 2026-07-22;
  where a page could not be reached, or is JS-rendered and blocked
  automated fetch, that is stated explicitly rather than silently
  omitted):

  - **Monaco is NOT an EU member state** (this repo's first research
    task was to verify rather than assume this, per the sibling
    `cloud-itonami-iso3166-fra`/`-est` catalogs' EU-establishment-style
    flagship checks -- those do not transfer to Monaco). Monaco has a
    customs union with France (the Franco-Monégasque customs
    convention), but this iteration did NOT find any Monaco-specific
    EU-directive procurement portal (no PLACE/e-Vergabe/Find-a-Tender
    equivalent) during this research -- an honest negative finding,
    not a claim that no such portal exists at all.
  - **General commercial-activity registration -- the flagship
    mechanism this catalog is grounded on** -- fetched directly from
    Monaco's own official one-stop business portal,
    `monentreprise.gouv.mc` (linked from `gouv.mc`'s own 'aider les
    entrepreneurs et les commerces' page): the **Répertoire du
    Commerce et de l'Industrie (RCI)** is Monaco's trade/company
    register. Its own page states the RCI 'recense toutes les
    personnes physiques ou morales, réputées commerçantes par la loi,
    et exerçant une activité commerciale sur le territoire de la
    Principauté', established by 'la loi n° 721 du 27 décembre 1961
    abrogeant et remplaçant la loi n° 598 du 2 juin 1955'. It is
    administered by the 'Service du RCI', which this iteration
    confirmed reports to the **Direction du Développement Économique**
    under the Département des Finances et de l'Économie -- NOT
    'Direction de l'Expansion Économique' as this repo's own task
    brief speculated; this iteration deliberately verified the
    current name directly against `monentreprise.gouv.mc` rather than
    assuming the brief's guess was correct.
  - **Flagship: RCI registration is conditioned on ONE OF TWO
    different administrative clearances, not a single boolean or
    threshold** -- this iteration fetched the RCI company-registration
    procedure page directly and confirmed its own text: 'L'inscription
    au Répertoire du Commerce et de l'Industrie est subordonnée à la
    délivrance du récépissé de la déclaration d'exercer, ou de
    l'autorisation administrative d'exercer' (RCI registration is
    conditioned on receipt of EITHER a declaration-of-exercise receipt
    OR administrative authorization to exercise), and 'doit être
    obligatoirement effectuée dans un délai d'un mois à dater de la
    délivrance du récépissé ou de l'autorisation' (registration must
    happen within one month of receiving whichever clearance applies).
    WHICH of the two clearances is required depends on the activity:
    this iteration separately fetched Monaco's own 'liste des
    activités réglementées et/ou soumises à autorisation' page, which
    lists activities requiring prior authorization (e.g. conseil
    juridique, enseignement, hôtelier, métaux précieux, prêt sur gage
    mobilier) distinct from activities with regulated ACCESS
    conditions (avocat, banques, médecin, architecte, taxis, etc, each
    citing its own sectoral law -- e.g. Loi n. 1.252 du 12/07/2002 for
    real-estate operations, Ordonnance Souveraine n. 4.178 du
    12/12/1968 for insurance -- not modeled individually here, the
    same honest scope-narrowing sibling catalogs use for delegated/
    sector-specific thresholds). This EITHER/OR-of-two-clearance-types
    gate, keyed off the activity's own authorization-required status,
    is the flagship this catalog's `rci-clearance-spec-basis` grounds
    (see `marketentry.governor`'s `rci-clearance-missing` check) --
    grep-verified absent as a governor check function name fleet-wide
    at build time, and a check SHAPE genuinely different from every
    prior sibling's (turnover formula / flat threshold / boolean
    registry membership / set-membership sector exclusion / signing-
    method validity / multi-criterion workforce-composition
    eligibility): a BRANCHING gate that selects WHICH evidence type is
    required from the engagement's own declared activity category,
    rather than checking one fixed boolean.
  - **Tax registration**: the Direction des Services Fiscaux
    (Département des Finances et de l'Économie) 'est chargée de
    l'assiette, du recouvrement et du contrôle des impôts, droits et
    taxes en vigueur à Monaco' (fetched directly from `gouv.mc`'s own
    administrative directory page for this service). Monaco DOES levy
    a corporate income tax -- 'Impôt sur les Bénéfices' (ISB) --
    contrary to the popular assumption that Monaco has no business
    taxation at all; this catalog verified rather than assumed this.
    The page cites Ordonnance Souveraine n° 3.152 du 19 mars 1964
    (establishing ISB) and Ordonnance Souveraine n° 3.037 du 19 août
    1963 (implementing the Franco-Monegasque fiscal convention of 18
    May 1963), and states a business must 'Déclarer une entreprise à
    la Direction des Services Fiscaux'. This iteration did NOT
    independently fetch Ordonnance n° 3.152's own primary statutory
    text (legimonaco.mc is a JS-rendered single-page application whose
    document content did not render for this session's automated
    fetch tools -- both `curl` and WebFetch either received only the
    SPA shell or a TLS-verification error) -- the citation rests on
    `gouv.mc`'s own administrative-directory page quoting the
    ordonnance by number and date, the same tier of confidence FRA's
    own `statute.facts` docstring uses for its Code du travail
    citation when the primary text exceeded a fetch limit.
  - `rci-clearance-spec-basis` below grounds the flagship governor
    check; `corporate-number-spec-basis` grounds the ISB/DSF check
    (mirroring FRA's SIRET / BTN's TPN / EST's VAT-record checks).
    This catalog carries NO `:rep-owner-authority` -- Monaco is not an
    EU member and this iteration found no Monaco-specific authorized-
    representative regime analogous to FRA's EU-establishment
    requirement; `rep-spec-basis` is honestly nil for MCO rather than
    forcing FRA's shape onto a different legal regime.

  Coverage is reported HONESTLY (see `coverage`): a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries.")

(def catalog
  "iso3 -> requirement map. `:required-evidence` mirrors the generic
  intake/portal-registration/filing evidence set; `:legal-basis` /
  `:owner-authority` / `:provenance` are the G2 citation the governor
  requires before any `:jurisdiction/assess` proposal can commit.
  `:rci-clearance-owner-authority` / `:rci-clearance-legal-basis` /
  `:rci-clearance-provenance` ground this vertical's flagship governor
  check (`rci-clearance-missing` in `marketentry.governor` /
  `rci-clearance-satisfied?` in `marketentry.registry`)."
  {"MCO" {:name "Monaco"
          :owner-authority "Direction du Développement Économique -- Service du Répertoire du Commerce et de l'Industrie (RCI), Département des Finances et de l'Économie"
          :legal-basis "Loi n° 721 du 27 décembre 1961 abrogeant et remplaçant la loi n° 598 du 2 juin 1955 (établissant le Répertoire du Commerce et de l'Industrie)"
          :national-spec "Répertoire du Commerce et de l'Industrie (RCI), via monentreprise.gouv.mc (Monaco's official one-stop business portal, 'Monaco Business Office'); no Monaco-specific EU-directive public-procurement e-tender portal was found during this research"
          :provenance "https://monentreprise.gouv.mc/thematiques/creation-et-gestion-d-activite/creation-d-activite/immatriculation-et-declaration-d-existence/le-repertoire-du-commerce-et-de-l-industrie"
          :required-evidence ["Récépissé de la déclaration d'exercer OR autorisation administrative d'exercer record (whichever the declared activity category requires)"
                              "RCI (Répertoire du Commerce et de l'Industrie) registration record"
                              "Direction des Services Fiscaux company tax declaration record (Impôt sur les Bénéfices)"
                              "Registered office (siège social) address record"]
          :rci-clearance-owner-authority "Service du Répertoire du Commerce et de l'Industrie (RCI), Direction du Développement Économique, Département des Finances et de l'Économie"
          :rci-clearance-legal-basis "monentreprise.gouv.mc's own RCI registration-procedure text (fetched directly): 'L'inscription au Répertoire du Commerce et de l'Industrie est subordonnée à la délivrance du récépissé de la déclaration d'exercer, ou de l'autorisation administrative d'exercer' -- registration itself traces to Loi n° 721 du 27 décembre 1961"
          :rci-clearance-provenance "https://monentreprise.gouv.mc/thematiques/creation-et-gestion-d-activite/creation-d-activite/immatriculation-et-declaration-d-existence/inscrire-sa-societe-au-repertoire-du-commerce-et-de-l-industrie"
          :corporate-number-owner-authority "Direction des Services Fiscaux, Département des Finances et de l'Économie"
          :corporate-number-legal-basis "Ordonnance Souveraine n° 3.152 du 19 mars 1964 (Impôt sur les Bénéfices); Ordonnance Souveraine n° 3.037 du 19 août 1963 (convention fiscale franco-monégasque du 18 mai 1963) -- cited via gouv.mc's own administrative-directory page for the Direction des Services Fiscaux; this iteration did not independently fetch the ordonnances' own primary text on legimonaco.mc (JS-rendered SPA, did not render for this session's fetch tools)"
          :corporate-number-provenance "https://www.gouv.mc/annuaire-des-services-administratifs/departement-des-finances-et-de-l-economie/direction-des-services-fiscaux"}
   "USA" {:name "United States" :owner-authority "GSA/SAM.gov" :legal-basis "FAR"
          :national-spec "SAM.gov" :provenance "https://sam.gov/"
          :required-evidence ["EIN record" "SAM.gov registration record" "State business registration record" "SAM UEI verification record"]}
   "DEU" {:name "Germany" :owner-authority "e-Vergabe" :legal-basis "GWB/VgV"
          :national-spec "e-Vergabe" :provenance "https://www.evergabe-online.de/"
          :required-evidence ["Handelsregister extract" "e-Vergabe registration record" "USt-IdNr record" "Authorized-representative record"]}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO spec-basis,
  and the governor must hold any proposal that tries to assess or file
  on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions actually
  have a spec-basis entry. Never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mco R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog for market-entry navigation, "
                 "not a survey of all ~194 jurisdictions -- extend "
                 "`marketentry.facts/catalog`, never fabricate a "
                 "jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings) satisfy
  every evidence item listed for `iso3`? Missing spec-basis -> never
  satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3]
  (:required-evidence (spec-basis iso3) []))

(defn rep-spec-basis
  "The jurisdiction's authorized-representative requirement map, or nil.
  For MCO this is deliberately nil -- Monaco is not an EU member and
  this iteration found no Monaco-specific authorized-representative
  regime analogous to FRA's EU-establishment requirement; see the
  `catalog` docstring's honest-scope-narrowing note."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rep-owner-authority sb)
      (select-keys sb [:rep-owner-authority :rep-legal-basis :rep-provenance]))))

(defn corporate-number-spec-basis
  "The jurisdiction's corporate-number / tax-id regime, or nil."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority
                       :corporate-number-legal-basis
                       :corporate-number-provenance]))))

(defn rci-clearance-spec-basis
  "The jurisdiction's RCI-clearance (declaration-receipt OR
  administrative-authorization) regime, or nil. For MCO this is real
  and current -- the flagship check this vertical adds is grounded
  here (RCI registration procedure, monentreprise.gouv.mc)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rci-clearance-owner-authority sb)
      (select-keys sb [:rci-clearance-owner-authority
                       :rci-clearance-legal-basis
                       :rci-clearance-provenance]))))
