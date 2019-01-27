package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.TheGame;

public class MainMenuScreen implements Screen {
    private final TheGame game;
    private final Texture backgroundTexture;
    private final Music theme;
    private final Music menu;
    private final Stage stage;

    public MainMenuScreen(final TheGame game) {
        this.game = game;

        game.getAssetManager().finishLoadingAsset("tom_and_jerry1.jpg");
        game.getAssetManager().finishLoadingAsset("font1.ttf");
        game.getAssetManager().finishLoadingAsset("font2.ttf");
        game.getAssetManager().finishLoadingAsset("tom_and_jerry_theme.mp3");
        game.getAssetManager().finishLoadingAsset("menu.mp3");

        stage = new Stage(new ScreenViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = game.getAssetManager().get("tom_and_jerry1.jpg");

        final Table table = new Table().top();
        table.setFillParent(true);
        table.padTop(50);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getAssetManager().get("font1.ttf");
        Label gameName = new Label("Tom 'n Jerry Game", labelStyle);
        table.add(gameName).colspan(2).spaceBottom(100);
        table.row();

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getAssetManager().get("font2.ttf");
        buttonStyle.overFontColor = Color.YELLOW;
        buttonStyle.downFontColor = Color.RED;
        buttonStyle.fontColor = Color.WHITE;

        final TextButton playButton = new TextButton("Play", buttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new SetupScreen(game));
            }
        });
        table.add(playButton).spaceRight(100);

        final TextButton exitButton = new TextButton("Exit", buttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Gdx.app.exit();
            }
        });
        table.add(exitButton);

        theme = game.getAssetManager().get("tom_and_jerry_theme.mp3");
        menu = game.getAssetManager().get("menu.mp3");
        menu.setLooping(true);
        theme.setOnCompletionListener(music -> menu.play());

    }

    @Override
    public void show() {
        if (!theme.isPlaying()) {
            theme.play();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0);
        stage.getBatch().end();
        stage.draw();
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
        stage.dispose();
    }
}
