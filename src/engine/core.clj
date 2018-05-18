(ns engine.core
  (:import
   (org.lwjgl.glfw GLFW))
  (:require [engine.window :as window]
            [engine.render :as render]
            [engine.audio.core :as audio]
            [engine.resouce.manager :as resouce-manager]
            [engine.main-loop :as main-loop]
            [engine.state :as state]))

(defonce global (atom nil))

(defn init
  [{window-type :window-type
    title :title
    height :height
    width :width
    user-main-loop :main-loop
    user-render :render ;?
    resources :resources}]
  (reset! global (state/init))
  (cond
    (= window-type :fullscreen)
    (window/init-fullscreen
     {:title title
      :global global})
    (and (nil? width) (nil? height))
    (window/init
     {:title title
      :width width
      :height height
      :global global}))
  (resouce-manager/init
   {:global global :resource resources})
  (render/init
   {:user-render user-render})
  (audio/init)
  (main-loop/init
   {:global global
    :user-main-loop user-main-loop}))

(defn run!
  []
  (main-loop/loop! global))

(defn exit
  []
  (window/close @global)
  (GLFW/glfwTerminate))
