package me.mortuza.drawline;

/**
 * Created by mortuza on 1/15/2017.
 */

public class ModelCoordinate {

    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;

    public ModelCoordinate(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    public int getxStart() {
        return xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyEnd(){
        return yEnd;
    }

}
