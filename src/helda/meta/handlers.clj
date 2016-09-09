(ns helda.meta.handlers
  (:require [schema.core :as s])
  (:require [helda.meta.schemas :refer :all])
  )

;todo think about case when adding handler and input msg already defined

(s/defn ^:always-validate add-handler [meta handler :- Handler]
  ;todo check to have one handler only
  (let [meta-data
    (if (meta :handlers) meta (assoc meta :handlers {} :input-table {}))
    ]
    (-> meta-data
      (assoc-in [:handlers (get-in handler [:input-msg :tag])]
        (handler :handler)
        )
      (assoc-in [:input-table (get-in handler [:input-msg :tag])]
        (handler :input-msg)
        )
      )
    )
  )

(defn validate [msg meta]
  (let [msg-tag (msg :tag)]
    (if-not msg-tag (throw (Exception. "Msg tag should be set")))
    (map #(% msg) (get-in meta [:msg-constraints msg-tag]))
    )
  )

(defn handle [msg meta world]
  (when (or (not (msg :world)) (= (msg :world) (meta :name)))
    (validate msg meta)
    (if-let [handler (get-in meta [:handlers (msg :tag)])]
      (handler msg world)
      (if-let [sys-handler (get-in meta [:sys-handlers (msg :tag)])]
        (sys-handler msg meta)
        nil
        )
      )
    )
  )
