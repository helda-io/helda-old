(ns helda.meta.worlds
  (:use [helda.meta.core])
  (:use [helda.helpers.response])
  (:require [schema.core :as s])
  )

; Worlds meta-info app
(defn create-meta []
  (-> (init-meta :worlds-meta)
    (add-field {
      :name :worlds-list
      :default-value []
      :description "List of worlds"
      })
    (add-field {
      :name :worlds-description
      :default-value {}
      :description "Worlds description per key"
      })

    (add-handler {
      :tag :worlds
      :input-msg {
        }
      :handler (fn [msg world]
        (reply-msg {} {
          :worlds-list (world :worlds-list)
          :worlds-description (world :worlds-description)
          })
        )
      })
    (add-handler {
      :tag :add-world
      :input-msg {
        :world-name "World name"
        :worlds-description "World description"
        }
      :msg-schema {
        :tag s/Keyword
        :world-name s/Keyword
        :worlds-description s/Str
      }
      :handler (fn [msg world]
        (-> (init-response :world-meta-reply)
          (save :worlds-list (conj (world :worlds-list) (msg :world-name)))
          (save
            :worlds-description
            (assoc
              (world :worlds-description)
              (msg :world-name)
              (msg :worlds-description)
              )
            )
          )
        )
      })
    )
  )
