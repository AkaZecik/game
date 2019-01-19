package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
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
    private BitmapFont hudFont;
    private BitmapFont countDownFont;
    private Label scoreLabel;
    private Label timeLabel;

    GameScreen(final TheGame game) {
        this.game = game;
        viewport = new ScreenViewport();
        hud = new Stage(viewport, this.game.batch);
        this.game.batch.setProjectionMatrix(viewport.getCamera().combined);
        hud.setDebugAll(true);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-MediumItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.YELLOW;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        hudFont = fontGenerator.generateFont(parameter);
        parameter.size = 120;
        parameter.borderWidth = 2;
        parameter.color = Color.WHITE;
        countDownFont = fontGenerator.generateFont(parameter);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = hudFont;
        scoreLabel = new Label("", style);
        timeLabel = new Label("", style);
        Container<Label> scoreLabelContainer = new Container<>(scoreLabel).top().right().pad(30);
        Container<Label> timeLabelContainer = new Container<>(timeLabel).top().left().pad(30);
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
        float time = (TimeUtils.nanoTime() - start) / 1_000_000_000f;

        if (time < 4) {
            game.batch.begin();
            countDownFont.draw(game.batch, Integer.toString(MathUtils.ceil(4f - time)),
                    viewport.getScreenWidth() / 2f - 50, viewport.getScreenHeight() / 2f + 60);
            game.batch.end();
        } else {
            scoreLabel.setText("Score " + score + "/" + total);
            timeLabel.setText("Time: " + new DecimalFormat("#0.0").format(time - 4f));
            hud.act();
            hud.draw();
        }

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
        hudFont.dispose();
        chaseMusic.dispose();
    }
}