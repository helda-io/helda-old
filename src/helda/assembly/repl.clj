(ns helda.assembly.repl
  (:require [helda.assembly.engines :refer [handle-msg]])
  )

(def cmd-prompt "helda > ")

(defn run-repl [engine]
  (print cmd-prompt)
  (flush)
  (doseq [ln (line-seq (java.io.BufferedReader. *in*))]
    (try
      (println (handle-msg engine ln))
      (catch Exception e
        (println (str "Got error: " (.getMessage e) " \n" e " \n"
          (apply str (interpose "\n" (.getStackTrace e))))
          )
        )
      )
    (print cmd-prompt)
    (flush)
    )
  )
