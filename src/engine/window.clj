(ns engine.window
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

; TODO: rewrite

(defonce window-template
  {:error-callback nil
   :key-callback nil
   :pointer nil
   :height 0
   :width 0
   :title "none"})

(defn configure-GLFW
  []
  ; optional, the current window hints are already the default
  (GLFW/glfwDefaultWindowHints)
  ; the window will stay hidden after creation
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  ; the window will be resizable
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE))

(defn init
  [global {:keys [height width title]}]
  (swap! global assoc :window window-template)
  ; setup an error callback. The default implementation
  ; will print the error message in System.err.
  (let [error-callback
        (GLFWErrorCallback/createPrint System/err)]
    (GLFW/glfwSetErrorCallback error-callback)
    (swap! global assoc-in [:window :error-callback]
           error-callback))

  ; initialize GLFW. Most GLFW functions will not work before doing this.
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))
  (configure-GLFW)

  (let [monitor
        (GLFW/glfwGetPrimaryMonitor)
        vidmode
        (GLFW/glfwGetVideoMode monitor)
        window-width
        (or width (.width  vidmode))
        window-height
        (or height (.height vidmode))
        window
        (GLFW/glfwCreateWindow
         window-width
         window-height
         title
         (if (some nil? [width height]) monitor nil)
         0)]
    (when-not window
      (throw (RuntimeException. "Failed to create the GLFW window")))
    (swap! global assoc-in [:window :pointer] window))

  (swap! global assoc-in
         [:window :key-callback]
         (proxy [GLFWKeyCallback] []
           (invoke [window key scancode action mods]
             (when (and (= key GLFW/GLFW_KEY_ESCAPE
                           (= action GLFW/GLFW_RELEASE)))
               (GLFW/glfwSetWindowShouldClose (:window @global) true)))))
  (GLFW/glfwSetKeyCallback (:window @global) (:keyCallback @global))

  ; Make the OpenGL context current
  (GLFW/glfwMakeContextCurrent
   (-> @global
       (:window)
       (:pointer)))
  ; Enable v-sync
  (GLFW/glfwSwapInterval 1)
  (identity global))

(defn show
  ^{:doc "Make window visible"}
  [global]
  (GLFW/glfwShowWindow
   (-> @global
       (:window)
       (:pointer))))

(defn close
  ^{:doc
    "Destroy specifited window
     :global engine.utils.vars.global-template"}
  [{window :window}]
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate))
