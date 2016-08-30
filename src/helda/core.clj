(ns helda.core
  (:gen-class)
  (:use [helda.examples.accounting])
  (:use [helda.meta.handlers])
  (:use [helda.meta.fields])
  )

(def meta (create-meta))

; (defn create-cli [meta storage]
;   (save-changes storage (seed-world meta))
;   )
; 
; (defn load-cli [meta storage]
;   (load-world storage)
;   )

(def world (seed-world meta))

(def sample-msg
  {
    :tag "msg.accounting-entry"
    :debit "account.assets.fixed"
    :credit "account.owner-equities"
    :amount 1000
    })

(defn -main
  "Small demo is here for now"
  [& args]
  (println
    (handle sample-msg meta world)
    )
  )
