(ns engine.utils.input.mouse
  (:import
   (org.lwjgl.glfw GLFW)))

(defn update-mouse-position
  ^{:doc "Resive :globasl objetc"}
  [{:keys [window mouse-buffer]}]
  (GLFW/glfwGetCursorPos window (:x mouse-buffer) (:y mouse-buffer)) 1)
