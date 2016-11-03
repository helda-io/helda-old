(ns helda.meta.worlds
  (:use [helda.meta.core])
  (:use [helda.helpers.response])
  (:require [schema.core :as s])
  )

(defn trim-handlers [world-meta world-tag]
  (let [
    handlers-map (get-in world-meta [:worlds-meta world-tag :handlers])
    tags (keys handlers-map)
  ]
    (zipmap
      tags
      (map #(dissoc (handlers-map %) :tag :handler) tags)
      )
    )
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
      :handler (fn [msg world]
        (reply-msg
          (init-response :help)
          {
          :worlds (zipmap
            (world :worlds-list)
            (map
              #(trim-handlers world %)
              (world :worlds-list)
              )
            )
          })
        )
      })
    (add-alias :worlds :help)
    )
  )
