(ns engine.clj.clj.window
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
  [global {:keys [height width title external-monitor]}]
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

  (let [window (atom nil)]
    (cond
      (not (some nil? [width height]))
      (reset! window (GLFW/glfwCreateWindow width height title 0 0))
      (when external-monitor)
      (let [vidmode
            (GLFW/glfwGetVideoMode external-monitor)
            width
            (.width vidmode)
            height
            (.height vidmode)]
        (reset! window (GLFW/glfwCreateWindow width height title 0 external-monitor)))
      :else
      (let [monitor
            (GLFW/glfwGetPrimaryMonitor)
            vidmode
            (GLFW/glfwGetVideoMode monitor)
            width
            (.width  vidmode)
            height
            (.height vidmode)]
        (reset! window (GLFW/glfwCreateWindow width height title monitor 0))))
    (when-not @window
      (throw (RuntimeException. "Failed to create the GLFW window")))
    (swap! global assoc-in [:window :pointer] @window))

  (swap! global assoc-in
         [:window :key-callback]
         (proxy [GLFWKeyCallback] []
           (invoke [window key scancode action mods]
             (when (and (= key GLFW/GLFW_KEY_ESCAPE
                           (= action GLFW/GLFW_RELEASE)))
               (GLFW/glfwSetWindowShouldClose (:window @global) true)))))
  (GLFW/glfwSetKeyCallback
   (-> @global
       (:window)
       (:pointer))
   (-> @global
       (:window)
       (:keyCallback)))
  (GLFW/glfwMakeContextCurrent
    (-> @global
      (:window)
      (:pointer)))
    ; Enable v-sync
  (GLFW/glfwSwapInterval 1)
  (GLFW/glfwShowWindow
    (-> @global
      (:window)
      (:pointer)))
  (identity global))

(defn show
  ^{:doc "Make window visible"}
  [global])
  ; Make the OpenGL context current


(defn close
  ^{:doc
    "Destroy specifited window
     :global engine.utils.vars.global-template"}
  [global]
  (GLFW/glfwDestroyWindow
    (-> @global
      (:window)
      (:pointer)))
  (GLFW/glfwTerminate))

