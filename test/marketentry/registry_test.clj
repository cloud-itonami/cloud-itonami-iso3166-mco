(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "MCO" 0)
        s (registry/register-submit "eng-1" "MCO" 0)]
    (is (= "MCO-DFT-000000" (get d "draft_number")))
    (is (= "MCO-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "MCO" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest rci-clearance-declaration-path
  (testing "activity NOT requiring authorization -- declaration-receipt alone satisfies"
    (is (true? (registry/rci-clearance-satisfied?
                {:requires-administrative-authorization? false
                 :has-administrative-authorization? false
                 :has-declaration-receipt? true})))
    (is (false? (registry/rci-clearance-satisfied?
                 {:requires-administrative-authorization? false
                  :has-administrative-authorization? false
                  :has-declaration-receipt? false})))))

(deftest rci-clearance-authorization-path
  (testing "activity requiring authorization -- declaration-receipt alone does NOT satisfy"
    (is (false? (registry/rci-clearance-satisfied?
                 {:requires-administrative-authorization? true
                  :has-administrative-authorization? false
                  :has-declaration-receipt? true})))
    (is (true? (registry/rci-clearance-satisfied?
                {:requires-administrative-authorization? true
                 :has-administrative-authorization? true
                 :has-declaration-receipt? false})))))

(deftest rci-clearance-missing-mirrors-satisfied
  (is (true? (registry/rci-clearance-missing?
              {:requires-administrative-authorization? true
               :has-administrative-authorization? false})))
  (is (false? (registry/rci-clearance-missing?
               {:requires-administrative-authorization? false
                :has-declaration-receipt? true}))))

;; ---------------------------------------------------------------------------
;; Money is compared at money precision, not at double precision
;; ---------------------------------------------------------------------------

(deftest whole-unit-fees-were-already-correct-and-stay-correct
  (testing "the seeded shape: base + rate x months in whole currency units"
    (is (registry/engagement-fee-matches-claim?
         {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
           :claimed-fee 860000.0}))))

(deftest cent-denominated-fees-are-no-longer-rejected-while-correct
  (testing "`(== (double claimed) (+ (double base) (* (double rate) (double months))))`
            rejected CORRECT totals once an amount carried cents -- 40,989 of
            327,060 combinations (12.5%), against 0 of 327,060 in whole units"
    (let [bad (for [m (range 1 37)
                    bc (range 10000 90000 2100)
                    rc (range 500 6000 210)
                    :let [truth (/ (+ bc (* rc m)) 100.0)]
                    :when (not (registry/engagement-fee-matches-claim?
                                {:base-fee (/ bc 100.0) :monthly-rate (/ rc 100.0)
                                  :monitoring-months m :claimed-fee truth}))]
                [m (/ bc 100.0) (/ rc 100.0) truth])]
      (is (empty? bad) (str "false rejections: " (count bad) " e.g. " (first bad))))))

(deftest a-genuinely-wrong-fee-is-still-caught
  (testing "rounding to money precision must not blunt the check"
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 860000.01})))
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 859999.99})))))

(deftest an-unverifiable-fee-never-matches
  (testing "un-verifiable is not the same as correct, and not a crash"
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12})))
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee "500000" :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 860000.0})))
    (is (nil? (registry/compute-engagement-fee {:base-fee 500000 :monthly-rate 30000})))))
