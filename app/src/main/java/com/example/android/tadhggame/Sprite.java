package com.example.android.tadhggame;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Base sprite class for all agents in Tadhg's game
 * <p>
 * Date     Rev     Author         Description
 * =======  ===     ===========    ===========
 * 15.8.12    0     A. Connolly    Initial version
 * 15.8.29    1     A. Connolly    Add bounding box
 */
public abstract class Sprite {

    //member variables
    private int x, y, lastX, lastY;          //the (x,y) coordinates of the sprite
    private double dx, dy;     //the horizontal and vertical velocity
    private double ddx, ddy;   //the horizontal and vertical acceleration
    private int width, height;  //the sprite width and height
    private double angle;
    private Rect mBB;  //bounding box matching size of sprite for collision detection

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
        this.lastX=x;
        this.lastY=y;
        this.width = width;
        this.height = height;
        mBB = new Rect(this.x,this.y,this.x+this.width,this.y+this.height);
    }

    abstract void draw(Canvas c);

    public void scaleBB(double scaleFactor){
        //scaled from centre, 1 results in no change
        if(mBB==null) {
            setBB(
                    this.x,
                    this.y,
                    this.x + this.width,
                    this.y + this.height
            );

        }
        if(scaleFactor!=1) {
            mBB.left += (int)((double)this.width * ( 1 - scaleFactor) / 2.0);
            mBB.right -= (int)((double)this.width * ( 1 - scaleFactor) / 2.0);
            mBB.top += (int)((double)this.width * ( 1 - scaleFactor) / 2.0);
            mBB.bottom -= (int)((double)this.width * ( 1 - scaleFactor) / 2.0);
        }
    }

    public void setBB(int left, int top, int right, int bottom){
        mBB = new Rect(
                left,
                top,
                right,
                bottom
        );
    }

    public void setBB(Rect r){
        mBB=r;
    }

    public boolean intersect(Rect r){
        return mBB.intersect(r);
    }

    public Rect getBB(){
        return mBB;
    }

    public void setX(int x) {
        this.lastX=this.x;
        this.x = x;
        if(mBB!=null){
            mBB.offset(this.x-this.lastX,0);
        }
    }

    public int getLastX(){
        return this.lastX;
    }

    public int getLastY(){
        return this.lastY;
    }

    public void setY(int y) {
        this.lastY=this.y;
        this.y = y;
        if(mBB!=null){
            mBB.offset(0,this.y-this.lastY);
        }
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