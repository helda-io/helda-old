(ns helda.core
  (:gen-class)
  (:require [helda.examples.accounting :as accounting])
  (:require [helda.meta.worlds :as worlds])
  (:require [helda.meta.fields :refer [seed-world]])
  (:require [helda.adapters.core :refer :all])
  (:require [helda.adapters.dsl :refer :all])
  (:require [helda.assembly.engines :refer :all])
  )

(defn create-dsl-in-memory [meta]
  (helda.assembly.engines.SingleEngine.
    (helda.adapters.dsl.DslMsgAdapter.)
    (helda.storage.core.WorldStorageAtom. (atom (seed-world meta)))
    meta
    )
  )

(defn create-dsl-router-in-memory [& meta-list]
  (helda.assembly.engines.Router.
    (helda.adapters.dsl.DslMsgAdapter.)
    (map
      #(helda.assembly.engines.SingleEngine.
        (helda.adapters.core.SimpleMsgAdapter.)
        (helda.storage.core.WorldStorageAtom. (atom (seed-world %)))
        %
        )
      (conj meta-list (worlds/create-meta meta-list))
      )
    )
  )

(def cmd-prompt "helda > ")

(defn -main
  "CLI"
  [& args]
  (let [engine (create-dsl-in-memory (accounting/create-meta))]
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
