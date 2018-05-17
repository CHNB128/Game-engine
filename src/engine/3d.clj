(ns engine.3d
  (:import 
    (org.lwjgl BufferUtils)
    (org.lwjgl.opengl GL GL11)
    (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))

(defn init-gl []
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
  (GL11/glHint GL11/GL_PERSPECTIVE_CORRECTION_HINT GL11/GL_NICEST)

  ; (GL11/glMatrixMode GL11/GL_PROJECTION)
  ; (GL11/glOrtho 0.0 (:width @globals)
  ;               (:height @globals) 0.0 ;; Y is 0 at the top to match mouse coords
  ;               -1.0 1.0)
  ; (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn display []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT  GL11/GL_DEPTH_BUFFER_BIT))
  (GL11/glMatrixMode GL11/GL_MODELVIEW))


(defn create-3d-obj 
  [{position-x :x position-y :y position-z :z}
   {rotation-x :x rotation-y :y rotation-z :z rotation-angle :angle}
   {scale-x :x scale-y :y scale-z :z}]
  (GL11/glLoadIdentity)
  (GL11/glTranslatef position-x position-y position-z)
  (GL11/glRotatef rotation-angle rotation-x rotation-y rotation-z)
  (GL11/glScalef scale-x scale-y scale-z))

  ; (GL11/glBegin GL11/GL_TRIANGLES)
  ; (do
  ;   (GL11/glColor3f 1.0 0.0 0.0)
  ;   (GL11/glVertex2i 100 0)
  ;   (GL11/glColor3f 0.0 1.0 0.0)
  ;   (GL11/glVertex2i -50 86.6)
  ;   (GL11/glColor3f 0.0 0.0 1.0)
  ;   (GL11/glVertex2i -50 -86.6))
  ; (GL11/glEnd))
