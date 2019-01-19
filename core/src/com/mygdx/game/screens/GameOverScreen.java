package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    private final Stage stage;
    private final FreeTypeFontGenerator fontGenerator;
    private final BitmapFont font;
    private final Sound catScream;

    GameOverScreen(final TheGame game, int score, int max_score) {
        this.game = game;
        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);

        final Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 72;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        font = fontGenerator.generateFont(parameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label gameOverLabel = new Label("Game Over!", labelStyle);
        Label scoreLabel = new Label("Your score: " + score + "/" + max_score, labelStyle);

        table.add(gameOverLabel).colspan(2);
        table.row();
        table.add(scoreLabel).colspan(2);
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
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(playAgainButon);
        table.add(menuButton);

        catScream = Gdx.audio.newSound(Gdx.files.internal("cat_scream.wav"));
    }

    @Override
    public void show() {
        catScream.play();
    }

    @Override
    public void render(float delta) {
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

    }

    @Override
    public void dispose() {
        font.dispose();
        fontGenerator.dispose();
        catScream.dispose();
        stage.dispose();
    }
}
