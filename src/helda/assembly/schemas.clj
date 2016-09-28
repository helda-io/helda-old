(ns helda.assembly.schemas
  (:require [schema.core :as s])
  (:require [schema.core :refer [defschema => optional-key maybe eq if]])
  )

(defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source (=> Message [])
  })

(defschema Assembly{
    :generators [Generator]
    :meta-list [Meta]
    :storage-url s/Str
  })
