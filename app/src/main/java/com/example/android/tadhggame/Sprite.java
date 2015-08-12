package com.example.android.tadhggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Base sprite class for all agents in Tadhg's game
 *
 * Date     Rev     Author         Description
 * =======  ===     ===========    ===========
 * 15.8.12    0     A. Connolly    Initial version
 *
 */
public class Sprite{

    //member variables
    private int x, y;          //the (x,y) coordinates of the sprite
    private int width,height;  //the sprite width and height
    private BitmapDrawable img;         //resource ID for sprite img

    public Sprite(){}

    public Sprite(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

    public void draw(Canvas c){
        c.drawBitmap();
    }

    public void setX(int x){
        this.x=x;
    }

    public void setY(int y){
        this.y=y;
    }

    public void setWidth(int w){
        this.width=w;
    }

    public void setHeight(int h){
        this.height=h;
    }

    public void setImg(BitmapDrawable img){
        this.img=img;
    }

    public int getX(int x){
        return this.x=x;
    }

    public int getY(int y){
        return this.y=y;
    }

    public int getWidth(int w){
        return this.width=w;
    }

    public int getHeight(int h){
        return this.height=h;
    }

    public Drawable getImg(BitmapDrawable img){
        return this.img=img;
    }
}