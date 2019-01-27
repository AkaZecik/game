package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
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
    private final Texture backgroundTexture;
    private final Stage hud;
    private final Music chaseMusic;
    private final Sound chomp;
    private final Label scoreLabel;
    private final Label timeLabel;
    private final GameObjectDrawing cheeseDrawing;
    private final GameObjectDrawing mouseDrawing;
    private final GameObjectDrawing catDrawing;
    private final GameObjectDrawing trapDrawing;
    private final GameObject mouse;
    private final GameObject cat;
    private final Array<GameObject> traps;
    private long start;
    private int score;
    private int maxCheese;
    private int maxTraps;
    private float mouseSpeedFactor;
    private GameObject cheese;
    private float secondsSinceStart;

    GameScreen(final TheGame game, int maxCheese, int maxTraps, float mouseSpeedFactor) {
        this.game = game;
        viewport = new ScreenViewport();
        hud = new Stage(viewport, this.game.batch);
        this.game.batch.setProjectionMatrix(viewport.getCamera().combined);
        hud.setDebugAll(true);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.getAssetManager().get("font6.ttf");
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

        backgroundTexture = game.getAssetManager().get("floor.jpg");

        chaseMusic = game.getAssetManager().get("chase.mp3");
        chaseMusic.setLooping(true);

        chomp = game.getAssetManager().get("chomp.mp3");

        this.maxCheese = maxCheese;
        this.maxTraps = maxTraps;
        this.mouseSpeedFactor = mouseSpeedFactor;

        mouseDrawing = new GameObjectDrawing(75, 75, game.getAssetManager().get("mouse.png"));
        mouseDrawing.setRotation(90);
        mouseDrawing.setPosition(viewport.getScreenWidth() / 2f, viewport.getScreenHeight() / 2f);
        mouse = new GameObject(viewport.getScreenWidth() / 2f, viewport.getScreenHeight() / 2f, 90, 0f);

        cheeseDrawing = new GameObjectDrawing(75, 75, game.getAssetManager().get("cheese2.png"));

        catDrawing = new GameObjectDrawing(116, 40, game.getAssetManager().get("cat.png"));
        cat = new GameObject(-200, -200, 0, 100f);

        do {
            spawnCheese();
        } while (mouseDrawing.overlap(cheeseDrawing));

        trapDrawing = new GameObjectDrawing(73, 40, game.getAssetManager().get("trap.png")/*Gdx.files.internal("trap.png")*/);
        traps = new Array<>();

        for (int i = 0; i < maxTraps / 3; ++i) {
            traps.add(randomTrap());
        }
    }

    private GameObject randomTrap() {
        int edge = MathUtils.random(3);
        float angle = MathUtils.random(MathUtils.PI) + edge * MathUtils.PI / 2;
        float x = 0;
        float y = 0;

        switch (edge) {
            case 0:
                x = MathUtils.random(1024);
                y = -40;
                break;
            case 1:
                x = 1064;
                y = MathUtils.random(768);
                break;
            case 2:
                x = MathUtils.random(1024);
                y = 828;
                break;
            case 3:
                x = -40;
                y = MathUtils.random(768);
                break;
        }

        return new GameObject(x, y, angle,
                100f + secondsSinceStart / 30f * 50f);
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

        if (-5 <= deltaX && deltaX <= 5 && -5 <= deltaY && deltaY <= 5) {
            mouse.speed = 0;
        } else {
            mouse.speed = 200f * mouseSpeedFactor;
            mouse.angle = MathUtils.atan2(cursorY - mouseY, cursorX - mouseX);
        }
    }

    private void progressWorld(float delta) {
        for (GameObject trap : traps) {
            trap.move(delta);

            if (trap.x < -40 || trap.x > 1064 || trap.y < -40 || trap.y > 828) {
                trap.copy(randomTrap());
            }
        }

        if (traps.size <= maxTraps && secondsSinceStart / 5 > traps.size) {
            traps.add(randomTrap());
        }

        cat.angle = MathUtils.atan2(mouse.y - cat.y, mouse.x - cat.x);
        cat.move(delta);
        mouse.move(delta);

        if (mouseDrawing.overlap(cheeseDrawing)) {
            ++score;
            chomp.play();
            float size = MathUtils.clamp(75f * score / maxCheese, 0f, 75f);
            mouseDrawing.resize(75f + size, 75f + size);

            do {
                spawnCheese();
            } while (mouseDrawing.overlap(cheeseDrawing));

            if (score == maxCheese) {
                scoreLabel.setColor(Color.GREEN);
            }
        }

        if (mouseDrawing.overlap(catDrawing)) {
            if (score < maxCheese) {
                Gdx.app.postRunnable(() -> {
                    dispose();
                    game.setScreen(new GameOverScreen(game, score, maxCheese, maxTraps, mouseSpeedFactor));
                });
            } else {
                Gdx.app.postRunnable(() -> {
                    dispose();
                    game.setScreen(new WinScreen(game, maxCheese, maxTraps, mouseSpeedFactor));
                });
            }
        }

        for (GameObject trap : traps) {
            trapDrawing.transform(trap);

            if (mouseDrawing.overlap(trapDrawing)) {
                Gdx.app.postRunnable(() -> {
                    dispose();
                    game.setScreen(new GameOverScreen(game, score, maxCheese, maxTraps, mouseSpeedFactor));
                });
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
            ((BitmapFont) game.getAssetManager().get("font7.ttf"))
                    .draw(game.batch, Integer.toString(MathUtils.ceil(4f - time)),
                            viewport.getScreenWidth() / 2f - 50, viewport.getScreenHeight() / 2f + 60);
            game.batch.end();
        } else {
            secondsSinceStart = time - 4f;
            processInput();
            progressWorld(delta);

            for (GameObject trap : traps) {
                trapDrawing.draw(game.batch, trap);
            }

            catDrawing.draw(game.batch, cat);
            cheeseDrawing.draw(game.batch, cheese);
            game.batch.end();

            scoreLabel.setText("Score " + score + "/" + maxCheese);
            timeLabel.setText("Time: " + new DecimalFormat("#0.0").format(secondsSinceStart));
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
        chaseMusic.stop(); // TODO
    }

    @Override
    public void dispose() {
    }

    private class GameObjectDrawing {
        private final Polygon polygon;
        private final PolygonRegion region;
        private final PolygonSprite sprite;
        private float width;
        private float height;

        GameObjectDrawing(float width, float height, Texture texture) {
            this.width = width;
            this.height = height;
            float[] vertices = new float[]{0, 0, width, 0, width, height, 0, height};
            polygon = new Polygon(vertices);
            setupPolygon();

            float textureWidth = texture.getWidth();
            float textureHeight = texture.getHeight();

            region = new PolygonRegion(new TextureRegion(texture),
                    new float[]{0, 0, textureWidth, 0, textureWidth, textureHeight, 0, textureHeight},
                    new short[]{0, 1, 2, 0, 3, 2});

            sprite = new PolygonSprite(region);
            setupSprite();

            setPosition(0, 0);
        }

        private void setupPolygon() {
            float[] vertices = new float[]{0, 0, width, 0, width, height, 0, height};
            polygon.setVertices(vertices);
            polygon.setOrigin(width / 2f, height / 2f);
        }

        private void setupSprite() {
            sprite.setSize(width, height);
            sprite.setOrigin(width / 2f, height / 2f);
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

        void resize(float width, float height) {
            this.width = width;
            this.height = height;
            setupPolygon();
            setupSprite();
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

        void copy(GameObject gameObject) {
            x = gameObject.x;
            y = gameObject.y;
            angle = gameObject.angle;
            speed = gameObject.speed;
        }
    }
}
