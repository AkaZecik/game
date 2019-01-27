package com.mygdx.game;

@SuppressWarnings("ALL")
public class Score {
    private final int eatenCheese;
    private final int maxTraps;
    private final float mouseSpeed;
    private final float time;

    Score(int eatenCheese, int maxTraps, float mouseSpeed, float time) {
        this.eatenCheese = eatenCheese;
        this.maxTraps = maxTraps;
        this.mouseSpeed = mouseSpeed;
        this.time = time;
    }
}
