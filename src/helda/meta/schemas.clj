(ns helda.meta.schemas
  (:require [schema.core :as s])
  (:require [schema.core :refer [defschema => optional-key maybe eq if]])
  )

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

(defschema Field{
    :name s/Keyword
    (optional-key :description) s/Str
    :default-value s/Any
    (optional-key :schema) s/Any
    ;second parameter is schema (if provided)
    (optional-key :validator) (=> (maybe Message) [Response s/Any])
  })

(defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source (if keyword? (eq :examples) (=> Message []))
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
    (optional-key :validator) (=> (maybe Message) [Message])
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
