(ns helda.assembly.core
  (:require [schema.core :as s])
  (:require [helda.engines :refer :all])
  (:require [helda.assembly.generators :refer :all])
  )

(s/defn ^always-validate init-assembly :- Assembly [adapter :- AdapterEnum]
  {:meta-list [] :generators [] :adapter adapter :storage-url "atom"})

(s/defn ^always-validate add-generator
  [assembly :- Assembly generator :- Generator]
  (assoc assembly :generators (conj (assembly :generators) generator))
  )

(s/defn ^always-validate add-meta [assembly :- Assembly meta :- Meta]
  (assoc assembly :meta-list (conj (assembly :meta-list) meta))
  )

(s/defn ^always-validate run-assembly [assembly :- Assembly]
  (let [engine
    (create-engine
      (if (= :repl (assembly :adapter))
        (helda.adapters.dsl.DslMsgAdapter.)
        (helda.adapters.core.SimpleMsgAdapter.)
        )
      ;todo starts-with to add mongo support
      #(helda.storage.core.WorldStorageAtom. (atom (seed-world %)))
      (assembly :meta-list)
      )]
      (start-all-gens engine (assembly :generators))
    )
  )
