(ns engine.window
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.opengl GL GL11)
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defn init
  [{title :title
    width :width
    height :height
    global :global}]
  (swap! global assoc
         :width width
         :height height
         :title title
         :last-time (System/currentTimeMillis)
         :errorCallback (GLFWErrorCallback/createPrint System/err))

  (GLFW/glfwSetErrorCallback (:errorCallback @global))
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)

  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)

  (swap! global assoc
         :window (GLFW/glfwCreateWindow width height title 0 0))

  (when (= (:window @global) nil)
    (throw (RuntimeException. "Failed to create the GLFW window")))

  (swap! global assoc
         :keyCallback
         (proxy [GLFWKeyCallback] []
           (invoke [window key scancode action mods]
             (when (and (= key GLFW/GLFW_KEY_ESCAPE)
                        (= action GLFW/GLFW_RELEASE))
               (GLFW/glfwSetWindowShouldClose (:window @global) true)))))
  (GLFW/glfwSetKeyCallback (:window @global) (:keyCallback @global))

  (let [vidmode (GLFW/glfwGetVideoMode (GLFW/glfwGetPrimaryMonitor))]
    (GLFW/glfwSetWindowPos
     (:window @global)
     (/ (- (.width vidmode) width) 2)
     (/ (- (.height vidmode) height) 2))
    (GLFW/glfwMakeContextCurrent (:window @global))
    ; Enable v-sync
    (GLFW/glfwSwapInterval 1)
    ; Make the window visible
    (GLFW/glfwShowWindow (:window @global))))

(defn init-fullscreen
  [{title :title
    global :global}]
  (swap! global assoc
         :errorCallback (GLFWErrorCallback/createPrint System/err))

  (GLFW/glfwSetErrorCallback (:errorCallback @global))
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  (let [monitor (GLFW/glfwGetPrimaryMonitor)
        vidmode (GLFW/glfwGetVideoMode monitor)
        width   (.width  vidmode)
        height  (.height vidmode)])

  (swap! global assoc
         :width     width
         :height    height
         :title     title
         :tri-x     (/ width 2)
         :tri-y     (/ height 2)
         :last-time (System/currentTimeMillis))

  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)

  (swap! global assoc
         :window (GLFW/glfwCreateWindow width height title monitor 0))

  (when (= (:window @global) nil)
    (throw (RuntimeException. "Failed to create the GLFW window")))

  (swap! global assoc
         :keyCallback
         (proxy [GLFWKeyCallback] []
           (invoke [window key scancode action mods]
             (when (and (= key GLFW/GLFW_KEY_ESCAPE)
                        (= action GLFW/GLFW_RELEASE))
               (GLFW/glfwSetWindowShouldClose (:window @global) true)))))
  (GLFW/glfwSetKeyCallback (:window @global) (:keyCallback @global))

  (GLFW/glfwMakeContextCurrent (:window @global))
  (GLFW/glfwSwapInterval 1)
  (GLFW/glfwShowWindow (:window @global)))

(defn close
  ^{:doc
    "Destroy specifited window
     :global engine.utils.vars.global-template"}
  [{keyCallback :keyCallback errorCallback :errorCallback window :window}]
  (.free keyCallback)
  (.free errorCallback)
  (GLFW/glfwDestroyWindow window))
