(ns helda.meta.fields
  (:require [schema.core :as s])
  )

(s/defschema Field{
    :field s/Str
    (s/optional-key :description) s/Str
    :default-value s/Any
  })

(s/defn ^:always-validate add-field [meta field :- Field]
  (-> meta
    (assoc :fields (conj (meta :fields) (field :field)))
    (assoc-in [:fields-table (field :field)] field)
    (assoc-in [:seed (field :field)] (field :default-value))
    )
  )

(s/defn ^:always-validate add-fields [meta fields :- [Field]]
  (loop [fields-list fields res meta]
    (if-not (empty? fields-list)
      (recur
        (pop fields-list)
        (add-field res (peek fields-list))
        )
      res
      )
    )
  )

(defn seed [meta] (meta :seed))
(defn seed-world [meta]
  (loop [world {} fields (meta :fields) ]
    (if-not (empty? fields)
      (let [field (peek fields)]
        (recur
          (assoc world field (get-in meta [:seed field]))
          (pop fields)
          )
        )
      world
      )
    )
  )

(s/defn fields [meta] :- [s/Str] (meta :fields))
(s/defn fields-table [meta] :- [Field] (meta :fields-table))
(s/defn lookup-field [meta field] :- Field (get-in meta [:fields-table field]))

(defn init-fields-meta [meta]
  (-> meta
    (assoc-in [:handlers :fields-list] (fn [msg world] (fields meta)))
    (assoc-in [:handlers :fields-table] (fn [msg world] (fields-table meta)))
    )
  )
