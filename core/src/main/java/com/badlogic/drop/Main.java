package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;
    public FileHandle fontFile;
    public int score;
    public serialListener listener;
    public Thread ListenerThread;
    public int num;
    public int highScore;

    @Override
    public void create() {
        fontFile = new FileHandle("font/gameFontPixel.fnt");
        batch = new SpriteBatch();
        font = new BitmapFont(fontFile);
        viewport = new FitViewport(8,5);
        score = 0;


        font.setUseIntegerPositions(false);
        font.getData().setScale((viewport.getWorldHeight()) *1.25f / Gdx.graphics.getHeight());

        listener = new serialListener();
        ListenerThread = new Thread(listener);
        ListenerThread.setDaemon(true);
        ListenerThread.start();


        this.setScreen(new StartScreen(this));
    }

    public void render(){
        num = listener.serialListener();
        super.render();
    }

    public void dispose(){
        batch.dispose();
        font.dispose();
    }
}
