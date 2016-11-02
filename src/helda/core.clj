(ns helda.core
  (:gen-class)
  (:require [helda.examples.accounting :as accounting])
  (:require [helda.assembly.repl :refer :all])
  )

(defn -main
  "CLI"
  [& args]
  (-> :repl accounting/run-accounting run-repl)
  )
