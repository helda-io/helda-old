(ns helda.router-test
  (:require [clojure.test :refer :all]
            [helda.assembly.engines :refer :all]
            [helda.examples.accounting :as accounting]))

(deftest router-test
  (let [
    engine (accounting/run-accounting :embedded)
    msg {
      :tag :accounting-entry
      :debit :account-assets-fixed
      :credit :account-owner-equities
      :amount 1000
    }]
    (testing "Checking router results"
      (is (= 1 (count (handle-msg engine msg))))
      )
    )
  )

(deftest worlds-app-test
  (let [
    engine (accounting/run-accounting :embedded)
    result (do
      (handle-msg engine {:tag :commands})
      )
    ]
    (testing "Checking router results"
      (is (= :accounts (first (first (result :commands)))))
      )
    )
  )
