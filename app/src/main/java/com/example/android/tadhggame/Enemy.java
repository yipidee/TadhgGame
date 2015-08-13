package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Display;

import java.util.Random;

/**
 * Created by adrian on 13/08/15.
 */
public class Enemy extends Sprite {

    //constants
    final public static int SHOE = 0;
    final public static int WAN = 1;
    final public static int NYAN = 2;
    final public static int BANANA = 3;
    final public static int APPLE = 4;
    final public static int FISH = 5;
    final public static int PUMPKIN = 6;

    //enemy states
    final public static int NORMAL = 0;
    final public static int GHOST = 1;
    final public static int EXPLODED = 2;
    final private static Random rand = new Random();

    //constants
    final private int xVel = -10;

    //member variable
    private int mType;
    private Context mContext;
    private int mState = NORMAL; //default to normal
    private int lastDrawTime;
    private double amplitude;
    private double phase;
    private double frequency;

    //member drawables
    private Drawable img;
    private Drawable popped;
    private Drawable ghost;

    public Enemy(Context c) {
        super();
        init(c);
    }

    public Enemy(int x, int y, int width, int height, Context c){
        super(x, y, width, height);
        init(c);
    }

    private void init(Context c){
        this.setType(rand.nextInt(7));
        mContext=c;
        this.loadDrawables();
        amplitude=100+rand.nextDouble()*100;
        frequency=rand.nextDouble()*10;
        phase=rand.nextDouble()*Math.PI/2;
    }

    public static Enemy spawn(Context c, int state, int w, int h, int screenW, int screenH){
        Enemy enemy;
        int x,y;
        x=screenW-w;
        y=rand.nextInt(screenH-h);
        enemy = new Enemy(x, y, w, h, c);
        enemy.setState(state);
        return enemy;
    }

    public void draw(Canvas c){
        Drawable d;
        switch(mState){
            case NORMAL:
                d=img;
                break;
            case GHOST:
                d=ghost;
                break;
            case EXPLODED:
                d=popped;
                break;
        }
        d.setBounds(
                this.getX(),
                this.getY(),
                this.getX()+this.getWidth(),
                this.getY()+this.getHeight()
        );
        d.draw(c);
    }

    private void loadDrawables(){
        popped = ContextCompat.getDrawable(mContext, R.id.img_pop);
        ghost = ContextCompat.getDrawable(mContext, R.id.img_ghost);
        switch (mType) {
            case APPLE:
                img = ContextCompat.getDrawable(mContext, R.id.img_apple);
                break;

            case WAN:
                img = ContextCompat.getDrawable(mContext, R.id.img_wan);
                break;

            case NYAN:
                img = ContextCompat.getDrawable(mContext, R.id.img_nyan);
                break;

            case BANANA:
                img = ContextCompat.getDrawable(mContext, R.id.img_banana);
                break;

            case SHOE:
                img = ContextCompat.getDrawable(mContext, R.id.img_shoe);
                break;

            case FISH:
                img = ContextCompat.getDrawable(mContext, R.id.img_fish);
                break;
        }
    }

    public void updatePhysics(int timeNow) {
        int dt = timeNow-lastDrawTime;
        int newX,newY;
        newX=this.getX()+xVel*dt;

        switch (mState) {
            case NORMAL:
                switch (mType) {
                    case APPLE:
                        newY=(int)(this.getY()+amplitude*Math.sin(frequency*((double)newX)+phase));
                        break;

                    case WAN:
                        break;

                    case NYAN:
                        break;

                    case BANANA:
                        break;

                    case SHOE:
                        break;

                    case PUMPKIN:
                        break;
                }
            case GHOST:
                break;
        }
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;
    }
}
