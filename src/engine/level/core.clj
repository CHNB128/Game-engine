(ns engine.level.core
  (:import
   (java.util.Objects)
   (org.lwjgl.assimp Assimp)))

(defonce level-template
  {:resources nil
   :loop-fn nil})

(defn info []
  (Assimp/aiGetLegalString)
  (println "Major version: " (Assimp/aiGetVersionMajor))
  (println "Minor version: " (Assimp/aiGetVersionMinor))
  (println "Version Revision: " (Assimp/aiGetVersionRevision))
  (println "Compile flags: " (Assimp/aiGetCompileFlags))
  (println " ")
  (println "Avalible import format :")
  (run!
   (fn [x]
     (let [desc (Assimp/aiGetImportFormatDescription x)]
       (println (inc x) ":" (.mNameString desc) (.mFileExtensionsString desc))))
   (range 0 (Assimp/aiGetImportFormatCount)))
  (println " ")
  (println "Avalible export format :")
  (run!
   (fn [x]
     (let [desc (Assimp/aiGetImportFormatDescription x)]
       (println (inc x) ":" (.mNameString desc) (.mFileExtensionsString desc))))
   (range 0 (Assimp/aiGetExportFormatCount))))

(defn load-level
  [global path-to-level-file]
  (throw (Exception. "Method not yet implemented"))
  (as-> level-template $
    (swap! global assoc :leve $)))
