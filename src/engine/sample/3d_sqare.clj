(ns engine.sample.3d-sqare
  (:import 
    (org.lwjgl BufferUtils)
    (org.lwjgl.opengl GL GL11)
    (org.lwjgl.glfw GLFW GLFWErrorCallback GLFWKeyCallback)))
    
(defn render []
  (GL11/glBegin GL11/GL_QUADS);                // Begin drawing the color cube with 6 quads
  ;// Top face (y = 1.0)
  ;// Define vertices in counter-clockwise (CCW) order with normal pointing out
  (GL11/glColor3f 0.0, 1.0, 0.0);     // Green
  (GL11/glVertex3f 1.0, 1.0, -1.0);
  (GL11/glVertex3f -1.0, 1.0, -1.0);
  (GL11/glVertex3f -1.0, 1.0,  1.0);
  (GL11/glVertex3f 1.0, 1.0,  1.0);

  ; // Bottom face (y = -1.0)
  (GL11/glColor3f 1.0, 0.5f, 0.0);     // Orange
  (GL11/glVertex3f 1.0, -1.0,  1.0);
  (GL11/glVertex3f -1.0, -1.0,  1.0);
  (GL11/glVertex3f -1.0, -1.0, -1.0);
  (GL11/glVertex3f 1.0, -1.0, -1.0);

  ; // Front face  (z = 1.0)
  (GL11/glColor3f 1.0, 0.0, 0.0);     // Red
  (GL11/glVertex3f  1.0,  1.0, 1.0);
  (GL11/glVertex3f -1.0,  1.0, 1.0);
  (GL11/glVertex3f -1.0, -1.0, 1.0);
  (GL11/glVertex3f 1.0, -1.0, 1.0);

  ; // Back face (z = -1.0)
  (GL11/glColor3f 1.0, 1.0, 0.0);     // Yellow
  (GL11/glVertex3f 1.0, -1.0, -1.0);
  (GL11/glVertex3f -1.0, -1.0, -1.0);
  (GL11/glVertex3f -1.0,  1.0, -1.0);
  (GL11/glVertex3f 1.0,  1.0, -1.0);

  ; // Left face (x = -1.0)
  (GL11/glColor3f 0.0, 0.0, 1.0);     // Blue
  (GL11/glVertex3f -1.0,  1.0,  1.0);
  (GL11/glVertex3f -1.0,  1.0, -1.0);
  (GL11/glVertex3f -1.0, -1.0, -1.0);
  (GL11/glVertex3f -1.0, -1.0,  1.0);

  // Right face (x = 1.0)
  (GL11/glColor3f(1.0, 0.0, 1.0);     // Magenta
  (GL11/glVertex3f(1.0,  1.0, -1.0);
  (GL11/glVertex3f(1.0,  1.0,  1.0);
  (GL11/glVertex3f(1.0, -1.0,  1.0);
  (GL11/glVertex3f(1.0, -1.0, -1.0);
  (GL11/glEnd();  // End of drawing color-cube
