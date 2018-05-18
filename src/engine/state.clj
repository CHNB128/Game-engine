(ns engine.state
  (:import
   (org.lwjgl BufferUtils)))

(defonce global-template
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
   :resources nil})

(defn init
  []
  global-template)
