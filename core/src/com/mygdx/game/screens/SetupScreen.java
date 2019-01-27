package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.TheGame;

import java.text.DecimalFormat;

public class SetupScreen implements Screen {
    private final TheGame game;
    private final Texture backgroundTexture;
    private final Music theme;
    private final Music menu;
    private final Stage stage;
    private final BitmapFont font;
    private final FreeTypeFontGenerator fontGenerator;
    private final Skin skin;

    SetupScreen(final TheGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("tom_and_jerry1.jpg"));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        final Table table1 = new Table().top();
        table1.setFillParent(true);
        table1.padTop(50);
        stage.addActor(table1);

        table1.setDebug(true); // TODO remove

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 38;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        font = fontGenerator.generateFont(parameter);

        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.overFontColor = Color.YELLOW;
        buttonStyle.downFontColor = Color.RED;
        buttonStyle.fontColor = Color.WHITE;


        Label maxTrapsLabel = new Label("Max traps", labelStyle);
        table1.add(maxTrapsLabel).spaceRight(10);
        Slider maxTrapsSlider = new Slider(5, 50, 1, false, skin);
        maxTrapsSlider.setValue(30);
        table1.add(maxTrapsSlider).spaceRight(10);
        Label maxTrapsValue = new Label(Integer.toString((int) maxTrapsSlider.getValue()), labelStyle);
        table1.add(maxTrapsValue).width(60);
        table1.row();

        maxTrapsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maxTrapsValue.setText(Integer.toString((int) maxTrapsSlider.getValue()));
            }
        });

        Label maxCheese = new Label("Max cheese", labelStyle);
        table1.add(maxCheese).spaceRight(10);
        Slider maxCheeseSlider = new Slider(1, 30, 1, false, skin);
        maxCheeseSlider.setValue(15);
        table1.add(maxCheeseSlider).spaceRight(10);
        Label maxCheeseValue = new Label(Integer.toString((int) maxCheeseSlider.getValue()), labelStyle);
        table1.add(maxCheeseValue).width(60);
        table1.row();

        maxCheeseSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maxCheeseValue.setText(Integer.toString((int) maxCheeseSlider.getValue()));
            }
        });

        Label mouseSpeedLabel = new Label("Mouse speed", labelStyle);
        table1.add(mouseSpeedLabel).spaceRight(10);
        Slider mouseSpeedSlider = new Slider(1f, 3f, 0.1f, false, skin);
        mouseSpeedSlider.setValue(1f);
        table1.add(mouseSpeedSlider).spaceRight(10);
        Label mouseSpeedValue = new Label(new DecimalFormat("#0.0").format(mouseSpeedSlider.getValue()), labelStyle);
        table1.add(mouseSpeedValue).width(60);
        table1.row().spaceTop(100);

        mouseSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mouseSpeedValue.setText(new DecimalFormat("#0.0").format(mouseSpeedSlider.getValue()));
            }
        });

        final TextButton startButton = new TextButton("Start", buttonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                int maxTraps = (int) maxTrapsSlider.getValue();
                int maxCheese = (int) maxCheeseSlider.getValue();
                float mouseSpeed = mouseSpeedSlider.getValue();
                game.setScreen(new GameScreen(game, maxCheese, maxTraps, mouseSpeed));
            }
        });
        table1.add(startButton).spaceRight(100);

        final TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table1.add(backButton).colspan(2);
        table1.row();

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
        theme.dispose();
        skin.dispose();
        menu.dispose();
        stage.dispose();
    }
}
