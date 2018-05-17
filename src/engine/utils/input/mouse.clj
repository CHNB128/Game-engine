(ns engine.utils.input.mouse
  (:import 
    (org.lwjgl.glfw GLFW)))

(defn update-mouse-position
  ^{:doc "Resive :globasl objetc"}
  [{:keys [window mouse-x-buf mouse-y-buf]}]
  (GLFW/glfwGetCursorPos window mouse-x-buf mouse-y-buf))
