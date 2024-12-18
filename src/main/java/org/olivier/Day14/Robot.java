package org.olivier.Day14;

import org.olivier.Utils.Tuple;

public class Robot {


    private Tuple.Pair<Integer, Integer> curPos;
    private Tuple.Pair<Integer, Integer> startingPos;
    private Tuple.Pair<Integer, Integer> velocity;

    public Robot(Tuple.Pair<Integer, Integer> position, Tuple.Pair<Integer, Integer> velocity) {
        this.curPos = new Tuple.Pair<>(position.v0(), position.v1());
        this.startingPos = position;
        this.velocity = velocity;
    }

    public Tuple.Pair<Integer, Integer> getCurPos() {
        return curPos;
    }

    public void setCurPos(Tuple.Pair<Integer, Integer> curPos) {
        this.curPos = curPos;
    }

    public Tuple.Pair<Integer, Integer> getStartingPos() {
        return startingPos;
    }

    public void setStartingPos(Tuple.Pair<Integer, Integer> startingPos) {
        this.startingPos = startingPos;
    }

    public Tuple.Pair<Integer, Integer> getVelocity() {
        return velocity;
    }

    public void setVelocity(Tuple.Pair<Integer, Integer> velocity) {
        this.velocity = velocity;
    }
}
