(ns helda.assembly.generators
  
  (:require [clojure.core.async :refer :all])
  (:require [helda.engines :refer :all])
  )

(s/defn add-generator [assembly generator :- Generator]

  )

(defn start-gen [engine generator period count]
  (thread
    (loop [iter (if (> count 0) count -1)]
      (if-let [msg (generator)]
        (do
          (println (str
            "Message " msg " ;response "
            ;(handle-msg engine msg)
            ))
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
