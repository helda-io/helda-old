(load-file "scripts/repl.clj")
(require '[helda.examples.accounting :as accounting])
(-> :repl accounting/run-accounting run-repl)
