package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.mygdx.game.Score;
import com.mygdx.game.TheGame;

public class WinScreen implements Screen {
    private final TheGame game;
    private final Stage stage;
    private final Texture backgroundImage;
    private final Music catScream;
    private final Sound partyBlower;

    WinScreen(final TheGame game, int score, int maxCheese, int maxTraps, float mouseSpeedFactor, float time) {
        this.game = game;
        Score newScore = new Score(score, maxTraps, mouseSpeedFactor, time);
        game.enterNewScore(newScore);

        game.getAssetManager().finishLoadingAsset("jerry.jpg");
        game.getAssetManager().finishLoadingAsset("font4.ttf");
        game.getAssetManager().finishLoadingAsset("font5.ttf");
        game.getAssetManager().finishLoadingAsset("cat_scream.wav");
        game.getAssetManager().finishLoadingAsset("blower.mp3");

        stage = new Stage(new ScreenViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);

        final Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        backgroundImage = game.getAssetManager().get("jerry.jpg");

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getAssetManager().get("font4.ttf");
        Label winLabel = new Label("You won!", labelStyle);

        table.add(winLabel).colspan(2);
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

        catScream = game.getAssetManager().get("cat_scream.wav");
        partyBlower = game.getAssetManager().get("blower.mp3");
        catScream.setOnCompletionListener(music -> partyBlower.play());
    }

    @Override
    public void show() {
        catScream.play();
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
        catScream.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
