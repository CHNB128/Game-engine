(ns engine.input.mouse
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.glfw GLFW)))

(defonce mouse-template
  {:x (BufferUtils/createDoubleBuffer 1)
   :y (BufferUtils/createDoubleBuffer 1)})

(defn init
  [global]
  (swap! global assoc :mouse mouse-template)
  (identity global))

(defn update-mouse-position
  ^{:doc "Resive :globasl objetc"}
  [global]
  (let [{:keys [window mouse-buffer]} @global]
    (GLFW/glfwGetCursorPos window (:x mouse-buffer) (:y mouse-buffer))))
