(ns helda.meta.schemas
  (:require [schema.core :as s])
  )

(s/defschema Field{
    :field s/Any ;todo change to symbol
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
      s/Keyword s/Str ;description
    }
    :handler (s/make-fn-schema s/Any [[s/Any s/Any]]) ;function [msg world]
    (s/optional-key :generator) Generator
    (s/optional-key :validator) (s/make-fn-schema s/Any [[s/Any]]); function [msg]
  })
