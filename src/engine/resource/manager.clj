(ns engine.resource.manager)

(defn init
  [{:keys [global resources]}]
  (swap! global assoc :resources resources))
