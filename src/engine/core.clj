(ns engine.core
  (:import
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback))
  (:require [schema.core :as s]
            [engine.window :as window]
            [engine.graphic.render :as render]
            [engine.sound.core :as sound]
            [engine.input.mouse :as mouse]
            [engine.sound.core :as sound]
            [engine.resource.manager :as resource-manager]))

(defonce global-template
  ^{:doc "Field description: 
    :scene - used for storage scene for game
    :resources - used to store repeated resources, that was added from scene"}
  {:delta-time 0})

(defn init
  [{remote-window :remote-window
    title :title
    height :height
    width :width}]
  (-> (atom global-template)
      (mouse/init)
      (sound/init)
      (window/init)
      (render/init)))

(defn loop!
  [global]
  (window/init-windowed global 500 500 "test")
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
