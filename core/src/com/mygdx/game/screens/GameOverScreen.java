package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

public class GameOverScreen implements Screen {
    private final TheGame game;
    private final Texture backgroundImage;
    private final Stage stage;
    private final FreeTypeFontGenerator fontGenerator;
    private final BitmapFont font;
    private final Sound gameOverSound;

    GameOverScreen(final TheGame game, int score, int maxCheese, int maxTraps, float mouseSpeedFactor) {
        this.game = game;
        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        final Table table = new Table().top();
        table.setFillParent(true);
        table.padTop(50);
        stage.addActor(table);

        backgroundImage = game.getAssetManager().get("tom_and_jerry3.jpg");
//        backgroundImage = new Texture(Gdx.files.internal("tom_and_jerry3.jpg"));

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 72;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        font = fontGenerator.generateFont(parameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.RED;
        Label gameOverLabel = new Label("Game Over!", labelStyle);
        Label scoreLabel = new Label("Your score: " + score + "/" + maxCheese, labelStyle);

        table.add(gameOverLabel).colspan(2);
        table.row();
        table.add(scoreLabel).colspan(2).spaceBottom(100);
        table.row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.overFontColor = Color.YELLOW;
        buttonStyle.downFontColor = Color.RED;
        buttonStyle.fontColor = Color.WHITE;
        TextButton playAgainButon = new TextButton("Play again", buttonStyle);
        TextButton menuButton = new TextButton("Menu", buttonStyle);

        playAgainButon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game, maxCheese, maxTraps, mouseSpeedFactor));
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(playAgainButon).spaceRight(100);
        table.add(menuButton);

        gameOverSound = game.getAssetManager().get("game_over.mp3");
//        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("game_over.mp3"));
    }

    @Override
    public void show() {
        gameOverSound.play();
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0);
        game.batch.end();
        stage.act();
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
        gameOverSound.stop();
    }

    @Override
    public void dispose() {
        font.dispose();
        fontGenerator.dispose();
//        gameOverSound.dispose();
        stage.dispose();
    }
}
