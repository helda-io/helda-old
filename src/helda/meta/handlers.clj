(ns helda.meta.handlers
  (:require [schema.core :as s])
  (:require [helda.helpers.request :refer [coerce]])
  (:require [helda.meta.schemas :refer :all])
  )

(s/defn ^:always-validate add-handler :- Meta [meta :- Meta handler :- Handler]
  (assoc-in meta [:handlers (handler :tag)] handler)
  )

(defn coerce-msg [handler msg]
  (if (handler :coerce)
    ((handler :coerce) msg (handler :msg-schema))
    (coerce msg (handler :msg-schema))
    )
  )

(defn validate-msg [handler msg]
  (if (handler :validator)
    ((handler :validator) msg)
    msg
    )
  )

(defn handle [msg meta world]
  (if-not (msg :tag) (throw (Exception. "Msg tag should be set")))
  (when (or (not (msg :world)) (= (msg :world) (meta :name)))
    (if-let [handler (get-in meta [:handlers (msg :tag)])]
      ;todo organize as pipeline
      ((handler :handler) (validate-msg handler (coerce-msg handler msg)) world)
      (if-let [sys-handler (get-in meta [:sys-handlers (msg :tag)])]
        (sys-handler msg meta)
        )
      )
    )
  )
