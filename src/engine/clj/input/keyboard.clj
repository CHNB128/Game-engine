(ns engine.clj.input.keyboard
  (:import
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defonce key-map
  {:up GLFW/GLFW_KEY_UP
   :down GLFW/GLFW_KEY_DOWN
   :right GLFW/GLFW_KEY_RIGHT
   :left GLFW/GLFW_KEY_LEFT
   :a GLFW/GLFW_KEY_A
   :s GLFW/GLFW_KEY_S
   :w GLFW/GLFW_KEY_W
   :d GLFW/GLFW_KEY_D})

(defn key-pressed?
  ^{:doc
    "Check in key press for specificated window
     :window window value from global
     :key keyword from input.keyboard/key-map"}
  [window key]
  (= (GLFW/glfwGetKey window (key key-map) GLFW/GLFW_PRESS)))
