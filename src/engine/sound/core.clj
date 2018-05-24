(ns engine.sound.core
  (:import
   (org.lwjgl.openal AL10 ALC10 ALC AL)
   (org.lwjgl.system MemoryStack)
   (org.lwjgl.stb STBVorbis)
   (org.lwjgl.system.libc LibCStdlib)))

(defonce global-audio-template
  {:device nil
   :context nil
   :listener nil})

(defonce audio-template
  {:buffer nil
   :source nil})

(defn init
  [global]
  ; create audio instance in global object
  (swap! global assoc :audio global-audio-template)
  (swap! global assoc-in [:audio :device]
         (ALC10/alcOpenDevice
          (ALC10/alcGetString 0 ALC10/ALC_DEVICE_SPECIFIER)))
  (let [attributes [0]]
    (swap! global assoc-in [:audio :context]
           (ALC10/alcCreateContext
            (-> @global (:audio) (:device))
            attributes)))
  (let [context (-> @global (:audio) (:context))]
    (ALC10/alcMakeContextCurrent context)))

; :deprecated    
; (defn check
;   [global]
;   (let [device
;         (-> @global (:audio) (:device))
;         alCapabilities
;         (AL/createCapabilities
;          (ALC/createCapabilities device))]
;     (println "OpenAL 1.1 support: " (alCapabilities/OpenAL11))
;     (println "OpenAL 1.0 support: " (alCapabilities/OpenAL11))))

(defn close
  [global]
  (ALC10/alcDestroyContext
   (-> @global
       (:audio)
       (:context)))
  (ALC10/alcCloseDevice
   (-> @global
       (:audio)
       (:device))))

(defn setup-audio
  [global audio]
  (swap! global assoc-in [:level :audio]
    (conj (-> @global (:level) (:audio)) audio)))

(defn play-audio 
  [audio]
  (AL/alSourcePlay (:source audio))
;   try {
;      //Wait for a second
;      Thread.sleep(1000)};
;  catch (InterruptedException ignored) {}

  
(defn delete-audio 
  [global]
  (AL/alDeleteSources (:source audio))
  (AL/alDeleteBuffers (:buffer audio))
  ; (swap! global dissoc )

(defn load-audio
  [global path-to-audio-file]
  (let [; Allocate space to store return information from the function
        stack 
        (MemoryStack/stackPush)
        channels-buffer 
        (.mallocInt stack 1)
        sample-rate-buffer 
        (.mallocInt stack 1)
        raw-buffer
        (STBVorbis/stb_vorbis_decode_filename
          path-to-audio-file
          channels-buffer
          sample-rate-buffer)
        channels
        (.get channelsBuffer 0)
        ; find the correct OpenAL format
        format 
        (cond
          (= channels 1)
          AL_FORMAT_MONO16 
          (= channels 1)
          AL_FORMAT_STEREO16 
          :else -1)
        sample-rate 
        (.get sampleRateBuffer 0)
        ; Request space for the buffer
        *buffer 
        (AL/alGenBuffers)
        ; Send the data to OpenAL
        _ (Al/alBufferData *buffer format raw-buffer sample-rate)
        ; Free the memory allocated by STB
        _ (free raw-buffer)
        *source (AL/alGenSources)
        _ (AL/alSourcei *source AL/AL_BUFFER *buffer)]
    (setup-audio global
      (-> audio-template
        (assoc :source *source)
        (assoc :buffer *buffer)))))

