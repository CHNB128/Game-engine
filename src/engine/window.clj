(ns engine.window
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.opengl GL GL11)
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defmacro configure-GLFW
  []
  ; optional, the current window hints are already the default
  (GLFW/glfwDefaultWindowHints)
  ; the window will stay hidden after creation
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  ; the window will be resizable
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE))

(defn init
  [{specified-window :window
    height :height
    width :width
    title :title
    global :global}]
  ; setup an error callback. The default implementation
  ; will print the error message in System.err.
  (->> (GLFWErrorCallback/createPrint System/err)
       (swap! global assoc :errorCallback)
       (GLFW/glfwSetErrorCallback))
  ; initialize GLFW. Most GLFW functions will not work before doing this.
  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))
  (configure-GLFW)
  (let [primary-monitor (GLFW/glfwGetPrimaryMonitor)
        vidmode (GLFW/glfwGetVideoMode primary-monitor)]
    (as->
     (GLFW/glfwCreateWindow
      (cond
        (when-not (nil? specified-window))
        nil
        (when-not (nil? width))
        width
        :else
        (.width  vidmode))
      (cond
        (when-not (nil? specified-window))
        nil
        (when-not (nil? height))
        height
        :else
        (.height vidmode))
      title
      (cond
        (when-not (nil? specified-window))
        specified-window
        (when-not (and (nil? width) (nil? height)))
        nil
        :else
        primary-monitor)
        ; nil it's shared-window, that don't implemented            
      nil) $
      (swap! global assoc :window $)
      (when (nil? $)
        (throw (RuntimeException. "Failed to create the GLFW window")))))
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
  [{keyCallback :keyCallback
    errorCallback :errorCallback
    window :window}]
  (.free keyCallback)
  (.free errorCallback)
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate))