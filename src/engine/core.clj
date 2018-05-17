(ns engine.core
  (:import  
    (org.lwjgl.glfw GLFW))
  (:require [engine.window :as window]
            [engine.state :as state]))

(defonce global (atom nil))

(defn init
  [{window-type :window-type 
    title :title
    height :height 
    width :width}]
  (reset! global (state/init))
  (cond 
    (= window-type :fullscreen)
    (window/init-fullscreen ...)))

(defn exit
  []
  (window/close @global)
  (GLFW/glfwTerminate))
