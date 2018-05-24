(ns engine.graphic.render
  (:import
   (org.lwjgl BufferUtils)
   (org.lwjgl.opengl GL GL11)
   (org.lwjgl.glfw GLFW)))

(defn init-gl
  [global]
  ; This line is critical for LWJGL's interoperation with GLFW's
  ; OpenGL context, or any context that is managed externally.
  ; LWJGL detects the context that is current in the current thread,
  ; creates the GLCapabilities instance and makes the OpenGL
  ; bindings available for use.
  (GL/createCapabilities)
  ; (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
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
  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glOrtho 0.0
                (:width @global)
                (:height @global)
                0.0 ;; Y is 0 at the top to match mouse coords
                -1.0
                1.0)
  (GL11/glMatrixMode GL11/GL_MODELVIEW))

(defn update-delta-time [global]
  (swap! global assoc :delta-time (System/currentTimeMillis)))

(defn init
  [global]
  (init-gl global))

(defn rerender
  [global]
  ; clear the framebuffer
  (GL11/glClear
   (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))
  ; swap the color buffers
  (GLFW/glfwSwapBuffers (:window @global))
  (update-delta-time global))
