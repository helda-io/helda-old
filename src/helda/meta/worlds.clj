(ns helda.meta.worlds
  (:use [helda.meta.core])
  (:use [helda.helpers.response])
  (:require [schema.core :as s])
  )

; Worlds meta-info app
(defn create-meta [meta-list]
  (-> (init-meta :worlds-meta)
    (add-field {
      :name :worlds-list
      :default-value (map #(% :name) meta-list)
      :description "List of worlds"
      })
    (add-field {
      :name :worlds-meta
      :default-value
        (zipmap (map #(% :name) meta-list) meta-list)
      :description "Worlds description per key"
      })

    (add-handler {
      :tag :worlds
      :input-msg {
        }
      :handler (fn [msg world]
        (reply-msg {} {
          :worlds (zipmap
            (world :worlds-list)
            (-> (map #(get-in world [:worlds-meta % :handlers]) (world :worlds-list))
              
              )
            )
          })
        )
      })
    (add-alias :worlds :help)
    )
  )
