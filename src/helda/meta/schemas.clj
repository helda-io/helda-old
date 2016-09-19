(ns helda.meta.schemas
  (:require [schema.core :as s])
  (:require [schema.core :refer [defschema => optional-key maybe]])
  )

(defschema Field{
    :name s/Keyword
    (optional-key :description) s/Str
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
    (optional-key :world) World
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
    (optional-key :description) s/Str
    :input-msg {
      s/Keyword s/Str
    }
    (optional-key :examples) [Message]
    :handler (=> Response [Message World])
    (optional-key :msg-schema) s/Any; Input msg schema
    (optional-key :generator) Generator
    (optional-key :coerce) (=> Message [Message s/Any]);second arg is schema (msg-schema)
    (optional-key :validator) (=> (maybe Message) [Message s/Any]);second arg is schema (msg-schema)
  })

(defschema WorldFixture{
  :tag s/Keyword
  (optional-key :description) s/Str
  :field-values {
    s/Keyword s/Any
    }
  })

(defschema Meta{
  :name s/Keyword
  :fields {s/Keyword Field}
  :handlers {s/Keyword Handler}
  :sys-handlers {s/Keyword (=> Message [Message Meta])}
  :fixtures {s/Keyword WorldFixture}
  })
