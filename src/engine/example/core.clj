(ns engine.core
  (:require [engine.alpha :as alpha]
            [engine.beta  :as beta]
            [engine.gamma :as gamma])
            ;;[engine.omega :as omega]
  (:import (org.lwjgl Version))
  (:gen-class))

;; ======================================================================
(defn start-cider-nrepl []
  (.start 
    (Thread. 
      (fn []
        (println "Starting Cider Nrepl Server Port 7888")
        (load-string (str "(require '[clojure.tools.nrepl.server :as nrepl-server])"
                          "(require '[cider.nrepl :as cider])"
                          "(nrepl-server/start-server :port 7888 :handler cider/cider-nrepl-handler)"))))))

;; ======================================================================
(defn -main
  [& args]
  (println "Hello, Lightweight Java Game Library! V" (Version/getVersion))
  (when (= "cider" (second args))
    (start-cider-nrepl))
  (cond
   (= "alpha" (first args)) (alpha/main)
   (= "beta"  (first args)) (beta/main)
   (= "gamma" (first args)) (gamma/main)
   ;;(= "omega" (first args)) (omega/main)
   :else (alpha/main)))
