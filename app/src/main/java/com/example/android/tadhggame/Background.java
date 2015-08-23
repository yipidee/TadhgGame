package com.example.android.tadhggame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Background class
 * Handles drawing and updating of background images.
 *
 * Date     Rev  Author       Description
 * =======  ===  ===========  ===========================
 * 15.8.22    0  A. Connolly  Initial implementation
 */
public class Background {

    //constants
    private static final int SHIFT_COUNT = 5;
    //member variables
    private static int frameCounter = 0;
    private Context mContext;

    private int mBG_Color;

    private Bitmap mMountains;
    private Bitmap mClouds;
    private Bitmap mHills;
    private Bitmap mForeGround;

    private Rect mMountSrc;
    private Rect mCloudSrc;
    private Rect mHillSrc;
    private Rect mFGSrc;

    private Rect mDstRect;

    public Background(Context c){
        mContext = c;
        Resources res = mContext.getResources();

        //load Bitmaps
        mMountains = BitmapFactory.decodeResource(res, R.drawable.mountain);

        //set source rectangles
        mMountSrc = new Rect(0,0,mMountains.getWidth()/2-10,mMountains.getHeight());

        //set dest rectangle
        mDstRect = new Rect(0,0,Utility.getSurfaceWidth(),Utility.getSurfaceHeight());
    }

    public void updatePhysics(){
        int rewindX=mMountains.getWidth()/2-10;

        frameCounter++;
        if(frameCounter>=SHIFT_COUNT) frameCounter=0;

        if(frameCounter%SHIFT_COUNT==0){
            mMountSrc.offset(3,0);
        }

        //drop src rect back to start to repeat background
        if(mMountSrc.left>rewindX) mMountSrc.offset(-rewindX,0);
    }

    public void draw(Canvas c){
        c.drawBitmap(mMountains,mMountSrc,mDstRect,null);
    }
}
