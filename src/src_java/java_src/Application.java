package java_src;

import clojure.lang.AFn;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;import java.lang.Override;


public class Application implements ApplicationListener {
    private AFn create_fn;
    private AFn dispose_fn;
    private AFn render_fn;
    private AFn resize_fn;
    private AFn pause_fn;
    private AFn resume_fn;

    public Application(
            LwjglApplicationConfiguration cfg,
            AFn create_fn,
            AFn dispose_fn,
            AFn render_fn,
            AFn resize_fn,
            AFn pause_fn,
            AFn resume_fn) {
        this.create_fn  = create_fn;
        this.dispose_fn = dispose_fn;
        this.render_fn  = render_fn;
        this.resize_fn  = resize_fn;
        this.pause_fn   = pause_fn;
        this.resume_fn  = resume_fn;
    }

    @Override
    public void create() {
        create_fn.invoke();
    }

    @Override
    public void dispose() {
        dispose_fn.invoke();
    }

    @Override
    public void render() {
        render_fn.invoke();
    }

    @Override
    public void resize(int width, int height) {
        resize_fn.invoke(width, height);
    }

    @Override
    public void pause() {
        pause_fn.invoke();
    }

    @Override
    public void resume() {
        resume_fn.invoke();
    }

    public void reLoad(AFn create_fn,
                      AFn dispose_fn,
                      AFn render_fn,
                      AFn resize_fn,
                      AFn pause_fn,
                      AFn resume_fn) {
        this.create_fn  = create_fn;
        this.dispose_fn = dispose_fn;
        this.render_fn  = render_fn;
        this.resize_fn  = resize_fn;
        this.pause_fn   = pause_fn;
        this.resume_fn  = resume_fn;
    }
}