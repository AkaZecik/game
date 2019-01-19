package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.TheGame;

public class MainMenuScreen implements Screen {
    private final TheGame game;
    private Texture backgroundTexture;
    private Music music;
    private Stage stage;
    private BitmapFont font;
    private FreeTypeFontGenerator fontGenerator;

    public MainMenuScreen(final TheGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("tom-and-jerry.jpg"));

        final Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 72;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        font = fontGenerator.generateFont(parameter);

        final TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.overFontColor = Color.YELLOW;
        style.downFontColor = Color.RED;
        style.fontColor = Color.WHITE;

        final TextButton startButton = new TextButton("Start", style);
        table.add(startButton);

        final TextButton exitButton = new TextButton("Exit", style);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (exitButton.isPressed()) {
                    dispose();
                    Gdx.app.exit();
                }
            }
        });

        table.add(exitButton);

        music = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
        music.setLooping(true);
    }

    @Override
    public void show() {
        music.play();
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
        font.dispose();
        backgroundTexture.dispose();
        music.dispose();
        stage.dispose();
    }
}
