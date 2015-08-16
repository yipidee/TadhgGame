package com.example.android.tadhggame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * GameView class
 * View used to implement drawing surface for game.
 *
 * <p>
 * Extends: SurfaceView
 * Implements: SurfaceHolder
 * <p>
 * Date     Rev  Author       Description
 * =======  ===  ===========  ===========================
 * 15.8.16    0  A. Connolly  Initial implementation
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    class GameThread extends Thread{

    }

    public GameView(Context c, AttributeSet attr){
        super(c, attr);
    }

    public void surfaceDestroyed(SurfaceHolder holder){

    }

    public void surfaceChanged(SurfaceHolder holder, int a, int b, int c){

    }

    public void surfaceCreated(SurfaceHolder holder){

    }
}
