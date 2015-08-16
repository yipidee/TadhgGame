package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.WindowManager;

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
    final private double FALLING_DDY = 10;  //positive is down
    final private double FLYING_DDY = -7;
    final private double ROT_VEL = Math.PI/60;
    final private int OFFSET_X = 15;
    final private int OFFSET_Y = 15;
    final private double MAX_DY = 50;
    final private double MAX_ROTATE = Math.PI/4;
    //calculate values, can't be final due to pixel density differences
    private int MAX_Y;
    private int MIN_Y;

    // member variables
    private Context mContext;
    private Drawable tadhg;
    private int mState;
    private long lastDrawTime;

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

        //get display size
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);

        setY((p.y - 2 * h) / 2);
        MAX_Y=p.y-h-OFFSET_Y;
        MIN_Y=OFFSET_Y;
        setDy(0);
        setDdy(FALLING_DDY);
        setAngle(0);
        mState=FALLING;
        lastDrawTime=System.currentTimeMillis()+50;
    }

    private void updatePhysics(long timeNow){

        if(lastDrawTime>timeNow) return;

        int initialY = getY();
        double initialDy = getDy();
        double initialAngle = getAngle();
        long deltaT = timeNow - lastDrawTime;

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
        newY = initialY + (int)((newDy+initialDy)/2*deltaT);
        if(newY>=MAX_Y){
            newY=MAX_Y;
        }else if(newY<=MIN_Y){
            newY=MIN_Y;
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
        setDdy(newDy);
        setAngle(newAngle);
        lastDrawTime=timeNow;
    }

    public void draw(Canvas c){
        tadhg.setBounds(getX(),getY(),getX()+getWidth(),getY()+getHeight());
        tadhg.draw(c);
    }
}
