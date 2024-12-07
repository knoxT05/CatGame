package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class EndScreen implements Screen {
    final Main game;
    Texture gameOver;

    public EndScreen(final Main game){
        this.game = game;
        gameOver = new Texture("EndBG.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        if(game.score > game.highScore){
            game.highScore = game.score;
        }

        input();

        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.draw(gameOver, 0, 0, worldWidth, worldHeight);

        game.font.draw(game.batch, "Final Score: " + game.score, 2.15f, 1.25f);
        game.font.draw(game.batch, "High Score: " + game.highScore, 2.30f, 0.75f);
        game.batch.end();
    }

    private void input(){
        if(game.num == 500 || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            game.score = 0;
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

