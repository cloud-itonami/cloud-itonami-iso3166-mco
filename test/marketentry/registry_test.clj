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
