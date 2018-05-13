(ns game-engine-name.utils.user-input.keyboard
  (:import 
    (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defn key-pressed?
  ^{:doc 
    "Check in key press for specificated window
     :window GLFWwindow 
     :key GLFW_KEY"} 
  [window key]
  (= (GLFW/glfwGetKey window key) GLFW/GLFW_PRESS))
