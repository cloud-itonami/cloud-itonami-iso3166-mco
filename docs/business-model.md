# Business Model: Independent Public-Sector Market-Entry & Procurement Compliance Service — Monaco

## Classification

- Repository: `cloud-itonami-iso3166-mco`
- ISO 3166: `MCO` (Monaco)
- Activity: RCI (Répertoire du Commerce et de l'Industrie) commercial
  registration and ongoing regulatory-compliance navigation for an
  already-incorporated operator
- Monaco is NOT an EU member state (verified, not assumed) — this
  catalog does not carry an EU-establishment-style requirement; no
  Monaco-specific EU-directive procurement portal was found during
  this research

## Customer

- an already-incorporated `cloud-itonami-cofog-{code}` /
  `cloud-itonami-isco-{code}` / `cloud-itonami-unspsc-{segment}` /
  `cloud-itonami-{ISIC}` operator wanting to register a commercial
  activity in Monaco
- a foreign SME or individual entrepreneur entering the Monaco market
  for the first time
- a `cloud-itonami-M6910` client that has just completed incorporation
  and now needs Monaco market access

## Offer

- registration walkthrough for the Répertoire du Commerce et de
  l'Industrie (RCI), Monaco's trade/company register, administered by
  the Service du RCI under the Direction du Développement Économique
- clearance checklist: does the declared activity require a simple
  déclaration d'exercer receipt, or prior autorisation administrative
  d'exercer (Monaco's own distinction — some activities, e.g. conseil
  juridique, enseignement, hôtelier, métaux précieux, prêt sur gage
  mobilier, require the latter)
- tax-declaration checklist: registering the business with the
  Direction des Services Fiscaux for Impôt sur les Bénéfices (ISB) —
  Monaco DOES levy a corporate income tax on businesses meeting the
  applicable criteria, contrary to the popular no-business-tax
  assumption
- ongoing regulatory-change monitoring subscription
- compliance-audit export package for the client's own records

## Revenue

- per-engagement market-entry fee (one-time registration + checklist
  completion)
- recurring regulatory-change monitoring subscription
- compliance-audit export package

## Trust Controls

- any actual RCI registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off (`:filing/submit` is never automated at any phase)
- a false or fabricated regulatory-requirement claim is a HARD hold
  that cannot be overridden by human approval alone — it must be
  corrected against a cited official source first
- this service does **not** provide legal or tax advice; characterization
  and filing on the client's behalf beyond checklist/draft assistance
  routes to Monaco-licensed counsel or a registered agent
- every requirement cites the official portal or regulation, never
  invented

## Boundary with adjacent actors (read before forking)

- **`cloud-itonami-M6910`**: helps a client BECOME a legal entity
  (incorporation, ISIC 6910) — a prior, different regulatory phase
  (company law). This blueprint assumes incorporation is already done
  and handles Monaco RCI market entry (a different regulatory domain).
- **`cloud-itonami-cofog-{code}`**: a jurisdiction-agnostic operator
  template for ONE public function. This blueprint is the orthogonal
  jurisdiction-specific axis — the two compose (fork a COFOG-function
  blueprint AND this one to operate in Monaco).
- **`cloud-itonami-iso3166-fra`**: France's own market-entry actor.
  Monaco has a customs union with France (the Franco-Monégasque
  customs convention) but is administered under its own distinct RCI
  regime — this blueprint does not assume France's PLACE/BOAMP/SIRET
  mechanisms apply in Monaco, and vice versa.
