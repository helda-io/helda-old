(ns helda.meta.worlds
  (:use [helda.meta.core])
  (:use [helda.helpers.response])
  )

; Worlds meta-info app
(defn create-meta []
  (-> (init-meta "worlds-meta")
    (add-field {
      :field :worlds-list
      :default-value []
      :description "List of worlds"
      })
    (add-field {
      :field :worlds-description
      :default-value {}
      :description "Worlds description per key"
      })

    (add-handler {
      :input-msg {
        :tag "worlds"
        }
      :handler (fn [msg world]
        (reply-msg {} {
          :worlds-list (world :worlds-list)
          :worlds-description (world :worlds-description)
          })
        )
      })
    (add-handler {
      :input-msg {
        :tag "add-world"
        :world-name "World name"
        :worlds-description "World description"
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
