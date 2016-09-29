(ns helda.assembly.schemas
  (:require [schema.core :as s])
  (:require [schema.core :refer [defschema => optional-key maybe eq enum if]])
  (:require [helda.meta.schemas :refer :all])
  )

(defschema Generator{
    :period s/Num
    :count s/Num
    :msg-source (=> Message [])
  })

(def AdapterEnum (enum :repl :embedded))

(defschema Assembly{
    :meta-list [Meta]
    :generators [Generator]
    :storage-url s/Str
    :adapter AdapterEnum
  })
