(ns engine.graphic.render
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.opengl GL GL11)
   (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defn init-gl
  []
  ; (GL/createCapabilities)
  (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
  (GL11/glClearColor 0.0 0.0 0.0 0.0)
  ; Set background depth to farthest
  (GL11/glClearDepth 1.0)
  ; Enable depth testing for z-culling
  (GL11/glEnable GL11/GL_DEPTH_TEST)
  ; Enable smooth shading
  (GL11/glShadeModel GL11/GL_SMOOTH)
  ; Set the type of depth-test
  (GL11/glDepthFunc GL11/GL_LEQUAL)
  ; Nice perspective corrections
  (GL11/glHint GL11/GL_PERSPECTIVE_CORRECTION_HINT GL11/GL_NICEST))

  ; (GL11/glMatrixMode GL11/GL_PROJECTION)
  ; (GL11/glOrtho 0.0 (:width @global)
  ;               (:height @global) 0.0 ;; Y is 0 at the top to match mouse coords
  ;               -1.0 1.0)
  ; (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn init
  []
  (init-gl))

(defn reload
  [])