(ns helda.meta.core
  (:require [schema.core :as s])
  (:require [helda.meta.fields :as fields])
  (:require [helda.meta.handlers :as handlers])
  )

(defn init-meta [name]
  (-> {:name name :handlers {}}
    fields/init-fields-meta
    handlers/init-handlers-meta
    )
  )
