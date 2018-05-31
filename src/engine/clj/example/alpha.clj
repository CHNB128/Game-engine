(ns engine.clj.alpha
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

;; ======================================================================
;; spinning triangle in OpenGL 1.1
(defonce global (atom {:errorCallback nil
                        :keyCallback   nil
                        :window        nil
                        :width         0
                        :height        0
                        :title         "none"
                        :angle         0.0
                        :delta-time     0}))

(defn init-window
  [width height title]

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

(defn init-gl
  []
  (GL/createCapabilities)
  (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
  (GL11/glClearColor 0.0 0.0 0.0 0.0)
  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glOrtho 0.0 (:width @global)
                0.0 (:height @global)
                -1.0 1.0)
  (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn draw
  []
  (let [{:keys [width height angle]} @global
        w2 (/ width 2.0)
        h2 (/ height 2.0)]
    (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT  GL11/GL_DEPTH_BUFFER_BIT))
    (GL11/glLoadIdentity)
    (GL11/glTranslatef w2 h2 0)
    (GL11/glRotatef angle 0 0 1)
    (GL11/glScalef 2 2 1)
    (GL11/glBegin GL11/GL_TRIANGLES)
    (do
      (GL11/glColor3f 1.0 0.0 0.0)
      (GL11/glVertex2i 100 0)
      (GL11/glColor3f 0.0 1.0 0.0)
      (GL11/glVertex2i -50 86.6)
      (GL11/glColor3f 0.0 0.0 1.0)
      (GL11/glVertex2i -50 -86.6))
    (GL11/glEnd)))

(defn update-global
  []
  (let [{:keys [width height angle last-time]} @global
        cur-time (System/currentTimeMillis)
        delta-time (- cur-time last-time)
        next-angle (+ (* delta-time 0.05) angle)
        next-angle (if (>= next-angle 360.0)
                     (- next-angle 360.0)
                     next-angle)]
    (swap! global assoc
           :angle next-angle
           :delta-time cur-time)))

(defn main-loop
  []
  (while (not (GLFW/glfwWindowShouldClose (:window @global)))
    (update-global)
    (draw)
    (GLFW/glfwSwapBuffers (:window @global))
    (GLFW/glfwPollEvents)))

(defn main
  []
  (println "Run example Alpha")
  (try
    (init-window 800 600 "alpha")
    (init-gl)
    (main-loop)
    (.free (:errorCallback @global))
    (.free (:keyCallback @global))
    (GLFW/glfwDestroyWindow (:window @global))
    (finally
      (GLFW/glfwTerminate))))
