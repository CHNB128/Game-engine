(ns game-engine-name.utils.window
  (:import 
    (org.lwjgl BufferUtils)
    (org.lwjgl.opengl GL GL11)
    (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defn init-window 
  ([title width height globals]
   (swap! globals assoc
     :width width
     :height height
     :title title
     :last-time (System/currentTimeMillis)
     :errorCallback (GLFWErrorCallback/createPrint System/err))
      
   (GLFW/glfwSetErrorCallback (:errorCallback @globals))
   (when-not (GLFW/glfwInit)
     (throw (IllegalStateException. "Unable to initialize GLFW")))

   (GLFW/glfwDefaultWindowHints)
   (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
   (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
    
   (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
   (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)

   (swap! globals assoc
     :window (GLFW/glfwCreateWindow width height title 0 0))
      
   (when (= (:window @globals) nil)
     (throw (RuntimeException. "Failed to create the GLFW window")))
      
   (swap! globals assoc
     :keyCallback
     (proxy [GLFWKeyCallback] []
       (invoke [window key scancode action mods]
         (when (and (= key GLFW/GLFW_KEY_ESCAPE)
                    (= action GLFW/GLFW_RELEASE))
           (GLFW/glfwSetWindowShouldClose (:window @globals) true)))))
   (GLFW/glfwSetKeyCallback (:window @globals) (:keyCallback @globals))

   (let [vidmode (GLFW/glfwGetVideoMode (GLFW/glfwGetPrimaryMonitor))]
     (GLFW/glfwSetWindowPos
       (:window @globals)
       (/ (- (.width vidmode) width) 2)
       (/ (- (.height vidmode) height) 2))
     (GLFW/glfwMakeContextCurrent (:window @globals))
     ; Enable v-sync
     (GLFW/glfwSwapInterval 1)
     ; Make the window visible
     (GLFW/glfwShowWindow (:window @globals))))
  ([title globals]
   (swap! globals assoc
     :errorCallback (GLFWErrorCallback/createPrint System/err))

   (GLFW/glfwSetErrorCallback (:errorCallback @globals))
   (when-not (GLFW/glfwInit)
     (throw (IllegalStateException. "Unable to initialize GLFW")))

   (let [monitor (GLFW/glfwGetPrimaryMonitor)
         vidmode (GLFW/glfwGetVideoMode monitor)
         width   (.width  vidmode)
         height  (.height vidmode)])

   (swap! globals assoc
     :width     width
     :height    height
     :title     title
     :tri-x     (/ width 2)
     :tri-y     (/ height 2)
     :last-time (System/currentTimeMillis))

   (GLFW/glfwDefaultWindowHints)
   (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
   (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)

   (swap! globals assoc
     :window (GLFW/glfwCreateWindow width height title monitor 0))

   (when (= (:window @globals) nil)
     (throw (RuntimeException. "Failed to create the GLFW window")))

   (swap! globals assoc
     :keyCallback
     (proxy [GLFWKeyCallback] []
       (invoke [window key scancode action mods]
         (when (and (= key GLFW/GLFW_KEY_ESCAPE)
                   (= action GLFW/GLFW_RELEASE))
           (GLFW/glfwSetWindowShouldClose (:window @globals) true)))))
   (GLFW/glfwSetKeyCallback (:window @globals) (:keyCallback @globals))

   (GLFW/glfwMakeContextCurrent (:window @globals))
   (GLFW/glfwSwapInterval 1)
   (GLFW/glfwShowWindow (:window @globals))))

(defn destroy-window 
  ^{:doc 
    "Destroy specifited window
     :globals game-engine-name.utils.vars.globals-template"}
  [{keyCallback :keyCallback errorCallback :errorCallback window :window}]
  (.free keyCallback)
  (.free errorCallback)
  (GLFW/glfwDestroyWindow window))
