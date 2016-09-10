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
      :description "Worlds description"
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
        }
      :handler (fn [msg world]
        ;todo add handler body
        )
      })
    )
  )
