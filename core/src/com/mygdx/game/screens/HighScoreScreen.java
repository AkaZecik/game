package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

import java.util.ArrayList;

public class HighScoreScreen implements Screen {
    private final TheGame game;
    private final Texture backgroundTexture;
    private final Stage stage;

    HighScoreScreen(final TheGame game) {
        this.game = game;

        game.getAssetManager().finishLoadingAsset("tom_and_jerry2.jpg");
        game.getAssetManager().finishLoadingAsset("font3.ttf");

        stage = new Stage(new ScreenViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = game.getAssetManager().get("tom_and_jerry2.jpg");

        final Table table = new Table().top();
        table.setFillParent(true);
        table.padTop(50);
        stage.addActor(table);

        ArrayList<Score> highscores = game.getHighscores();
        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getAssetManager().get("font3.ttf");

        Label cheese = new Label("Cheese", labelStyle);
        Label traps = new Label("Traps", labelStyle);
        Label mouseSpeed = new Label("Speed", labelStyle);
        Label time = new Label("time", labelStyle);

        table.add(cheese).spaceRight(50);
        table.add(traps).spaceRight(50);
        table.add(mouseSpeed).spaceRight(50);
        table.add(time).spaceRight(50);
        table.row();

        for (Score score : highscores) {
            Label label1 = new Label(Integer.toString(score.eatenCheese), labelStyle);
            Label label2 = new Label(Integer.toString(score.maxTraps), labelStyle);
            Label label3 = new Label(Float.toString(score.mouseSpeed), labelStyle);
            Label label4 = new Label(Float.toString(score.time), labelStyle);
            table.add(label1).spaceRight(50);
            table.add(label2).spaceRight(50);
            table.add(label3).spaceRight(50);
            table.add(label4).spaceRight(50);
            table.row();
        }

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getAssetManager().get("font2.ttf");
        buttonStyle.overFontColor = Color.YELLOW;
        buttonStyle.downFontColor = Color.RED;
        buttonStyle.fontColor = Color.WHITE;

        final TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(backButton).colspan(4);
        table.row();
    }

    @Override
    public void show() {
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
        stage.dispose();
    }

}
