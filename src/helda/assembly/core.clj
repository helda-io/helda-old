(ns helda.assembly.core
  (:require [schema.core :as s])
  (:require [helda.assembly.engines :refer :all])
  (:require [helda.assembly.adapters.core :refer :all])
  (:require [helda.assembly.adapters.dsl :refer :all])
  (:require [helda.assembly.generators :refer :all])
  (:require [helda.meta.fields :refer :all])
  (:require [helda.meta.schemas :refer :all])
  (:require [helda.assembly.schemas :refer :all])
  )

(s/defn ^:always-validate init-assembly :- Assembly [adapter :- AdapterEnum]
  {:meta-list [] :generators [] :endpoints []
    :adapter adapter :storage-url "atom"})

(s/defn ^:always-validate add-generator
  [assembly :- Assembly generator :- Generator]
  (assoc assembly :generators (conj (assembly :generators) generator))
  )

(s/defn ^:always-validate add-endpoint
  [assembly :- Assembly endpoint :- Endpoint]
  (assoc assembly :endpoints (conj (assembly :endpoints) endpoint))
  )

(s/defn ^:always-validate add-meta :- Assembly [assembly :- Assembly meta :- Meta]
  (assoc assembly :meta-list (conj (assembly :meta-list) meta))
  )

(s/defn ^:always-validate run-assembly [assembly :- Assembly]
  (let [engine
    (create-engine
      (if (= :repl (assembly :adapter))
        (helda.assembly.adapters.dsl.DslMsgAdapter.)
        (helda.assembly.adapters.core.SimpleMsgAdapter.)
        )
      ;todo starts-with to add mongo support
      #(helda.storage.core.WorldStorageAtom. (atom (seed-world %)))
      (assembly :meta-list)
      (assembly :endpoints)
      )]
      (start-all-gens engine (assembly :generators))
      engine
    )
  )
