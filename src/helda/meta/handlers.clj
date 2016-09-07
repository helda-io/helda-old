(ns helda.meta.handlers
  (:require [schema.core :as s])
  )

(s/defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source s/Any ;function that provides msgs
  })

(s/defschema Handler{
    :input-msg {
      (s/required-key :tag) s/Str
      s/Keyword s/Str
    }
    :handler s/Any ;function
    (s/optional-key :generator) Generator
  })

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
   (validate msg meta)
   (if-let [handler (get-in meta [:handlers (msg :tag)])]
    (handler msg world)
    nil
    )
  )
