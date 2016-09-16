(ns helda.meta.handlers
  (:require [schema.core :as s])
  (:require [helda.meta.schemas :refer :all])
  )

(s/defn ^:always-validate add-handler :- Meta [meta :- Meta handler :- Handler]
  (assoc-in meta [:handlers (handler :tag)] handler)
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
      ((handler :handler) msg world)
      (if-let [sys-handler (get-in meta [:sys-handlers (msg :tag)])]
        (sys-handler msg meta)
        nil
        )
      )
    )
  )
