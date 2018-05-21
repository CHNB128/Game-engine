(ns engine.core
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback))
  (:require [schema.core :as s]
            [engine.window :as window]
            [engine.graphic.render :as render]
            [engine.sound.core :as sound]
            [engine.input.mouse :as mouse]
            [engine.resource.manager :as resource-manager]))

(defonce global
  (atom
   {:errorCallback nil
    :keyCallback nil
    :window nil
    :window-height 0
    :window-width 0
    :window-title "none"
    :delta-time 0
    :main-loop (fn [g])
    :mouse-buffer
    {:x (BufferUtils/createDoubleBuffer 1)
     :y (BufferUtils/createDoubleBuffer 1)}
    :resources nil}))

(defn init
  [context]
  ; (s/validate
  ;  {(s/required-key :window-type) s/Keyword
  ;   (s/optional-kel :remote-window) 
  ;   (s/optional-kel :width) s/Num
  ;   (s/optional-kel :height) s/Num
  ;   (s/optional-kel :title) s/Str}
  ;  context)
  (let [{remote-window :remote-window
         title :title  
         height :height
         width :width
         processor :processor
         resources :resources} context]
    (swap! global assoc
          :main-loop processor
          :title title)))

(defn main-loop [] ((:main-loop @global) global))

(defn loop!
  []
  (window/init-windowed global 500 500 "test")
  (render/init global)
  (while (not (GLFW/glfwWindowShouldClose (:window @global)))
    (mouse/update-mouse-position global)
    (main-loop)
    (render/rerender global)
    (render/update-delta-time global)
    ; update event pool
    (GLFW/glfwPollEvents)))

(defn stop
  []
  (.free (:keyCallback @global))
  (.free (:errorCallback @global))
  (window/close @global))
