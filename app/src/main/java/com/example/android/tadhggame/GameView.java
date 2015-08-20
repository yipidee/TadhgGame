package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

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

        //Game states
        public final static int PAUSE = 0;
        public final static int READY = 1;
        public final static int RUNNING = 2;
        public final static int GAMEOVER = 3;

        //Game Constants
        private final static long SPAWN_TIME = 500;
        private final static long GHOST_TIME = 7500;

        //member variables
        private Handler mHandler;
        private int mState;
        private Context mContext;

        private boolean mRun = false;
        private final Object mRunLock = new Object();

        private SurfaceHolder mSurfaceHolder;

        private Bitmap bg1;
        private Bitmap bg2;
        private Bitmap bg3;
        private Bitmap fg;

        private Paint scorePaint;
        private Paint messagePaint;

        private Tadhg tadhg;
        private ArrayList<Enemy> enemies;

        private int tadhgWidth;
        private int tadhgHeight;
        private int enemyWidth;
        private int enemyHeight;

        public GameThread(SurfaceHolder surfaceholder, Context c, Handler handler){
            mSurfaceHolder=surfaceholder;
            mContext=c;
            mHandler=handler;



            tadhg = new Tadhg(mContext, tadhgWidth, tadhgHeight);
            enemies = new ArrayList<>();
            enemies.add(Enemy.spawn(mContext,Enemy.NORMAL,enemyWidth,enemyHeight));
        }

        public boolean doTouch(MotionEvent event){
            synchronized (mSurfaceHolder){
                return true;
            }
        }
    }

    //GameView member variables
    Context mContext;
    GameThread mThread;

    public GameView(Context c, AttributeSet attr){
        super(c, attr);

        //get reference to the surface holder
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        //game thread
        mThread = new GameThread(holder,c,new Handler(){
            public void handleMessage(Message m){

            }
        });

        setFocusable(true);
    }

    public GameThread getThread(){
        return mThread;
    }

    // pickup touch events and pass to thread
    public boolean onTouchEvent(MotionEvent event){
        return mThread.doTouch(event);
    }

    public void surfaceDestroyed(SurfaceHolder holder){

    }

    public void surfaceChanged(SurfaceHolder holder, int a, int b, int c){

    }

    public void surfaceCreated(SurfaceHolder holder){

    }
}
