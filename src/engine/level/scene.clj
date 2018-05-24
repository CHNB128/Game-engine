(ns engine.level.scene)

; TODO: rewrite


(defn add-scene
  [global scene-name scene-object]
  (swap! global
         assoc-in [:scenes (keyword scene-name)]
         scene-object))

(defn remove-scene
  [global scene-name]
  (swap! global
         dissoc
         (:scenes @global) (keyword scene-name)))

(defmacro new-scene
  [resources loop-fn]
  (-> scene-template
      (assoc :resources resources)
      (assoc :loop-fn loop-fn)))

(defn change-scene
  [global scene-name]
  (let [next-scene (get (:scenes @global) (keyword scene-name))]
    (when (nil? next-scene)
      (throw (Exception. "Scene not found")))
    (swap! global
           assoc :current-scene
           (keyword scene-name))))
