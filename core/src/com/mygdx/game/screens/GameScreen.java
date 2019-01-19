package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.TheGame;

import java.text.DecimalFormat;

public class GameScreen implements Screen {
    private final TheGame game;
    private final ScreenViewport viewport;
    private Stage hud;
    private long start;
    private int score;
    private int total;
    private Music chaseMusic;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont font;
    private Label scoreLabel;
    private Label timeLabel;

    GameScreen(final TheGame game) {
        this.game = game;
        viewport = new ScreenViewport();
        this.game.batch.setProjectionMatrix(viewport.getCamera().combined);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-MediumItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.RED;
        font = fontGenerator.generateFont(parameter);

        hud = new Stage(viewport, this.game.batch);
        hud.setDebugAll(true);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        scoreLabel = new Label("ABC", style);
        timeLabel = new Label("DEF", style);
        Container<Label> scoreLabelContainer = new Container<>(scoreLabel).top().right();
        Container<Label> timeLabelContainer = new Container<>(timeLabel).top().left();
        scoreLabelContainer.setFillParent(true);
        timeLabelContainer.setFillParent(true);

        WidgetGroup widgetGroup = new WidgetGroup();
        widgetGroup.setFillParent(true);
        widgetGroup.addActor(scoreLabelContainer);
        widgetGroup.addActor(timeLabelContainer);
        hud.addActor(widgetGroup);

        chaseMusic = Gdx.audio.newMusic(Gdx.files.internal("chase.mp3"));
        chaseMusic.setLooping(true);

        total = 10;
    }

    @Override
    public void show() {
        start = TimeUtils.nanoTime();
        score = 0;
        chaseMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scoreLabel.setText("Score " + score + "/" + total);
        timeLabel.setText("Time: " + new DecimalFormat("#0.0")
                .format((TimeUtils.nanoTime() - start) / 1_000_000_000f));
        hud.act();
        hud.draw();
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
        fontGenerator.dispose();
        font.dispose();
        chaseMusic.dispose();
    }
}
