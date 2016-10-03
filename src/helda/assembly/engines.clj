(ns helda.assembly.engines
  (:require [helda.assembly.adapters.core :refer :all])
  (:require [helda.meta.handlers :refer [handle]])
  (:require [helda.meta.worlds :as worlds])
  (:require [helda.storage.core :refer :all])
  )

(defprotocol Engine
  "Application entry point"

  (handle-msg [this msg] [this publisher msg] "Handle incoming msg")
  )

(deftype SingleEngine [storage meta]
  Engine

  (handle-msg [this msg] (handle-msg this this msg))
  (handle-msg [this publisher msg]
    (if-let [results (handle msg meta (load-world storage))]
      (do
        (save-changes storage (results :world))
        (results :response)
        )
      )
    )
  )

(deftype Router [engines]
  Engine

  (handle-msg [this msg] (handle-msg this this msg))
  (handle-msg [this publisher msg]
    (->> engines
      (map #(handle-msg % publisher msg))
      (remove nil?)
      first
      )
    )
  )

(deftype AssemblyEngine [adapter router]
  Engine

  (handle-msg [this msg] (handle-msg this router msg))
  (handle-msg [this publisher msg]
    (->> msg
      (convert-input-msg adapter)
      (handle-msg router publisher)
      (convert-results adapter)
      )
    )
  )

(defn create-engine [adapter storage-builder meta-list]
  (AssemblyEngine.
    adapter
    (Router. (map
      #(SingleEngine. (storage-builder %) %)
      (conj meta-list (worlds/create-meta meta-list))
      ))
    )
  )