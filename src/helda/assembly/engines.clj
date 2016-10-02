(ns helda.assembly.engines
  (:require [helda.assembly.adapters.core :refer :all])
  (:require [helda.meta.handlers :refer [handle]])
  (:require [helda.meta.worlds :as worlds])
  (:require [helda.storage.core :refer :all])
  )

(defprotocol Engine
  "Application entry point"

  (handle-msg [this msg] "Handle incoming msg")
  )

(deftype SingleEngine [storage meta]
  Engine

  (handle-msg [this msg]
    (if-let [results (handle msg meta (load-world storage))]
      (do
        (save-changes storage (results :world))
        (results :msg)
        )
      )
    )
  )

(deftype Router [adapter engines]
  Engine

  (handle-msg [this msg]
    (->> engines
      (map #(handle-msg % (convert-input-msg adapter msg)))
      (remove nil?)
      (map #(convert-results adapter %))
      first
      )
    )
  )

(defn create-engine [adapter storage-builder meta-list]
  (helda.assembly.engines.Router.
    adapter
    (map
      #(helda.assembly.engines.SingleEngine.
        (storage-builder %)
        %
        )
      (conj meta-list (worlds/create-meta meta-list))
      )
    )
  )
