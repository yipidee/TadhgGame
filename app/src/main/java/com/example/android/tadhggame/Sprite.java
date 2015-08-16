package com.example.android.tadhggame;

import android.graphics.Canvas;

/**
 * Base sprite class for all agents in Tadhg's game
 * <p>
 * Date     Rev     Author         Description
 * =======  ===     ===========    ===========
 * 15.8.12    0     A. Connolly    Initial version
 */
public abstract class Sprite {

    //member variables
    private int x, y;          //the (x,y) coordinates of the sprite
    private double dx, dy;     //the horizontal and vertical velocity
    private double ddx, ddy;   //the horizontal and vertical acceleration
    private int width, height;  //the sprite width and height
    private double angle;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Sprite() {
    }

    public Sprite(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    abstract void draw(Canvas c);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getDdx() {
        return ddx;
    }

    public void setDdx(double ddx) {
        this.ddx = ddx;
    }

    public double getDdy() {
        return ddy;
    }

    public void setDdy(double ddy) {
        this.ddy = ddy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public int getX() {
        return this.x = x;
    }

    public int getY() {
        return this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}