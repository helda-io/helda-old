(ns helda.helpers.request
  (:require [schema.core :as s])
  (:require [schema.coerce :as coerce])
  (:require [helda.meta.schemas :refer :all])
  )

(s/defn coerce :- Message [msg :- Message schema]
  ((coerce/coercer schema coerce/string-coercion-matcher) msg)
  )

; you don't need validation if you did coercion
(s/defn validate-schema [msg :- Message schema]
  (s/validate schema msg)
  )
