(ns statute.facts
  "General-law compliance catalog for Monaco (MCO) -- extends this
  repo's existing `marketentry.facts` (RCI/market-entry registration
  only, narrow scope) with a second, orthogonal catalog of statutes a
  company generally must track for compliance. Mirrors
  cloud-itonami-iso3166-fra/-usa/-gbr/-deu's `statute.facts`
  (ADR-2607141700, cloud-itonami-compliance-fact-federation).

  Every entry below was WebFetch-verified directly this iteration
  (2026-07-22/23) against an official gouv.mc / monentreprise.gouv.mc
  page -- never fabricated. A law not in this table has NO spec-basis,
  full stop; extend `catalog`, do not invent an id/url.

  - **RCI (corporate registration)**: same Loi n° 721 du 27 décembre
    1961 this repo's `marketentry.facts` grounds its flagship check
    on -- carried here too because it is Monaco's foundational
    company-registration statute, the same role FRA's own
    `statute.facts` gives 'Code de commerce' alongside FRA's separate
    procurement-specific `marketentry.facts` legal-basis. This
    iteration did NOT find a single consolidated 'Code de commerce
    monégasque' distinct from this law during this session's research
    -- rather than invent one, this entry cites the RCI law this
    iteration actually confirmed.
  - **Tax (Impôt sur les Bénéfices)**: Ordonnance Souveraine n° 3.152
    du 19 mars 1964, fetched directly from `gouv.mc`'s own
    administrative-directory page for the Direction des Services
    Fiscaux (the same citation `marketentry.facts`'
    `corporate-number-legal-basis` uses for the flagship's fiscal
    check -- both trace to the same DSF page this iteration fetched
    once).
  - **Labor (durée légale du travail)**: Ordonnance-Loi n. 677 du
    02/12/1959 sur la durée du travail, fetched directly from
    `monentreprise.gouv.mc`'s own 'durée légale du travail' page,
    which states 'la durée légale du travail est fixée à 39 heures par
    semaine de travail effectif' and additionally cites Ordonnance n.
    5.505 du 09/01/1975 (application conditions) and Loi n° 822 du 23
    juin 1967 (weekly rest) -- this entry cites the primary 1959
    ordinance-law by title/number/date as fetched, without asserting
    article-level detail beyond the 39-hour figure this iteration
    actually read.
  - **Honest gap**: this iteration did NOT independently verify a
    Monaco-specific data-protection-law citation this session (Monaco
    is known to have a data-protection authority and statute, but this
    iteration did not fetch and read its current text/number) --
    deliberately omitted rather than guessed, unlike FRA's confirmed
    'Loi n° 78-17' (Loi Informatique et Libertés) entry. A
    data-protection entry is a natural next-wave addition once
    independently confirmed.
  - `legimonaco.mc` (Monaco's official Journal de Monaco / primary
    legal-text portal) is a JS-rendered single-page application whose
    document content did not render for this session's automated
    fetch tools (`curl` returned only the SPA shell; WebFetch returned
    a TLS-certificate-verification error) -- every citation below
    therefore rests on an official `gouv.mc`/`monentreprise.gouv.mc`
    government page quoting the law by number/date, not on
    `legimonaco.mc`'s own primary statutory text. This is the same
    tier of confidence FRA's own `statute.facts` docstring uses for
    its Code du travail citation when the primary text exceeded a
    fetch limit.")

(def catalog
  "iso3 -> vector of statute entries."
  {"MCO"
   [{:statute/id "mco.loi-721-rci"
     :statute/title "Loi n° 721 du 27 décembre 1961 abrogeant et remplaçant la loi n° 598 du 2 juin 1955 (Répertoire du Commerce et de l'Industrie)"
     :statute/jurisdiction "MCO"
     :statute/kind :law
     :statute/law-number "721"
     :statute/url "https://monentreprise.gouv.mc/thematiques/creation-et-gestion-d-activite/creation-d-activite/immatriculation-et-declaration-d-existence/le-repertoire-du-commerce-et-de-l-industrie"
     :statute/url-provenance :official-gouv-mc
     :statute/enacted-date "1961-12-27"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "mco.ordonnance-3152-isb"
     :statute/title "Ordonnance Souveraine n° 3.152 du 19 mars 1964 (Impôt sur les Bénéfices)"
     :statute/jurisdiction "MCO"
     :statute/kind :ordonnance
     :statute/law-number "3.152"
     :statute/url "https://www.gouv.mc/annuaire-des-services-administratifs/departement-des-finances-et-de-l-economie/direction-des-services-fiscaux"
     :statute/url-provenance :official-gouv-mc
     :statute/enacted-date "1964-03-19"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:tax}}
    {:statute/id "mco.ordonnance-loi-677-duree-travail"
     :statute/title "Ordonnance-Loi n. 677 du 02/12/1959 sur la durée du travail"
     :statute/jurisdiction "MCO"
     :statute/kind :law
     :statute/law-number "677"
     :statute/url "https://monentreprise.gouv.mc/thematiques/emploi/reglementation-du-travail/duree-legale-du-travail/duree-legale-du-travail"
     :statute/url-provenance :official-gouv-mc
     :statute/enacted-date "1959-12-02"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor :employment}}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mco statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "MCO")) " MCO statutes seeded with an "
                 "official gouv.mc/monentreprise.gouv.mc citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
