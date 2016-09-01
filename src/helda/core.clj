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

(deftype DefaultEngine [adapter storage meta]
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

(defn create-dsl-in-memory [meta]
  (DefaultEngine.
    (helda.adapters.dsl.DslMsgAdapter.)
    (helda.storage.core.WorldStorageAtom. (atom (seed-world meta)))
    meta
    )
  )

(def sample-msg {
  :tag "msg.accounting-entry"
  :debit "account.assets.fixed"
  :credit "account.owner-equities"
  :amount 1000
  })

(defn -main
  "CLI"
  [& args]
  (let [engine (create-dsl-in-memory (accounting/create-meta))]
    (println (handle-msg engine "msg.accounting-entry"))
    )
  )
