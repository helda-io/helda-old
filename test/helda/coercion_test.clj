(ns helda.coercion-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [helda.helpers.request :refer [coerce]]
            [helda.helpers.request :refer [coerce]]
            ))

(deftest coercing-test
  (let [res (coerce {:hello "world" "hi" "entropy" :num "123" :num2 "123"}
    {s/Keyword s/Str :num s/Num})]
    (testing "Type transformation expected"
      (is (= 123 (res :num)))
      (is (= "entropy" (res :hi)))
      )
    )
  )
