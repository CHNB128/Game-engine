(ns refraction.event
  (:import
    (org.lwjgl.glfw GLFW GLFWKeyCallback)))

(defn- set-key-listener
  [global]
  (swap! global assoc
    :key-callback
    (proxy [GLFWKeyCallback] []
      (invoke [window key scancode action mods]
        (when
          (and
            (= key GLFW/GLFW_KEY_ESCAPE)
            (= action GLFW/GLFW_RELEASE))
          (GLFW/glfwSetWindowShouldClose (:window @global) true)))))
  (GLFW/glfwSetKeyCallback (:window @global) (:key-callback @global)))

(defn init
  [global]
  (set-key-listener global))

(defn close
  [global]
  (.free (:key-callback @global)))

