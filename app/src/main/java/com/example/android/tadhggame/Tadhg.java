package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Tadhg class
 * The hero of the game.
 * Can only move vertically at a fixed horizontal position.
 *
 * <p>
 * Extends: Sprite
 * <p>
 * Date     Rev  Author       Description
 * =======  ===  ===========  ===========================
 * 15.8.14    0  A. Connolly  Initial implementation
 */
public class Tadhg extends Sprite {

    //Tadhg states
    final static public int FLYING = 0;
    final static public int FALLING = 1;

    //physics constants
    private double FALLING_DDY;  //positive is down
    private double FLYING_DDY;
    final private double ROT_VEL = Math.PI/60;
    final private int OFFSET_X = 0;
    final private int OFFSET_Y = 15;
    private double MAX_DY;
    final private double MAX_ROTATE = Math.PI/4;

    //calculate values, can't be final due to pixel density differences
    private int MAX_Y;
    private int MIN_Y;

    // member variables
    private Context mContext;
    private Drawable tadhg;
    private int mState;
    private long mLastDrawTime;
    private int mLives;
    private boolean atTop, atBottom;

    public Tadhg(Context c, int w, int h){
        super();
        init(c, w, h);
    }

    private void init(Context c, int w, int h){
        mContext=c;
        tadhg= ContextCompat.getDrawable(mContext,R.drawable.img_tadhg);
        setWidth(w);
        setHeight(h);
        setX(OFFSET_X);
        mLives = 3;

        //get display size
        Point p = Utility.getScreenExtents(mContext);

        setY((p.y - 2 * h) / 2);
        MAX_Y=p.y-h-OFFSET_Y;
        MIN_Y=OFFSET_Y;
        setDy(0);
        setAngle(0);
        mState=FALLING;
        MAX_DY=(double)p.y/(double)2500;  // max velocity pixels per millisec
        FALLING_DDY=MAX_DY/1000;  // falling acceleration
        FLYING_DDY=FALLING_DDY*-0.75;  // flying acceleration
        setDdy(FALLING_DDY);
        atTop=false;
        atBottom=false;
        mLastDrawTime =System.currentTimeMillis();
    }

    public void livesDown(){
        mLives--;
    }

    public void setLives(int i){
        mLives=i;
    }

    public int getLives(){
        return mLives;
    }

    public void updatePhysics(long timeNow){

        if(mLastDrawTime >timeNow) return;

        int initialY = getY();
        double initialDy = getDy();
        double initialAngle = getAngle();
        long deltaT = timeNow - mLastDrawTime;

        double newDy;
        double newAngle;
        int newY;

        switch(mState){
            case FALLING:
                newDy = initialDy+FALLING_DDY*deltaT;
                newAngle=initialAngle+ROT_VEL*deltaT;
                break;
            case FLYING:
                newDy = initialDy+FLYING_DDY*deltaT;
                newAngle=initialAngle-ROT_VEL*deltaT;
                break;
            default:
                newDy = initialDy;
                newAngle=initialAngle;
                break;
        }

        if(Math.abs(newDy)>=MAX_DY){
            if (newDy>=MAX_DY){
                newDy=MAX_DY;
            }else{
                newDy=-MAX_DY;
            }
        }
        atTop=atBottom=false;
        newY = initialY + (int)((newDy+initialDy)/2*deltaT);
        if(newY>=MAX_Y){
            newY=MAX_Y;
            atBottom=true;
        }else if(newY<=MIN_Y){
            newY=MIN_Y;
            atTop=true;
        }
        if(Math.abs(newAngle)>=MAX_ROTATE){
            if(newAngle>=MAX_ROTATE){
                newAngle=MAX_ROTATE;
            }else{
                newAngle=-MAX_ROTATE;
            }
        }

        //update physics properties
        setY(newY);
        setDy(newDy);
        setAngle(newAngle);
        mLastDrawTime =timeNow;
    }

    public void draw(Canvas c){
        tadhg.setBounds(getX(),getY(),getX()+getWidth(),getY()+getHeight());
        tadhg.draw(c);
    }

    public boolean atBottom(){
        return atBottom;
    }

    public boolean atTop(){
        return atTop;
    }

    public void setState(int state){
        mState=state;
    }
}
