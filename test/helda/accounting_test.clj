(ns helda.accounting-test
  (:require [clojure.test :refer :all]
            [helda.meta.handlers :refer :all]
            [helda.meta.fields :refer :all]
            [helda.examples.accounting :refer :all]))

(deftest create-meta-test
  (let [meta (create-meta)]
    (testing "There are some fields" (is (> (count (meta :fields)) 0)))
    (testing "Checking field exist"
      (is (= 1 (count (filter #(= % "account.assets.fixed")(meta :fields))))))
    )
  )

(deftest handler-test
  (let
    [msg {
      :tag "msg.accounting-entry"
      :debit "account.assets.fixed"
      :credit "account.owner-equities"
      :amount 1000
    }
    world (seed-world (create-meta))]
      (let [changes (handle msg world)]
        (testing "World changes"
          (is (= 1000 (get-in changes [:world "account.assets.fixed"])))
          (is (= -1000 (get-in changes [:world "account.owner-equities"])))
          )
        )
    )
  )
