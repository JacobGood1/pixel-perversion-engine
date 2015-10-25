(ns pixel-perversion-engine.config
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplicationConfiguration]))

(defn set-field
  [obj field arg]
  `(set! (~(symbol (str ".-" field)) ~obj) ~arg))

(defmacro set-all!
  [obj & fields-and-values]
  (conj (for [[field val] (partition 2 fields-and-values)]
          (set-field obj field val)) 'do))

(defmacro set-all-helper
  [obj & args]
  `(set-all! ~obj ~@(flatten (for [x args] [x x]))))

(defn new-config
  [& {:keys [a height full-screen
             override-density icon-paths
             title gles30-context-minor-version
             audio-device-buffer-size r use-gl-30
             icon-file-types resizable y disable-audio
             gles30-context-major-version
             audio-device-simultaneous-sources
             use-hdpi preferences-directory
             initial-background-color add-icon
             g samples width stencil
             audio-device-buffer-count
             foreground-fps
             background-fps v-sync-enabled depth
             allow-software-mode force-exit b x]
      :or {height 640
           width 480
           use-gl-30 true
           full-screen false}}]
  (let [config (LwjglApplicationConfiguration.)]
    (set-all! config height height
              width width
              useGL30 use-gl-30
              title title
              fullscreen full-screen)
    config))