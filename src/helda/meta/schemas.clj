(ns helda.meta.schemas
  (:require [schema.core :as s])
  (:require [schema.core :refer [defschema =>]])
  )

(defschema Field{
    :name s/Keyword
    (s/optional-key :description) s/Str
    :default-value s/Any
  })

(defschema Message{
    (s/required-key :tag) s/Keyword
    s/Keyword s/Any
  })

(defschema World{
    s/Keyword s/Any
  })

(defschema Response{
    :msg Message
    (s/optional-key :world) World
  })

(defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source (=> Message []) ;function that provides msgs
  })

(defschema Handler{
    :tag s/Keyword
    :input-msg {
      s/Keyword s/Str ;description
    }
    :handler (=> Response [Message World])
    (s/optional-key :generator) Generator
    (s/optional-key :validator) (=> (s/maybe Message) [Message])
  })

(defschema Meta{
  :name s/Keyword
  :fields {s/Keyword Field}
  :handlers {s/Keyword Handler}
  :sys-handlers {s/Keyword (=> Message [Message Meta])}
  })
