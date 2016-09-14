(ns helda.meta.schemas
  (:require [schema.core :as s])
  )

(s/defschema Field{
    :field s/Any ;todo change to keyword
    (s/optional-key :description) s/Str
    :default-value s/Any
  })

(s/defschema Message{
    :tag s/Str ;todo change to keyword
    s/Keyword s/Any
  })

(s/defschema World{
    s/Keyword s/Any
  })

(s/defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source (s/=> s/Any []) ;function that provides msgs
  })

(s/defschema Handler{
    :input-msg {
      (s/required-key :tag) s/Str
      s/Keyword s/Str ;description
    }
    :handler (s/=> s/Any [Message World])
    (s/optional-key :generator) Generator
    (s/optional-key :validator) (s/=> s/Any [Message])
  })
