(ns helda.examples.accounting
  (:use [helda.meta.core])
  (:use [helda.meta.fields])
  (:use [helda.meta.handlers])
  (:use [helda.helpers.response])
  )

; Accounting app example
(defn create-meta []
  (-> (init-meta "accounts")
    (add-field {
      :field "account.assets.fixed"
      :default-value 0
      :description "Fixed Assets"
      })
    (add-field {
      :field "account.assets.cache"
      :default-value 0
      :description "Cache"
      })
    (add-field {
      :field "account.assets.bank"
      :default-value 0
      :description "Bank"
      })
    (add-field {
      :field "account.liabilites"
      :default-value 0
      :description "Company liabilites"
      })
    (add-field {
      :field "account.owner-equities"
      :default-value 0
      :description "Company owner equities"
      })
    (add-field {
      :field "accounting-entry.list"
      :default-value []
      :description "List of accounting entries"
      })
    (add-handler {
      :input-msg {
        :tag "msg.accounting-entry"
        :debit "Debit account field name"
        :credit "Credit account field name"
        :amount "Money amount"
        }
      :handler (fn [msg world]
        (let [changes {} debit (msg :debit) credit (msg :credit)
          amount (msg :amount)]
          (-> changes
            (save debit (+ (world debit) amount))
            (save credit (- (world credit) amount))
            (save "accounting-entry.list"
              (conj (world "accounting-entry.list") msg)
              )
            (reply-msg {:tag "Reply"})
            )
          )
        )
      })
    )
  )
