(ns helda.fields-test
  (:require [clojure.test :refer :all]
            [helda.examples.accounting :refer :all]
            [helda.meta.core :refer :all]
            [helda.meta.fields :refer [seed-world]]
            ))

(deftest add-field-test
  (let [meta (add-field (init-meta :example)
    {:name :name-first-name :default-value "N/A" :description "First name"})]
    (testing "Field was added"
      (is (= 1 (count (filter #(= % :name-first-name)(meta :fields)))))
      (is (= "N/A" (get-in meta [:seed :name-first-name])))
      (is (= "First name" (get-in meta [:fields-table :name-first-name :description])))
      )
    )
  )

(deftest add-fields-test
  (let [meta (add-fields (init-meta :example)
    [{:name :address-city :default-value "N/A" :description "City"}
    {:name :address-street :default-value "N/A" :description "Address"}])]
    (testing "Fields were added"
      (is (= 1 (count (filter #(= % :address-city)(meta :fields)))))
      (is (= "N/A" (get-in meta [:seed :address-city])))
      (is (= "City" (get-in meta [:fields-table :address-city :description])))
      (is (= 1 (count (filter #(= % :address-street)(meta :fields)))))
      (is (= "N/A" (get-in meta [:seed :address-street])))
      (is (= "Address" (get-in meta [:fields-table :address-street :description])))
      )
    )
  )

(deftest seed-test
  (let [world (seed-world (create-meta)) meta (create-meta)]
    (testing "The same counts of fields in worlds as in meta"
      (is (=
        (count (meta :fields))
        (count world)
        ))
      )
    (testing "Checking field exist with default value"
      (is (= 0 (world :account-assets-fixed)))
      )
    )
  )
