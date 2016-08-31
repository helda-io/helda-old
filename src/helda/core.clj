(ns helda.core
  (:gen-class)
  (:use [helda.examples.accounting])
  (:use [helda.meta.handlers])
  (:use [helda.meta.fields])
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

  (defn create-accounting-in-memory []
    (let [meta (create-meta)])
    (DefaultEngine.
      (helda.adapters.dsl.DslMsgAdapter.)
      (helda.storage.core.WorldStorageAtom. (atom (seed-world meta)))
      meta
      )
    )

(def meta )

(defn create-cli [meta storage]
  (save-changes storage (seed-world meta))
  )

(def world (seed-world meta))

(def sample-msg
  {
    :tag "msg.accounting-entry"
    :debit "account.assets.fixed"
    :credit "account.owner-equities"
    :amount 1000
    })

(defn -main
  "Small demo is here for now"
  [& args]
  (println
    (handle sample-msg meta world)
    )
  )
