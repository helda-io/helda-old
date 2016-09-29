(ns helda.assembly.core
  (:require [schema.core :as s])
  )

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

(defn create-engine [adapter storage-buider & meta-list]
  (helda.engines.Router.
    adapter
    (map
      #(helda.engines.SingleEngine.
        (helda.adapters.core.SimpleMsgAdapter.)
        (storage-builder %)
        %
        )
      (conj meta-list (worlds/create-meta meta-list))
      )
    )
  )
