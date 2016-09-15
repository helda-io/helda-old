(ns helda.meta.schemas
  (:require [schema.core :as s])
  )

(s/defschema Field{
    :name s/Keyword
    (s/optional-key :description) s/Str
    :default-value s/Any
  })

(s/defschema Message{
    (s/required-key :tag) s/Keyword
    s/Keyword s/Any
  })

(s/defschema World{
    s/Keyword s/Any
  })

(s/defschema Response{
    :msg Message
    (s/optional-key :world) World
  })

(s/defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source (s/=> Message []) ;function that provides msgs
  })

(s/defschema Handler{
    :input-msg {
      (s/required-key :tag) s/Keyword
      s/Keyword s/Str ;description
    }
    :handler (s/=> Response [Message World])
    (s/optional-key :generator) Generator
    (s/optional-key :validator) (s/=> (s/maybe Message) [Message])
  })

  (s/defschema Meta{
    :fields {s/Keyword Field}
    :handlers {s/Keyword Handler}
    })
