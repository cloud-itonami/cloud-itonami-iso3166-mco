# cloud-itonami-iso3166-mco

**`:implemented`** market-entry actor for **MCO** (Monaco).

Flagship HARD: `rci-clearance-missing` (RCI declaration-receipt OR
administrative-authorization, whichever the declared activity
category requires) · tax HARD: `fiscal-declaration-unverified`
(Direction des Services Fiscaux / Impôt sur les Bénéfices)

Monaco is NOT an EU member state (verified, not assumed); market
entry runs through Monaco's own Répertoire du Commerce et de
l'Industrie (RCI) regime rather than an EU-directive procurement
portal.

```
clojure -M:dev:test
clojure -M:dev:run
```

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Monaco:

- `src/culture/facts.cljk` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
