(ns engine.utils.3d.model-loader
  (:require [clojure.java.io :refer :all]
            [clojure.string :as str]
            [clojure.core.matrix 
             :refer [matrix set-current-implementation]]))
  
;; use Vectorz as default matrix implementation1
(set-current-implementation :vectorz)  

(defn load-model [file-path]
  (let [vertex (atom [])
        textures (atom [])
        normals (atom [])
        indices (atom [])]
    (with-open [rdr (reader file-path)]
      (doseq [line-str (line-seq rdr)]
        (let [line (str/split line-str #" ")]
          (cond 
            (= "v" (first line))
            (reset! vertex 
              (conj @vertex 
                (matrix 
                  (list
                    (Float/parseFloat (get line 1))
                    (Float/parseFloat (get line 2))
                    (Float/parseFloat (get line 3))))))
            (= "vt" (first line))
            (reset! textures 
              (conj @textures 
                (matrix
                  (list 
                    (Float/parseFloat (get line 1))
                    (Float/parseFloat (get line 2))))))
            (= "vn" (first line))
            (reset! normals 
              (conj @normals
                (matrix
                  (list
                    (Float/parseFloat (get line 1))
                    (Float/parseFloat (get line 2))
                    (Float/parseFloat (get line 3))))))
            (= "f" (first line))
            (reset! indices
              (conj @indices
                (list
                  (let [face-data (str/split (get line 1) #"/")]
                    (list              
                      (get @vertex (dec (Integer/parseInt (get face-data 0))))
                      (get @textures (dec (Integer/parseInt (get face-data 1))))
                      (get @normals (dec (Integer/parseInt (get face-data 2))))))
                  (let [face-data (str/split (get line 2) #"/")]              
                    (list              
                      (get @vertex (dec (Integer/parseInt (get face-data 0))))
                      (get @textures (dec (Integer/parseInt (get face-data 1))))
                      (get @normals (dec (Integer/parseInt (get face-data 2))))))
                  (let [face-data (str/split (get line 3) #"/")]              
                    (list              
                      (get @vertex (dec (Integer/parseInt (get face-data 0))))
                      (get @textures (dec (Integer/parseInt (get face-data 1))))
                      (get @normals (dec (Integer/parseInt (get face-data 2))))))))))))) 
    @indices))
