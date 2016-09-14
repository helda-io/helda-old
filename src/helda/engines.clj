(ns helda.engines
  (:require [helda.adapters.core :refer :all])
  (:require [helda.meta.handlers :refer [handle]])
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
      (if results
        (do
          (save-changes storage (results :world))
          (convert-results adapter (results :msg))
          )
        )
      )
    )
  )
;todo add take count
(deftype Router [adapter engines]
  Engine

  (handle-msg [this msg]
    (doall
      (map #(convert-results adapter %)
        (remove nil?
          (map #(handle-msg % (convert-input-msg adapter msg)) engines)
          )
        )
      )
    )
  )
