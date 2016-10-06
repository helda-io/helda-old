(ns helda.assembly.engines
  (:require [helda.assembly.adapters.core :refer :all])
  (:require [helda.assembly.tracer :refer :all])
  (:require [helda.meta.handlers :refer [handle]])
  (:require [helda.meta.worlds :as worlds])
  (:require [helda.storage.core :refer :all])
  )

(defprotocol Engine
  "Application entry point"

  (handle-msg [this msg] [this publisher msg] "Handle incoming msg")
  )

(deftype SingleEngine [storage meta tracer]
  Engine

  (handle-msg [this msg] (handle-msg this this msg))
  (handle-msg [this publisher msg]
    (if-let [results (handle msg meta (load-world storage))]
      (do
        (save-changes storage (results :world))
        (if-let [queue (get-in results [:world :queue])]
          (->> queue
            (pmap #(handle-msg publisher %))
            doall
            )
          )
        (trace tracer (meta :name) msg results)
        (if-let [response (results :response)]
          response
          (if-let [requests (results :requests)]
            (->> requests
              (pmap #(handle-msg publisher %))
              (remove nil?)
              (map #(handle-msg this %)) ;processing response inside current engine
              (remove nil?)
              first
              )
            )
          )
        )
      )
    )
  )

(deftype EndpointEngine [msg-sink async]
  Engine

  (handle-msg [this msg] (handle-msg this this msg))
  (handle-msg [this publisher msg]
    (future (->> msg msg-sink (handle-msg publisher)))
    )
  )

;todo can be rewritten to have o(1) routing using maps
(deftype Router [engines endpoints]
  Engine

  (handle-msg [this msg] (handle-msg this this msg))
  (handle-msg [this publisher msg]
    (if (msg :endpoint)
      (->> endpoints
        (map #(handle-msg % publisher msg))
        (remove nil?)
        first
        )
      (->> engines
        (map #(handle-msg % publisher msg))
        (remove nil?)
        first
        )
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

(defn create-engine [adapter storage-builder meta-list endpoints]
  (AssemblyEngine.
    adapter
    (Router.
      (map
        #(SingleEngine.
          (storage-builder %)
          %
          (helda.assembly.tracer.FullResultTracer.) ;todo add type selection
          )
        (conj meta-list (worlds/create-meta meta-list))
        )
      (map
        #(EndpointEngine. (% :msg-sink) (% :async))
        endpoints
        )
      )
    )
  )
