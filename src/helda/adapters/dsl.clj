(ns helda.adapters.dsl
  (:require [helda.adapters.core :refer :all])
  )

(deftype DslMsgAdapter []
  MsgAdapter

  (convert-input-msg [this msg]
    msg
    )

  (convert-results [this msg]
    msg
    )
  )
