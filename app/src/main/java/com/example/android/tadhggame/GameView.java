package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
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
        private final static double TADHG_RATIO = 1.4427;
        private final static double ENEMY_RATIO = 0.6667;

        //member variables
        private Handler mHandler;
        private int mState;
        private Context mContext;

        private boolean mRun = false;
        private final Object mRunLock = new Object();

        private int BG_COLOUR;

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
        private int enemyDim;

        public GameThread(SurfaceHolder surfaceholder, Context c, Handler handler){
            mSurfaceHolder=surfaceholder;
            mContext=c;
            mHandler=handler;

            //determine suitable sizes for sprites
            Point p = Utility.getScreenExtents(mContext);
            tadhgHeight=p.y/6;
            tadhgWidth = (int)((double)tadhgHeight*TADHG_RATIO);
            enemyDim =(int)((double)tadhgHeight*ENEMY_RATIO);

            tadhg = new Tadhg(mContext, tadhgWidth, tadhgHeight);
            enemies = new ArrayList<>();

            BG_COLOUR = mContext.getResources().getColor(android.R.color.holo_green_light);

            setState(READY);
        }

        //starts the game thread running
        public void run(){
            while(mRun){
                Canvas c = null;
                try{
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder){
                        switch(mState){
                            case READY:
                                tadhg.updatePhysics(System.currentTimeMillis());
                                if(tadhg.atBottom()){
                                    tadhg.setState(Tadhg.FLYING);
                                }else if(tadhg.atTop()){
                                    tadhg.setState(Tadhg.FALLING);
                                }
                                synchronized (mRunLock) {
                                    if (mRun) {
                                        c.drawColor(BG_COLOUR);
                                        tadhg.draw(c);
                                    }
                                }
                        }
                    }
                }finally{
                    if(c!=null){
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }

        }

        public void setRunning(boolean isRunning){
            synchronized (mRunLock){
                mRun=isRunning;
            }
        }

        public void setState(int state){
            mState=state;
        }

        public boolean doTouch(MotionEvent event){
            synchronized (mSurfaceHolder){
                boolean evHandled = false;
                int action = event.getAction();
                switch(mState){
                    // handle a touch event while game is running
                    case RUNNING:
                        if(action==MotionEvent.ACTION_DOWN){
                            tadhg.setState(Tadhg.FLYING);
                        }else if(action==MotionEvent.ACTION_UP){
                            tadhg.setState(Tadhg.FALLING);
                        }
                        evHandled=true;
                        break;

                    case READY:
                        if(action==MotionEvent.ACTION_DOWN){
                            setState(RUNNING);
                        }
                        evHandled=true;
                        break;

                    case GAMEOVER:
                        //TODO code to restart game
                        break;
                }
                return evHandled;
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
                //as yet no need for this function, but just in case
                Log.i("handler", m.toString());
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
        boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int a, int b, int c){
        //do nattin'
    }

    public void surfaceCreated(SurfaceHolder holder){
        mThread.setRunning(true);
        mThread.start();
    }
}
