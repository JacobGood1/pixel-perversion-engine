package java_src;


import clojure.lang.AFn;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Created by jacobgood1 on 10/15/2015.
 */


public class Start {
    private static Application game;

    public static void start(
            LwjglApplicationConfiguration config,
            AFn create_fn,
            AFn dispose_fn,
            AFn render_fn,
            AFn resize_fn,
            AFn pause_fn,
            AFn resume_fn){
        game = new Application(config,
                create_fn,
                dispose_fn,
                render_fn,
                resize_fn,
                pause_fn,
                resume_fn);
        new LwjglApplication(game, config);
    }
    public static void reLoad(AFn create_fn, AFn dispose_fn, AFn render_fn, AFn resize_fn, AFn pause_fn, AFn resume_fn){
        game.reLoad(create_fn, dispose_fn, render_fn, resize_fn, pause_fn, resume_fn);
    }
}
