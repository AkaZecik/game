package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.TheGame;

import java.text.DecimalFormat;

public class GameScreen implements Screen {
    private final TheGame game;
    private final ScreenViewport viewport;
    private Texture backgroundTexture;
    private Stage hud;
    private long start;
    private int score;
    private int max_score;
    private int max_traps;
    private Music chaseMusic;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont hudFont;
    private BitmapFont countDownFont;
    private Label scoreLabel;
    private Label timeLabel;
    private GameObjectDrawing cheeseDrawing;
    private GameObjectDrawing mouseDrawing;
    private GameObjectDrawing catDrawing;
    private GameObjectDrawing trapDrawing;
    private GameObject mouse;
    private GameObject cat;
    private GameObject cheese;
    private Array<GameObject> traps;

    GameScreen(final TheGame game) {
        this.game = game;
        viewport = new ScreenViewport();
        hud = new Stage(viewport, this.game.batch);
        this.game.batch.setProjectionMatrix(viewport.getCamera().combined);
        hud.setDebugAll(true);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rubik/Rubik-MediumItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.YELLOW;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        hudFont = fontGenerator.generateFont(parameter);
        parameter.size = 120;
        parameter.borderWidth = 2;
        parameter.color = Color.WHITE;
        countDownFont = fontGenerator.generateFont(parameter);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = hudFont;
        scoreLabel = new Label("", style);
        timeLabel = new Label("", style);
        Container<Label> scoreLabelContainer = new Container<>(scoreLabel).top().right().pad(30);
        Container<Label> timeLabelContainer = new Container<>(timeLabel).top().left().pad(30);
        scoreLabelContainer.setFillParent(true);
        timeLabelContainer.setFillParent(true);

        WidgetGroup widgetGroup = new WidgetGroup();
        widgetGroup.setFillParent(true);
        widgetGroup.addActor(scoreLabelContainer);
        widgetGroup.addActor(timeLabelContainer);
        hud.addActor(widgetGroup);

        backgroundTexture = new Texture(Gdx.files.internal("floor.jpg"));

        chaseMusic = Gdx.audio.newMusic(Gdx.files.internal("chase.mp3"));
        chaseMusic.setLooping(true);

        max_score = 10;
        max_traps = 15;

        mouseDrawing = new GameObjectDrawing(75, 75, Gdx.files.internal("mouse.png"));
        mouseDrawing.setRotation(90);
        mouseDrawing.setPosition(viewport.getScreenWidth() / 2f, viewport.getScreenHeight() / 2f);
        mouse = new GameObject(viewport.getScreenWidth() / 2f, viewport.getScreenHeight() / 2f, 90, 0f);

        cheeseDrawing = new GameObjectDrawing(75, 75, Gdx.files.internal("cheese.png"));

        catDrawing = new GameObjectDrawing(116, 40, Gdx.files.internal("cat.png"));
        cat = new GameObject(-200, -200, 0, 100f);

        do {
            spawnCheese();
        } while (mouseDrawing.overlap(cheeseDrawing));

        trapDrawing = new GameObjectDrawing(73, 40, Gdx.files.internal("trap.png"));
        traps = new Array<>();

        for (int i = 0; i < 5; ++i) {
            spawnRandomTrap();
        }
    }

    private void spawnRandomTrap() {
        int edge = MathUtils.random(3);
        float angle = MathUtils.random(MathUtils.PI) + edge * MathUtils.PI / 2;
        float x = 0;
        float y = 0;

        switch (edge) {
            case 0:
                x = MathUtils.random(1024);
                y = 0;
                break;
            case 1:
                x = 1024;
                y = MathUtils.random(768);
                break;
            case 2:
                x = MathUtils.random(1024);
                y = 768;
                break;
            case 3:
                x = 0;
                y = MathUtils.random(768);
                break;
        }

        traps.add(new GameObject(x, y, angle, 100f));
    }

    private void spawnCheese() {
        float x = MathUtils.random(1024f);
        float y = MathUtils.random(768f);
        cheese = new GameObject(x, y, 0, 0);
        cheeseDrawing.transform(cheese);
    }

    private void processInput() {
        float cursorX = Gdx.input.getX();
        float cursorY = 768 - Gdx.input.getY();
        float mouseX = mouse.x;
        float mouseY = mouse.y;
        float deltaX = cursorX - mouseX;
        float deltaY = cursorY - mouseY;

        if (-1 <= deltaX && deltaX <= 1 && -1 <= deltaY && deltaY <= 1) {
            mouse.speed = 0;
        } else {
            mouse.speed = 150f;
            mouse.angle = MathUtils.atan2(cursorY - mouseY, cursorX - mouseX);
        }
    }

    private void progressWorld(float delta) {
        for (GameObject trap : traps) {
            trap.move(delta);
        }

        cat.angle = MathUtils.atan2(mouse.y - cat.y, mouse.x - cat.x);
        cat.move(delta);
        mouse.move(delta);

        if (mouseDrawing.overlap(cheeseDrawing)) {
            ++score;
            System.out.println("SCORE!!!");
        }

        if (mouseDrawing.overlap(catDrawing)) {
            System.out.println("LOOSER");
        }

        for (GameObject trap : traps) {
            trapDrawing.transform(trap);

            if (mouseDrawing.overlap(trapDrawing)) {
                System.out.println("CAUGHT!");
            }
        }
    }

    @Override
    public void show() {
        start = TimeUtils.nanoTime();
        score = 0;
        chaseMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float time = (TimeUtils.nanoTime() - start) / 1_000_000_000f;

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);

        mouseDrawing.draw(game.batch, mouse);

        if (time < 4) {
            countDownFont.draw(game.batch, Integer.toString(MathUtils.ceil(4f - time)),
                    viewport.getScreenWidth() / 2f - 50, viewport.getScreenHeight() / 2f + 60);
            game.batch.end();
        } else {
            processInput();
            progressWorld(delta);

            for (GameObject trap : traps) {
                trapDrawing.draw(game.batch, trap);
            }

            catDrawing.draw(game.batch, cat);
            cheeseDrawing.draw(game.batch, cheese);
            game.batch.end();

            scoreLabel.setText("Score " + score + "/" + max_score);
            timeLabel.setText("Time: " + new DecimalFormat("#0.0").format(time - 4f));
            hud.act();
            hud.draw();
        }
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
        hudFont.dispose();
        chaseMusic.dispose();
        mouseDrawing.dispose();
        catDrawing.dispose();
        trapDrawing.dispose();
    }

    private class GameObjectDrawing {
        private final Polygon polygon;
        private final Texture trapTexture;
        private final PolygonRegion region;
        private final PolygonSprite sprite;
        private final float width;
        private final float height;

        GameObjectDrawing(float width, float height, FileHandle fileHandle) {
            this.width = width;
            this.height = height;
            float[] vertices = new float[]{0, 0, width, 0, width, height, 0, height};
            polygon = new Polygon(vertices);
            polygon.setOrigin(width / 2f, height / 2f);
            polygon.setPosition(0, 0);
            trapTexture = new Texture(fileHandle);
            float textureWidth = trapTexture.getWidth();
            float textureHeight = trapTexture.getHeight();

            region = new PolygonRegion(new TextureRegion(trapTexture),
                    new float[]{0, 0, textureWidth, 0, textureWidth, textureHeight, 0, textureHeight},
                    new short[]{0, 1, 2, 0, 3, 2});

            sprite = new PolygonSprite(region);
            sprite.setSize(width, height);
            sprite.setOrigin(width / 2f, height / 2f);

            setPosition(0, 0);
        }

        void setRotation(float radians) {
            int degrees = (int) (radians * 180 / MathUtils.PI);
            setRotation(degrees);
        }

        void setRotation(int degrees) {
            sprite.setRotation(degrees);
            polygon.setRotation(degrees);
        }

        void setPosition(float x, float y) {
            polygon.setPosition(x - width / 2f, y - height / 2f);
            sprite.setPosition(x - width / 2f, y - height / 2f);
        }

        void draw(PolygonSpriteBatch batch, GameObject gameObject) {
            transform(gameObject);
            sprite.draw(batch);
        }

        void dispose() {
            trapTexture.dispose();
        }

        void resize() {

        }

        void transform(GameObject gameObject) {
            setPosition(gameObject.x, gameObject.y);
            setRotation(gameObject.angle);
        }

        boolean overlap(GameObjectDrawing other) {
            return Intersector.overlapConvexPolygons(polygon, other.polygon);
        }
    }

    class GameObject {
        float x;
        float y;
        float angle;
        float speed;

        GameObject(float x, float y, float radians, float speed) {
            this.x = x;
            this.y = y;
            this.angle = radians;
            this.speed = speed;
        }

        GameObject(float x, float y, int degrees, float speed) {
            this(x, y, degrees * MathUtils.PI / 180, speed);
        }

        void move(float delta) {
            this.x += MathUtils.cos(angle) * speed * delta;
            this.y += MathUtils.sin(angle) * speed * delta;
        }
    }
}
