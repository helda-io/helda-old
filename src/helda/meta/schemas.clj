(ns helda.meta.schemas
  (:require [schema.core :as s])
  )

(s/defschema Field{
    :field s/Str
    (s/optional-key :description) s/Str
    :default-value s/Any
  })

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
