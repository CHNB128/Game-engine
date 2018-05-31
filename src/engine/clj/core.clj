(ns engine.clj.core
  (:import (org.lwjgl.glfw GLFW))
  (:require [schema.core :as s]
            [engine.window :as window]
            [engine.graphic.opengl.render :as render]
            [engine.sound.openal.core :as sound]
            [engine.input.mouse :as mouse]
            [engine.resource.manager :as resource-manager]))

(defonce global-template
  ^{:doc "Field description: 
    :scene - used for storage scene for game
    :resources - used to store repeated resources, that was added from scene"}
  {:delta-time 0})

(defn init
  ^{:doc "Resived window-params as {:title :height :width :external-monitor}"}
  [window-params]
  (-> (atom global-template)
      (mouse/init)
      (sound/init)
      (window/init window-params)
      (render/init)))

(defn loop!
  [global]
  ; (window/init-windowed global 500 500 "test")
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
