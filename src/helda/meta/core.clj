(ns helda.meta.core
  (:require [schema.core :as s])
  (:require [helda.meta.schemas :refer :all])
  (:require [helda.meta.fields :as f])
  (:require [helda.meta.handlers :as h])
  )

(defn init-meta [name]
  (-> {:name name :handlers {}}
    (assoc-in
      [:sys-handlers "fields-list"]
      (fn [msg meta] (f/fields meta))
      )
    (assoc-in
      [:sys-handlers "fields-table"]
      (fn [msg meta] (f/fields-table meta))
      )
    )
  )

;todo Move all builder functions there
