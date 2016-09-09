(ns helda.meta.worlds
  (:use [helda.meta.core])
  )

; Worlds meta-info app
(defn create-meta []
  (-> (init-meta "worlds-meta")
    (add-field {
      :field "worlds-list"
      :default-value []
      :description "List of worlds"
      })
    (add-field {
      :field "worlds-description"
      :default-value {}
      :description "Worlds description"
      })
    ;todo add handlers  
    )
  )
