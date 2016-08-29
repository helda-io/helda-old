(ns helda.helpers.response)

(defn reply-msg [response msg]
  (assoc response :msg msg)
  )

(defn reply-field [response key value]
  (assoc-in response [:msg key] value)
  )

(defn save [response key value]
  (assoc-in response [:world key] value)
  )
