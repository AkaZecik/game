package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
    private final Sound gameOverSound;

    GameOverScreen(final TheGame game, int score, int maxCheese, int maxTraps, float mouseSpeedFactor) {
        this.game = game;

        game.getAssetManager().finishLoadingAsset("tom_and_jerry3.jpg");
        game.getAssetManager().finishLoadingAsset("font4.ttf");
        game.getAssetManager().finishLoadingAsset("font5.ttf");
        game.getAssetManager().finishLoadingAsset("game_over.mp3");

        stage = new Stage(new ScreenViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);

        final Table table = new Table().top();
        table.setFillParent(true);
        table.padTop(50);
        stage.addActor(table);

        backgroundImage = game.getAssetManager().get("tom_and_jerry3.jpg");

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getAssetManager().get("font4.ttf");
        Label gameOverLabel = new Label("Game Over!", labelStyle);
        Label scoreLabel = new Label("Your score: " + score + "/" + maxCheese, labelStyle);

        table.add(gameOverLabel).colspan(2);
        table.row();
        table.add(scoreLabel).colspan(2).spaceBottom(100);
        table.row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getAssetManager().get("font5.ttf");
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
    }

    @Override
    public void show() {
        gameOverSound.play();
    }

    @Override
    public void render(float delta) {
        game.getBatch().begin();
        game.getBatch().draw(backgroundImage, 0, 0);
        game.getBatch().end();
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
        stage.dispose();
    }
}
