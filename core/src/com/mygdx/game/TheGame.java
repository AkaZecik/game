package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.scenes.MainMenuScreen;

public class TheGame extends Game {
    public SpriteBatch batch;
    public Skin skin;

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin = new Skin();

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
