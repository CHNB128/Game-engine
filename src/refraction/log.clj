(ns refraction.log
  (:import
    (org.lwjgl.glfw GLFW GLFWErrorCallback)))

(defn- set-error-callback
  [global]
  (let [error-callback
        (GLFWErrorCallback/createPrint System/err)]
    (swap! global assoc :error-callback error-callback)
    (GLFW/glfwSetErrorCallback error-callback)))

(defn init
  [global]
  (set-error-callback global))

(defn close
  [global]
  (.free (:error-callback @global)))
