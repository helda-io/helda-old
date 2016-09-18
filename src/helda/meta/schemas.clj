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
    :tag s/Keyword
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
    ;Function that provides msgs (get schema as param, can be nil if not set
    ;Handler.msg-schema)
    :msg-source (=> Message [s/Any])
  })

(defschema Handler{
    :tag s/Keyword
    :input-msg {
      s/Keyword s/Str
    }
    :handler (=> Response [Message World])
    (s/optional-key :msg-schema) s/Any; Input msg schema
    (s/optional-key :generator) Generator
    (s/optional-key :coerce) (=> Message [Message s/Any]);second arg is schema (msg-schema)
    (s/optional-key :validator) (=> (s/maybe Message) [Message s/Any]);second arg is schema (msg-schema)
  })

(defschema Meta{
  :name s/Keyword
  :fields {s/Keyword Field}
  :handlers {s/Keyword Handler}
  :sys-handlers {s/Keyword (=> Message [Message Meta])}
  })
