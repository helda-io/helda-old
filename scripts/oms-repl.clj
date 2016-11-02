(load-file "scripts/repl.clj")
(require '[helda.examples.oms :as oms])
(-> :repl oms/run-oms run-repl)
