(ns helda.generators
  (:require [helda.engines :refer :all])
  )

(defn start-gen [engine generator period count]
  (thread
    (loop [iter (if (> count 0) count -1)]
      (if-let [msg (generator)]
        (do
          (handle-msg engine msg)
          (if (> period 0)
            (Thread/sleep period)
            )
          (if (= -1 iter)
            (recur -1)
            (recur (- count 1))
            )
          )
        )
      )
    )
  )
