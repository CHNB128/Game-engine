(ns engine.utils.main-loop
  (:require [engine.input.mouse :as mouse]
            [engine.render :as render]))

(defonce state
  (atom
   {:update-mouse-position nil
    :processor nil
    :rerender nil}))

(defn update-delta-time
  [global]
  (swap! global assoc :last-time (System/currentTimeMillis)))

(defn init
  ^{:doc ":processor implemented game logic"}
  [:key [global processor]]
  (swap! state assoc (mouse/update-mouse-position global))
  (swap! state assoc (processor))
  (swap! state assoc (render/reload)))
  ; ? (update-delta-time global))) , maybe beter in render ?

(defn loop!
  [:key [global]]
  (while (not (GLFW/glfwWindowShouldClose (:window @global)))
    (eval (:update-mouse-position @state))
    (eval (:processor @state))
    (eval (:rerender @state))))
