(ns pixel-perversion-engine.shader.shader
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics.glutils ShaderProgram]
           [com.badlogic.gdx.utils GdxRuntimeException]))

(defn shader-load
  [path]
  (.readString (.internal (Gdx/files) path)))

(defn shader-program
  [vert frag]
  (let [vert (shader-load vert)
        frag (shader-load frag)
        shader-program (new ShaderProgram vert frag)]
    (if (not (.isCompiled shader-program))
      (throw (new GdxRuntimeException (str "Could not compile shader: " (.getLog shader-program))))
      shader-program)))