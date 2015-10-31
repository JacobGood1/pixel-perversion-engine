(defproject pixel-perversion-engine "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.badlogicgames.gdx/gdx "1.7.0"]
                 [com.badlogicgames.gdx/gdx-freetype "1.7.0"]
                 [com.badlogicgames.gdx/gdx-freetype-platform "1.7.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-box2d "1.7.0"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.7.0"]
                 [com.badlogicgames.gdx/gdx-platform "1.7.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.7.0"
                  :classifier "natives-desktop"]
                 [instaparse "1.4.1"]]
  :repositories [["sonatype"
                  "https://oss.sonatype.org/content/repositories/releases/"]]
  :source-paths ["src"]
  :java-source-paths ["src/src_java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :repl-options {:init-ns pixel-perversion-engine.main})