package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Random;


/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    final Main game;

    Texture bgtexture1;
    Texture bgtexture2;
    Sprite catSprite;
    Rectangle catRectangle;
    Rectangle obstRectangle;
    Rectangle mouseRectangle;
    Vector2 touchPos;
    Array<Sprite> obstSprites;
    Array<Sprite> miceSprites;
    Texture cat1;
    Texture cat2;
    Texture cat3;
    Texture catCrouch;
    Texture pig;
    Texture hay;
    Texture chick;
    Texture sheep;
    Texture mouse1;
    Texture mouse2;
    Texture mouse3;
    float catTimer;
    float bgtimer;
    float obstTimer;
    float mouseTimer;
    float y;
    float vy = 0;
    float g = -9;
    int obstSelect;
    int mouseSelect;
    Random random;

    public GameScreen(final Main game){
        this.game = game;

        cat1 = new Texture("CatRun1.png");
        cat2 = new Texture("CatRun2.png");
        cat3 = new Texture("CatRun3.png");
        catCrouch = new Texture("catCrouch.png");
        bgtexture1 = new Texture("gameBG1.png");
        bgtexture2 = new Texture("gameBG2.png");
        chick = new Texture("chicken.png");
        hay = new Texture("haybale.png");
        pig = new Texture("piggy.png");
        sheep = new Texture("sheep.png");
        mouse1 = new Texture("mouse1.png");
        mouse2 = new Texture("mouse2.png");
        mouse3= new Texture("mouse3.png");

        catSprite = new Sprite(cat1);
        catSprite.setSize(0.60f, 1.5f);
        catSprite.setX(3.50f);

        touchPos= new Vector2();

        catRectangle = new Rectangle();
        obstRectangle = new Rectangle();
        mouseRectangle = new Rectangle();

        obstSprites = new Array<>();
        miceSprites = new Array<>();

        random = new Random();
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();

        y = Math.max(0, y + vy * Gdx.graphics.getDeltaTime());
        vy += g * Gdx.graphics.getDeltaTime();
    }

    private void input(){
        float speed = 4f;
        float y = catSprite.getY();
        float delta = Gdx.graphics.getDeltaTime();


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || game.num == 400 || Gdx.input.isKeyPressed(Input.Keys.D)) {
            catSprite.translateX(speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || game.num == 300 || Gdx.input.isKeyPressed(Input.Keys.A)) {
            catSprite.translateX(-speed * delta);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP) || game.num == 100 || Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(y == 0.0f){
                vy = 7.5f;
            }
        }else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || game.num == 200 || Gdx.input.isKeyPressed(Input.Keys.S)){
            catSprite.setSize(0.60f, 1.0f);
            catSprite.setTexture(catCrouch);
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            catSprite.setCenterX(touchPos.x);
        }
    }

    private void logic(){
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float catWidth = catSprite.getWidth();
        float catHeight = catSprite.getHeight();
        float delta = Gdx.graphics.getDeltaTime();

        catSprite.setX(MathUtils.clamp(catSprite.getX(), 0, worldWidth - catWidth));
        catRectangle.set(catSprite.getX(), catSprite.getY(), catWidth, catHeight);

        for (int i = obstSprites.size - 1; i >=0; i--){
            Sprite obstacle = obstSprites.get(i);
            float obstWidth = obstacle.getWidth();
            float obstHeight = obstacle.getHeight();

            obstacle.translateY(-1.8f * delta);
            obstRectangle.set(obstacle.getX(), obstacle.getY(), obstWidth-0.5f, obstHeight-0.5f);

            //System.out.println(catSprite.getY());
            if(obstacle.getY() < -obstHeight) obstSprites.removeIndex(i);

            if(obstRectangle.overlaps(catRectangle) && obstacle.getY() < 2 && obstacle.getY() > 0.5f){
                if(obstHeight == 1.75 && catSprite.getY() == 0 && !(Gdx.input.isKeyPressed(Input.Keys.DOWN) || game.num == 200 || Gdx.input.isKeyPressed(Input.Keys.S))){
                    game.setScreen(new EndScreen(game));
                } else if (obstHeight == 1 && catSprite.getY() == 0) {
                    game.setScreen(new EndScreen(game));
                } else if (obstHeight == 2 && !(Gdx.input.isKeyPressed(Input.Keys.DOWN) || game.num == 200 || Gdx.input.isKeyPressed(Input.Keys.S))){
                    game.setScreen(new EndScreen(game));
                } else if (obstHeight == 2.02f){
                    game.setScreen(new EndScreen(game));
                }

            }
        }

        obstTimer += delta;
        if (obstTimer > 1.4f){
            obstTimer = 0;
            createObstacle();
        }

        for(int i = miceSprites.size - 1;  i >= 0; i--){
            Sprite mouse = miceSprites.get(i);
            float mouseWidth = mouse.getWidth();
            float mouseHeight = mouse.getHeight();

            mouse.translateY(-1.8f * delta);
            mouseRectangle.set(mouse.getX(), mouse.getY(), mouseWidth, mouseHeight);

            if(mouse.getY() < -mouseHeight) miceSprites.removeIndex(i);

            if(mouseRectangle.overlaps(catRectangle) && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || game.num == 500)){
                game.score++;
                miceSprites.removeIndex(i);
            }
        }

        mouseTimer += delta;
        if(mouseTimer > 2f){
            mouseTimer = 0;
            createMice();
        }

        catTimer += delta;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || game.num == 200 || Gdx.input.isKeyPressed(Input.Keys.S)){
            catSprite.setTexture(catCrouch);
        } else if(catTimer > 0.25f && catTimer < 0.5f){
            catSprite.setSize(0.60f, 1.5f);
            catSprite.setTexture(cat2);
        } else if (catTimer >0.5f && catTimer < 0.75f) {
            catSprite.setSize(0.60f, 1.5f);
            catSprite.setTexture(cat3);
        } else if (catTimer > 0.75f) {
            catTimer = 0;
            catSprite.setSize(0.60f, 1.5f);
            catSprite.setTexture(cat1);
        }

    }

    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.draw(bgtexture1, 0, 0, worldWidth, worldHeight);

        bgtimer += Gdx.graphics.getDeltaTime();
        if(bgtimer > 0.25f && bgtimer < 0.5f){
            game.batch.draw(bgtexture2, 0, 0, worldWidth, worldHeight);
        } else if (bgtimer > 0.5f) {
            bgtimer = 0;
            game.batch.draw(bgtexture1, 0, 0, worldWidth, worldHeight);
        }

        game.font.draw(game.batch, "Mice caught: " + game.score, 0.2f, worldHeight);

        for (Sprite mouse : miceSprites){
            mouse.draw(game.batch);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || game.num == 200 || Gdx.input.isKeyPressed(Input.Keys.S)){
            catSprite.setY(y);
            catSprite.draw(game.batch);

            for (Sprite obstacle : obstSprites){
                obstacle.draw(game.batch);
            }
        }else{
            for (Sprite obstacle : obstSprites){
                obstacle.draw(game.batch);
            }

            catSprite.setY(y);
            catSprite.draw(game.batch);
        }



        game.batch.end();
    }

    private void createObstacle(){
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float obstWidth;
        float obstHeight;
        Sprite obstacle;

        obstSelect = random.nextInt(1,5);

        if (obstSelect == 1){
            obstWidth = 1;
            obstHeight = 1;
            obstacle = new Sprite(chick);
        }else if (obstSelect == 2){
            obstWidth = 2;
            obstHeight = 2.02f;
            obstacle = new Sprite(hay);
        }else if (obstSelect == 3){
            obstWidth = 1.75f;
            obstHeight = 1.75f;
            obstacle = new Sprite(pig);
        }else{
            obstWidth = 2;
            obstHeight = 2;
            obstacle = new Sprite(sheep);
        }

        obstacle.setSize(obstWidth, obstHeight);
        obstacle.setX(MathUtils.random(0F, worldWidth - obstWidth));
        obstacle.setY(worldHeight-2);
        obstSprites.add(obstacle);
    }

    private void createMice(){
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float mouseWidth;
        float mouseHeight;
        Sprite mouse;

        mouseSelect = random.nextInt(1,4);

        if(mouseSelect == 1){
            mouseWidth = 0.5f;
            mouseHeight = 0.5f;
            mouse = new Sprite(mouse1);
        }
        else if(mouseSelect == 2){
            mouseWidth = 0.6f;
            mouseHeight = 0.5f;
            mouse = new Sprite(mouse2);
        }
        else{
            mouseWidth = 0.6f;
            mouseHeight = 0.5f;
            mouse = new Sprite(mouse3);
        }

        mouse.setSize(mouseWidth, mouseHeight);
        mouse.setX(MathUtils.random(0F, worldWidth - mouseWidth));
        mouse.setY(worldHeight-2);
        miceSprites.add(mouse);
    }


    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        cat1.dispose();
        cat2.dispose();
        cat3.dispose();
        catCrouch.dispose();
        bgtexture1.dispose();
        bgtexture2.dispose();
        chick.dispose();
        hay.dispose();
        pig.dispose();
        sheep.dispose();
        mouse1.dispose();
        mouse2.dispose();
        mouse3.dispose();
    }
}
