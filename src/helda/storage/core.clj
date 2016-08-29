(ns helda.storage.core)

(defprotocol WorldStorageProtocol
  "World storage protocol"

  (load-world [this] "Returns world")
  (save-world [this world] "Saving world")
  )

(deftype WorldStorageAtom [atom]
  WorldStorageProtocol
  (load-world [this] @atom)
  (save-world [this world] (reset! atom world))
  )
