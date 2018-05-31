(ns refraction.core
  (:require
    [refraction.window :as window]
    [refraction.event :as event]
    [refraction.log :as log]))

(defonce global (atom {}))

(defn init []
  (log/init global)
  (window/init global 500 500 "test")
  (event/init global))
