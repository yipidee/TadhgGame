package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Canvas;
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
    private int BingoTop, BingoBottom;
    private double MAX_DY;
    final private double MAX_ROTATE = Math.PI/4;

    //calculate values, can't be final due to pixel density differences
    private int MAX_Y;
    private int MIN_Y;

    // member variables
    private Context mContext;
    private Drawable tadhg;
    private int mState;
    private int mLives;
    private boolean atTop, atBottom;

    public Tadhg(Context c, int w, int h){
        super();
        init(c, w, h);
    }

    public boolean isPastBingoTop(){
        boolean res = false;
        if (getY()<=BingoTop)res=true;
        return res;
    }

    public boolean isPastBingoBottom(){
        boolean res = false;
        if(getY()>=BingoBottom)res=true;
        return res;
    }

    public int getState(){
        return mState;
    }

    private void init(Context c, int w, int h){
        mContext=c;
        tadhg= ContextCompat.getDrawable(mContext,R.drawable.img_tadhg);
        setWidth(w);
        setHeight(h);
        setX(OFFSET_X);
        mLives = 3;

        //get display size
        int surfaceH=Utility.getSurfaceHeight();

        setY((surfaceH - 2 * h) / 2);
        MAX_Y=surfaceH-h-OFFSET_Y;
        MIN_Y=OFFSET_Y;
        setDy(0);
        setAngle(0);
        mState=FALLING;
        MAX_DY=(double)surfaceH/(double)1450;  // max velocity pixels per millisec
        FALLING_DDY=MAX_DY/550;  // falling acceleration
        FLYING_DDY=FALLING_DDY*-0.75;  // flying ac        BingoBottom=surfaceH-;celeration
        setDdy(FALLING_DDY);
        atTop=false;
        atBottom=false;

        //get bingo values for Ready mode
        BingoTop=(int)((MAX_DY*MAX_DY)/(2*Math.abs(FALLING_DDY)))+OFFSET_Y;
        BingoBottom=surfaceH-(int)((MAX_DY*MAX_DY)/(2*Math.abs(FLYING_DDY)))-h-OFFSET_Y;
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

    public void updatePhysics(long delta){

        if(delta<0) return;

        int initialY = getY();
        double initialDy = getDy();
        double initialAngle = getAngle();

        double newDy;
        double newAngle;
        int newY;

        switch(mState){
            case FALLING:
                newDy = initialDy+FALLING_DDY*delta;
                newAngle=initialAngle+ROT_VEL*delta;
                break;
            case FLYING:
                newDy = initialDy+FLYING_DDY*delta;
                newAngle=initialAngle-ROT_VEL*delta;
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
        newY = initialY + (int)((newDy+initialDy)/2*delta);
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
