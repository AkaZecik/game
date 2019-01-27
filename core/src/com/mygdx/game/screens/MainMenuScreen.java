package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    private final BitmapFont font1;
    private final FreeTypeFontGenerator fontGenerator;

    public MainMenuScreen(final TheGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("tom_and_jerry1.jpg"));

        final Table table = new Table().top();
        table.setFillParent(true);
        table.padTop(50);
        stage.addActor(table);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 72;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        font1 = fontGenerator.generateFont(parameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font1;
        labelStyle.fontColor = Color.BROWN;
        Label gameName = new Label("Tom 'n Jerry Game", labelStyle);
        table.add(gameName).colspan(2).spaceBottom(100);
        table.row();

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font1;
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

        theme = Gdx.audio.newMusic(Gdx.files.internal("tom_and_jerry_theme.mp3"));
        menu = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
        menu.setLooping(true);
        theme.setOnCompletionListener(music -> menu.play());

    }

    @Override
    public void show() {
        theme.play();
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
        fontGenerator.dispose();
        font1.dispose();
        backgroundTexture.dispose();
        theme.dispose();
        menu.dispose();
        stage.dispose();
    }
}
