package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SimpleGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
        Camera cam = new OrthographicCamera(123, 456);
        System.out.println(cam.viewportWidth);
        System.out.println(cam.viewportHeight);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
