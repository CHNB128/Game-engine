(ns game-engine-name.utils.user-input.mouse
  (:import 
    (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defn update-mouse-position
  [{:keys [window mouse-x-buf mouse-y-buf]}]
  (GLFW/glfwGetCursorPos window mouse-x-buf mouse-y-buf))
