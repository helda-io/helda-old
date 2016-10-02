(ns helda.core
  (:gen-class)
  (:require [helda.examples.accounting :as accounting])
  (:require [helda.assembly.engines :refer :all])
  )

(def cmd-prompt "helda > ")

(defn -main
  "CLI"
  [& args]
  (let [engine (accounting/run-accounting :repl)]
    (print cmd-prompt)
    (flush)
    (doseq [ln (line-seq (java.io.BufferedReader. *in*))]
      (try
        (println (handle-msg engine ln))
        (catch Exception e (println (str "Got error: " (.getMessage e))))
        )
      (print cmd-prompt)
      (flush)
      )
    )
  )
