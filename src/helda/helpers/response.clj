(ns helda.helpers.response
  (:require [schema.core :as s])
  (:require [helda.meta.schema :refer :all])
  )

(s/defn init-response :- Response [tag :- s/Keyword]
  {:msg {:tag tag}}
  )

(s/defn reply-msg :- Response [response :- Response msg :- Message]
  (assoc response :msg msg)
  )

(s/defn reply-field :- Response
  [
    response :- Response
    key :- s/Keyword
    value :- s/Any
    ]
  (assoc-in response [:msg key] value)
  )

(s/defn save :- Response [response :- Response key :- s/Keyword value :- s/Any]
  (assoc-in response [:world key] value)
  )
