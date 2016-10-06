(ns helda.assembly.tracer)

(defprotocol Tracer
  "Tracer can be used for debug or production login"

  (trace [this world-name msg result] "Method to trace messages")
  )

(deftype NoTracing []
  Tracer

  (trace [this world-name msg result] nil)
  )

(deftype ShortTracer []
  Tracer

  (trace [this world-name msg result]
    (println
      (str
        "World " world-name
        "Request " msg
        "Result " (result :response)
        )
      )
    )
  )

(deftype FullResultTracer []
  Tracer

  (trace [this world-name msg result]
    (println
      (str
        "World " world-name
        "Request " msg
        "Result " result
        )
      )
    )
  )

;stats tracer
