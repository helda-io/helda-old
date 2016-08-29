(ns helda.adapters.core)

(defprotocol MsgAdapter
  "Converting message"

  (convert-input-msg [this msg] "Converting incoming message")
  (convert-results [this msg] "Converting response message")
  )
