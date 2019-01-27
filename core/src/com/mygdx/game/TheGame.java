package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Base64Coder;
import com.mygdx.game.screens.MainMenuScreen;

import java.util.ArrayList;
import java.util.Collections;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class TheGame extends Game {
    private volatile PolygonSpriteBatch batch;
    private volatile AssetManager assetManager;
    private FileHandle highscoreFileHandle;
    private ArrayList<Score> highscores;

    @Override
    public void create() {
        batch = new PolygonSpriteBatch();

        assetManager = new AssetManager();
        assetManager.load("skin/uiskin.json", Skin.class);
        assetManager.load("tom_and_jerry1.jpg", Texture.class);
        assetManager.load("tom_and_jerry2.jpg", Texture.class);
        assetManager.load("tom_and_jerry3.jpg", Texture.class);
        assetManager.load("floor.jpg", Texture.class);
        assetManager.load("mouse.png", Texture.class);
        assetManager.load("cat.png", Texture.class);
        assetManager.load("trap.png", Texture.class);
        assetManager.load("jerry.jpg", Texture.class);
        assetManager.load("cheese2.png", Texture.class);
        assetManager.load("chase.mp3", Music.class);
        assetManager.load("menu.mp3", Music.class);
        assetManager.load("cat_scream.wav", Music.class);
        assetManager.load("tom_and_jerry_theme.mp3", Music.class);
        assetManager.load("chomp.mp3", Sound.class);
        assetManager.load("game_over.mp3", Sound.class);
        assetManager.load("blower.mp3", Sound.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        loadFont("font1.ttf", "fonts/rubik/Rubik-Black.ttf", 72, 2, Color.BROWN, Color.BLACK);
        loadFont("font2.ttf", "fonts/rubik/Rubik-Black.ttf", 72, 2, Color.WHITE, Color.BLACK);
        loadFont("font3.ttf", "fonts/rubik/Rubik-Medium.ttf", 38, 2, Color.WHITE, Color.BLACK);
        loadFont("font4.ttf", "fonts/rubik/Rubik-Medium.ttf", 72, 2, Color.RED, Color.BLACK);
        loadFont("font5.ttf", "fonts/rubik/Rubik-Medium.ttf", 72, 2, Color.WHITE, Color.BLACK);
        loadFont("font6.ttf", "fonts/rubik/Rubik-MediumItalic.ttf", 24, 1, Color.YELLOW, Color.BLACK);
        loadFont("font7.ttf", "fonts/rubik/Rubik-MediumItalic.ttf", 120, 2, Color.WHITE, Color.BLACK);

        highscoreFileHandle = Gdx.files.local("bin/highscore.json");

        if (!highscoreFileHandle.exists()) {
            highscores = new ArrayList<>();
            saveHighscore();
        } else {
            loadHighscore();
        }

        this.setScreen(new MainMenuScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public PolygonSpriteBatch getBatch() {
        return batch;
    }

    private void loadFont(String newFontName, String fontName, int size, int borderWidth, Color textColor, Color borderColor) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontFileName = fontName;
        parameter.fontParameters.size = size;
        parameter.fontParameters.color = textColor;
        parameter.fontParameters.borderColor = borderColor;
        parameter.fontParameters.borderWidth = borderWidth;
        assetManager.load(newFontName, BitmapFont.class, parameter);
    }

    public void enterNewScore(Score score) {
        highscores.add(score);
        Collections.sort(highscores);

        if (highscores.size() >= 4) {
            highscores.remove(3);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadHighscore() {
        highscores = json.fromJson(ArrayList.class, Base64Coder.decodeString(highscoreFileHandle.readString()));
    }

    private void saveHighscore() {
        highscoreFileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(highscores)), false);
    }

    public ArrayList<Score> getHighscores() {
        return highscores;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        saveHighscore();
    }
}
