(ns engine.window
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))
; TODO: rewrite

(defn configure-GLFW
  []
  ; optional, the current window hints are already the default
  (GLFW/glfwDefaultWindowHints)
  ; the window will stay hidden after creation
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  ; the window will be resizable
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE))

(defn init-windowed
  [global width height title]

  (swap! global assoc
         :width     width
         :height    height
         :title     title
         :delta-time (System/currentTimeMillis))

  (swap! global assoc
         :errorCallback (GLFWErrorCallback/createPrint System/err))
  (GLFW/glfwSetErrorCallback (:errorCallback @global))
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
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
    (GLFW/glfwSwapInterval 1)
    (GLFW/glfwShowWindow (:window @global))))

(defn init
  [global]
  ; setup an error callback. The default implementation
  ; will print the error message in System.err.
  (swap! global assoc
         :errorCallback
         (GLFWErrorCallback/createPrint System/err))
  (GLFW/glfwSetErrorCallback (:errorCallback @global))
  ; initialize GLFW. Most GLFW functions will not work before doing this.
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))
  (configure-GLFW)
  (let [monitor (GLFW/glfwGetPrimaryMonitor)
        vidmode (GLFW/glfwGetVideoMode monitor)
        width   (.width  vidmode)
        height  (.height vidmode)]
    (swap! global assoc
           :width width
           :height height
           :title (:title global)
           :delta-time (System/currentTimeMillis)
           :window (GLFW/glfwCreateWindow 500 500 (:title global) nil 0))
    (when (= (:window @global) nil)
      (throw (RuntimeException. "Failed to create the GLFW window"))))
  ; i don't think that is realy need
  ; but dont who to implement this correctly
  (swap! global assoc
         :keyCallback
         (proxy [GLFWKeyCallback] []
           (invoke [window key scancode action mods]
             (when (and (= key GLFW/GLFW_KEY_ESCAPE
                           (= action GLFW/GLFW_RELEASE)))
               (GLFW/glfwSetWindowShouldClose (:window @global) true)))))
  (GLFW/glfwSetKeyCallback (:window @global) (:keyCallback @global))
  ; Make the OpenGL context current
  (GLFW/glfwMakeContextCurrent (:window @global))
  ; Enable v-sync
  (GLFW/glfwSwapInterval 1)
  ; Make the window visible
  (GLFW/glfwShowWindow (:window @global)))

(defn close
  ^{:doc
    "Destroy specifited window
     :global engine.utils.vars.global-template"}
  [{window :window}]
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate))