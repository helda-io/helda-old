(ns helda.accounting-test
  (:require [clojure.test :refer :all]
            [helda.meta.handlers :refer :all]
            [helda.meta.fields :refer :all]
            [helda.assembly.engines :refer :all]
            [helda.examples.accounting :refer :all]))

(deftest create-meta-test
  (let [meta (create-meta)]
    (testing "There are some fields" (is (> (count (meta :fields)) 0)))
    (testing "Checking field exist"
      (is (= :account-assets-fixed
        (get-in meta [:fields :account-assets-fixed :name])))
      )
    )
  )

(deftest handler-test
  (let
    [msg {
      :tag :accounting-entry
      :debit :account-assets-fixed
      :credit :account-owner-equities
      :amount 1000
    }
    meta (create-meta)
    world (seed-world meta)
    changes (handle msg meta world)]
      (testing "World changes"
        (is (= 1000 (get-in changes [:world :account-assets-fixed])))
        (is (= -1000 (get-in changes [:world :account-owner-equities])))
        )
    )
  )

(deftest assembly-test
  (let
    [msg {
      :tag :accounting-entry
      :debit :account-assets-fixed
      :credit :account-owner-equities
      :amount 1000
    }
    engine (run-accounting :embedded)]
    (handle-msg engine msg)
    (let [response (handle-msg engine {:tag :get-accounts})]
      (testing "World changes"
        (is (= 1000 (response :account-assets-fixed)))
        (is (= -1000 (response :account-owner-equities)))
        )
      )
    )
  )

(deftest sys-handlers-test
  (let [
    meta (create-meta)
    world (seed-world meta)
    ]
    (testing "Sys-handlers checking"
      (is (= 6 (count (handle {:tag :fields-list} meta world))))
      (is (= 6 (count (handle {:tag :fields-table} meta world))))
      (is (= 2 (count (handle {:tag :lookup-fixture :fixture :sample1} meta world))))
      )
    )
  )
