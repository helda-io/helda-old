(ns helda.router-test
  (:require [clojure.test :refer :all]
            [helda.core :refer :all]
            [helda.engines :refer :all]
            [helda.examples.accounting :as accounting]))

(deftest router-test
  (let [
    engine (create-dsl-router-in-memory (accounting/create-meta))
    msg "msg.accounting-entry debit \"account.assets.fixed\"
    credit \"account.owner-equities\" amount 1000"
    ]
    (testing "Checking router results"
      (is (= 1 (count (handle-msg engine msg))))
      )
    )
  )

(deftest worlds-app-test
  (let [
    engine (create-dsl-router-in-memory (accounting/create-meta))
    result (do
      (handle-msg engine "add-world world-name \"accounts\" worlds-description \"Simple accounting app example\"")
      (first (handle-msg engine "worlds"))
      )
    ]
    (testing "Checking router results"
      (is (= "accounts" (first (result :worlds-list))))
      )
    )
  )
