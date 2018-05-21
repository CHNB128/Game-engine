(ns engine.input.mouse
  (:import
   (org.lwjgl.glfw GLFW)))

(defn update-mouse-position
  ^{:doc "Resive :globasl objetc"}
  [global]
  (let [{:keys [window mouse-buffer]} @global]
    (GLFW/glfwGetCursorPos window (:x mouse-buffer) (:y mouse-buffer))))
