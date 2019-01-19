package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.mygdx.game.screens.MainMenuScreen;

public class TheGame extends Game {
    public PolygonSpriteBatch batch;

    @Override
    public void create() {
        batch = new PolygonSpriteBatch();
        this.setScreen(new MainMenuScreen(this));
    }
}
