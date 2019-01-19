package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    private final SimpleGame game;
    private Texture dropImage;
    private Texture bucketImage;
    private Sound pistolSound;
    private Sound sniperSound;
    private Music menuMusic;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private long lastDropTime;
    private int dropsGathered;

    GameScreen(final SimpleGame game) {
        this.game = game;
        dropImage = new Texture("droplet.png");
        bucketImage = new Texture("bucket.png");
        pistolSound = Gdx.audio.newSound(Gdx.files.internal("pistol.wav"));
        sniperSound = Gdx.audio.newSound(Gdx.files.internal("sniper.wav"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));

        camera = new OrthographicCamera(800, 480);
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.x = 800f / 2f - 64f / 2f;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new Array<>();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.translate(0.5f, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 20, 480);
        game.batch.draw(bucketImage, bucket.x, bucket.y);

        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }

        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64f / 2f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 400 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 400 * Gdx.graphics.getDeltaTime();
        }

        if (bucket.x < 0) {
            bucket.x = 0;
        }

        if (bucket.x > 800 - 64) {
            bucket.x = 800 - 64;
        }

        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000) {
            spawnRaindrop();
        }

        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

            if (raindrop.y + 64 < 0) {
                iter.remove();
            }

            if (raindrop.overlaps(bucket)) {
                ++dropsGathered;
                pistolSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void show() {
        menuMusic.play();
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
        menuMusic.dispose();
        sniperSound.dispose();
        pistolSound.dispose();
        bucketImage.dispose();
        dropImage.dispose();
    }
}
