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

(defonce global-template
  ^{:doc "Field description: 
    :scene - used for storage scene for game
    :resources - used to store repeated resources, that was added from scene"}
  {:errorCallback nil
   :keyCallback nil
   :window
   {:object nil
    :height 0
    :width 0}
   :title "none"
   :delta-time 0
   :scene nil
   :resources nil
   :main-loop nil
   :mouse-buffer
   {:x (BufferUtils/createDoubleBuffer 1)
    :y (BufferUtils/createDoubleBuffer 1)}})

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
    (-> global-template
        (assoc :main-loop processor)
        (assoc :title title)
        (atom))))

(defn loop!
  [global]
  (window/init-windowed global 500 500 "test")
  (render/init global)
  (while (not (GLFW/glfwWindowShouldClose (:window @global)))
    (mouse/update-mouse-position global)
    ((:main-loop @global) global)
    (render/rerender global)
    ; update event pool
    (GLFW/glfwPollEvents)))

(defn stop
  [global]
  (.free (:keyCallback @global))
  (.free (:errorCallback @global))
  (window/close @global))
