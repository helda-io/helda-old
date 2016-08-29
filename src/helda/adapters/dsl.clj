(ns helda.adapters.dsl)

(deftype DslMsgAdapter
  MsgAdapter

  (convert-input-msg [this msg]
    msg
    )

  (convert-results [this msg]
    (msg :msg)
    )
  )
