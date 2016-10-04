(ns helda.assembly.generators
  (:require [schema.core :as s])
  (:require [helda.assembly.schemas :refer :all])
  (:require [helda.assembly.engines :refer :all])
  )

(s/defn run-gen [engine generator :- Generator]
  (loop [iter (if (> (generator :count) 0) (generator :count) -1)]
    (if-let [msg ((generator :msg-source))]
      (do
        (println (str
          "Message " msg " ;response "
          (handle-msg engine msg)
          ))
        (if (> (generator :period) 0)
          (Thread/sleep (generator :period))
          )
        (if (= -1 iter)
          (recur -1)
          (if-not (= 0 iter)
            (recur (- iter 1))
            )
          )
        )
      )
    )
  )

(s/defn start-all-gens [engine generators :- [Generator]]
  ;We can rewrite it to have more control on future (to cancel or suspend gens)
  (doall (pmap #(run-gen engine %) generators))
  )
