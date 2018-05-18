(ns engine.resouce-manager)

(defn init
  [:key [global resources]]
  (swap! global assoc :resources resources))
