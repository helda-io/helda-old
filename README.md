# Helda

[![Build Status](https://travis-ci.org/helda-io/helda.svg?branch=master)](https://travis-ci.org/helda-io/helda)

## What is this?

Helda is Agent/Actor (hybrid) oriented programming (AAOP) framework for Clojure.

It allows you to describe your app data, input messages and implement message handlers as pure functions.

Helda is a container for Clojure functions - like Spring framework is a container for Java objects.

## Usage example

```Clojure
(ns helda.examples.accounting
  (:use [helda.meta.handlers])
  (:use [helda.meta.fields])
  (:use [helda.helpers.response])
  )

; Accounting app example
(defn create-meta []
  (let [meta {}]
    (-> meta
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
  )
```

Find more examples in src/helda/examples folder

## Features

* Provide meta info for application state (fields, constraints)
* Seeding application state and fixtures for application state
* Describing application input as messages and pure functions as handlers

*Work in progress...*

But feel free to fork if you would like to contribute or create feature request or bug.
Any ideas on how to make framework better are welcomed.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
