package com.mygdx.game.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.TheGame;

public class GameScreen implements Screen {
    private final TheGame game;
    private long start;

    GameScreen(final TheGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        start = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
