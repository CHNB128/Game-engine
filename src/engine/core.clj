(ns engine.core
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.glfw GLFW))
  (:require [schema.core :as s]
            [engine.window :as window
            [engine.graphic.render :as render]
            [engine.sound.core :as sound]
            [engine.input.mouse :as mouse]
            [engine.resource.manager :as resource-manager]])))

(defonce global
  (atom
   {:errorCallback nil
    :keyCallback nil
    :window nil
    :window-height 0
    :window-width 0
    :window-title "none"
    :last-time 0
    :mouse-buffer
    {:x (BufferUtils/createDoubleBuffer 1)
     :y (BufferUtils/createDoubleBuffer 1)}
    :resources nil}))

(defn init
  [context]
  (s/validate
    {(s/required-key :window-type) s/Keyword}
    context)
  (let 
    [{window-type :window-type
      title :title
      height :height
      width :width
      processor :processor
      resources :resources} context]
    (swap! global assoc :main-loop processor)
    (cond
      (= window-type :fullscreen)
      (window/init-fullscreen)
      {:title title
        :global global}
      (and (nil? width) (nil? height))
      (window/init)
      {:title title
        :width width
        :height height
        :global global})
    (resource-manager/init)
    {:global global :resource resources}
    (render/init)))

(defn start
  []
  (while (not (GLFW/glfwWindowShouldClose (:window @global)))
    (mouse/update-mouse-position global)
    (eval (:processor @global))
    (render/reload)
    (swap! global assoc :last-time (System/currentTimeMillis))))

(defn stop
  []
  (window/close @global)
  (GLFW/glfwTerminate))
