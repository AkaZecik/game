package com.mygdx.game;

@SuppressWarnings("ALL")
public class Score implements Comparable<Score> {
    public final int eatenCheese;
    public final int maxTraps;
    public final float mouseSpeed;
    public final float time;

    public Score() {
        eatenCheese = 0;
        maxTraps = 0;
        mouseSpeed = 0;
        time = 0;
    }

    public Score(int eatenCheese, int maxTraps, float mouseSpeed, float time) {
        this.eatenCheese = eatenCheese;
        this.maxTraps = maxTraps;
        this.mouseSpeed = mouseSpeed;
        this.time = time;
    }

    @Override
    public int compareTo(Score o) {
        if (eatenCheese > o.eatenCheese) {
            return -1;
        } else if (eatenCheese < o.eatenCheese) {
            return 1;
        } else if (time > o.time) {
            return -1;
        } else if (time < o.time) {
            return 1;
        } else {
            return 0;
        }
    }
}
