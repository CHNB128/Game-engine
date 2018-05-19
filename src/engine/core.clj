(ns engine.core
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.glfw GLFW))
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
    :main-loop nil
    :mouse-buffer
    {:x (BufferUtils/createDoubleBuffer 1)
     :y (BufferUtils/createDoubleBuffer 1)}
    :resources nil}))

(defn update-delta-time
  [global]
  (swap! global assoc :delta-time (System/currentTimeMillis)))

(defn init
  [context]
  ; (s/validate
  ;  {(s/required-key :window-type) s/Keyword
  ;   (s/optional-kel :remote-window) 
  ;   (s/optional-kel :width) s/Num
  ;   (s/optional-kel :height) s/Num
  ;   (s/optional-kel :title) s/Str}
  ;  context)
  (let [{window-type :window-type
         remote-window :remote-window
         title :title
         height :height
         width :width
         processor :processor
         resources :resources} context]
    (swap! global assoc :main-loop processor)
    (cond
      (= window-type :fullscreen)
      (window/init {:title title})
      (= window-type :windowed)
      (window/init {:title title :height height :width width})
      (= window-type :remote)
      (window/init {:title title :specified-window remote-window}))
    (resource-manager/init global resources)
    (render/init global)))

(defn loop!
  [global]
  (while-not (GLFW/glfwWindowShouldClose (:window @global))
             (mouse/update-mouse-position global)
             ((:processor @global))
             (render/rerender)
             (update-delta-time global)
    ; activate event pool
             (GLFW/glfwPollEvents)))

(defn exit
  []
  (window/close @global))
