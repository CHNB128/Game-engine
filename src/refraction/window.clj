(ns refraction.window
  (:import
    (org.lwjgl.opengl GL GL46 GL11)
    (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defmacro set-window-params
  []
  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
  (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE))

(defn- init-gl
  []
  (GL/createCapabilities)
  (let [renderer (GL11/glGetString GL11/GL_RENDERER)
        version (GL11/glGetString GL11/GL_VERSION)]
    (println "Renderer: " renderer)
    (println "OpenGL version:" version)
    (GL11/glEnable GL11/GL_DEPTH_TEST)
    (GL11/glDepthFunc GL11/GL_LESS)))

(defn init
  [global width height title]

  (swap! global assoc
    :width     width
    :height    height
    :title     title
    :delta-time (System/currentTimeMillis))

  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  (set-window-params)

  (let [window
        (GLFW/glfwCreateWindow width height title 0 0)
        _
        (when (nil? window)
          (throw (RuntimeException. "Failed to create the GLFW window")))
        vidmode
        (GLFW/glfwGetVideoMode (GLFW/glfwGetPrimaryMonitor))]
    (GLFW/glfwSetWindowPos
      window
      (/ (- (.width vidmode) width) 2)
      (/ (- (.height vidmode) height) 2))
    (GLFW/glfwMakeContextCurrent window)
    (GLFW/glfwSwapInterval 1)
    (GLFW/glfwShowWindow window)
    (swap! global assoc :window window))

  (init-gl))

(defn close
  [global]
  (GLFW/glfwDestroyWindow (:window @global))
  (GLFW/glfwTerminate))
