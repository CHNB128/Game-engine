(ns engine.resource.manager)

(defn init
  [global resources]
  (swap! global assoc :resources resources))
