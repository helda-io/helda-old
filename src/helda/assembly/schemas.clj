(ns helda.assembly.schemas
  (:require [schema.core :as s])
  (:require [schema.core :refer [defschema => optional-key maybe eq enum if]])
  (:require [helda.meta.schemas :refer :all])
  )

(defschema Generator{
    :name s/Keyword
    :period s/Num
    :count s/Num
    :msg-source (=> Message [])
  })

(defschema Endpoint{
    :name s/Keyword
    :async s/Bool
    :msg-sink (=> (maybe Message) [Message])
  })

(def AdapterEnum (enum :repl :embedded))

(defschema Assembly{
    :meta-list [Meta]
    :generators [Generator]
    :endpoints [Endpoint]
    :storage-url s/Str
    :adapter AdapterEnum
  })
