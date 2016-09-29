(ns helda.assembly.core
  (:require [schema.core :as s])
  (:require [helda.engines :refer :all])
  )

(s/defn init-assembly :- Assembly [adapter :- AdapterEnum]
  {:meta-list [] :generators [] :adapter adapter :storage-url "atom"})  

(s/defn run-assembly [assembly :- Assembly]
  (create-engine
    (if (= :repl (assembly :adapter))
      (helda.adapters.dsl.DslMsgAdapter.)
      (helda.adapters.core.SimpleMsgAdapter.)
      )
    ;todo starts-with to add mongo support
    #(helda.storage.core.WorldStorageAtom. (atom (seed-world %)))
    (assembly :meta-list)
    )
  )
