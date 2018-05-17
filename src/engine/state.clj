(ns engine.state
  (:import 
    (org.lwjgl BufferUtils)))

(defonce global-template
  {:errorCallback nil
   :keyCallback   nil
   :window        nil
   :window-height 0
   :window-width  0
   :window-title  "none"
   :last-time     0
   :mouse-x-buf   (BufferUtils/createDoubleBuffer 1)
   :mouse-y-buf   (BufferUtils/createDoubleBuffer 1)})

(defn init
  []
  global-template)
