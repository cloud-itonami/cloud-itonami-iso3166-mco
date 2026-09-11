(ns culture.facts
  "Country-level regional-culture catalog for Monaco (MCO) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.

  Monaco is a thinly-documented micro-state; verification during Wave 1
  research dropped a few plausible candidates that could not be confirmed:
  Socca-the-food redirects to an unrelated article about a sport at its
  own Wikipedia title (worked around by citing the dedicated
  Monégasque-cuisine article instead, which does describe it as a
  Monaco dish); the annual 17 September Bread Festival is real (per
  secondary sources) but its Wikipedia section content could not be
  fetched/quoted directly, so it was dropped rather than cited
  second-hand.")

(def catalog
  "iso3 -> vector of culture entries."
  {"MCO"
   [{:culture/id "mco.dish.barbajuan"
     :culture/name "Barbajuan"
     :culture/country "MCO"
     :culture/kind :dish
     :culture/summary "Fried appetizer of vegetable and cheese fillings mainly found in the eastern French Riviera, western Liguria and Monaco, holding cultural significance in Monaco's national celebrations."
     :culture/url "https://en.wikipedia.org/wiki/Barbajuan"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mco.dish.stocafi"
     :culture/name "Stocafi"
     :culture/country "MCO"
     :culture/kind :dish
     :culture/summary "Monégasque entree consisting of a fish (stockfish) casserole, served with white wine, cognac and tomatoes."
     :culture/url "https://en.wikipedia.org/wiki/Mon%C3%A9gasque_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mco.dish.socca"
     :culture/name "Socca"
     :culture/country "MCO"
     :culture/kind :dish
     :culture/summary "Pancake made using chickpea flour and olive oil, listed among the dishes of Monégasque cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Mon%C3%A9gasque_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mco.product.postage-stamps"
     :culture/name "Postage stamps of Monaco"
     :culture/country "MCO"
     :culture/kind :product
     :culture/summary "Monaco's postage stamps, tied to French postal rates, remain popular among collectors and are considered a source of revenue for the principality."
     :culture/url "https://en.wikipedia.org/wiki/Postage_stamps_and_postal_history_of_Monaco"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mco.festival.sainte-devote"
     :culture/name "Feast of Saint Devota"
     :culture/name-local "Sainte Dévote"
     :culture/country "MCO"
     :culture/kind :festival
     :culture/summary "Festival honoring Saint Devota, patron saint of Monaco; on the evening before her feast day a symbolic fishing boat is brought in procession into Port-Hercule and set alight, on 27 January."
     :culture/url "https://en.wikipedia.org/wiki/Sainte_D%C3%A9vote"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mco.heritage.princes-palace"
     :culture/name "Prince's Palace of Monaco"
     :culture/country "MCO"
     :culture/kind :heritage
     :culture/summary "Built in 1191 as a Genoese fortress, evolved over seven centuries as the official residence of Monaco's rulers, serving as both a working headquarters and cultural heritage site open to public touring in summer."
     :culture/url "https://en.wikipedia.org/wiki/Prince%27s_Palace_of_Monaco"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mco.heritage.oceanographic-museum"
     :culture/name "Oceanographic Museum of Monaco"
     :culture/country "MCO"
     :culture/kind :heritage
     :culture/summary "Museum of marine sciences in Monaco City, part of the Institut océanographique, established in 1910."
     :culture/url "https://en.wikipedia.org/wiki/Oceanographic_Museum"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

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
      :note (str "cloud-itonami-iso3166-mco culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "MCO"))
                 " MCO entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
