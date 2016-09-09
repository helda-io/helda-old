(ns helda.core
  (:gen-class)
  (:require [helda.examples.accounting :as accounting])
  (:require [helda.meta.handlers :refer [handle]])
  (:require [helda.meta.fields :refer [seed-world]])
  (:require [helda.adapters.core :refer :all])
  (:require [helda.adapters.dsl :refer :all])
  (:require [helda.storage.core :refer :all])
  )

(defprotocol Engine
  "Application entry point"

  (handle-msg [this msg] "Handle incoming msg")
  )

(deftype SingleEngine [adapter storage meta]
  Engine

  (handle-msg [this msg]
    (let
      [results (handle
        (convert-input-msg adapter msg)
        meta
        (load-world storage)
      )]
      (save-changes storage (results :world))
      (convert-results adapter (results :msg))
      )
    )
  )

(deftype Router [adapter engines]
  Engine

  (handle-msg [this msg]
    ;(map convert-results adapter
      (remove nil?
        (map #(handle-msg % (convert-input-msg adapter msg)) engines)
        )
      ;)
    )
  )

(defn create-dsl-in-memory [meta]
  (SingleEngine.
    (helda.adapters.dsl.DslMsgAdapter.)
    (helda.storage.core.WorldStorageAtom. (atom (seed-world meta)))
    meta
    )
  )

(defn create-dsl-router-in-memory [& meta-list]
  (Router.
    (helda.adapters.dsl.DslMsgAdapter.)
    (map
      #(SingleEngine.
        (helda.adapters.core.SimpleMsgAdapter.)
        (helda.storage.core.WorldStorageAtom. (atom (seed-world %)))
        %
        )
      meta-list
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
