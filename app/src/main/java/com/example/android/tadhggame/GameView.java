package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Canvas;
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
        private final static long MIN_WAIT = 40;

        //member variables
        private Handler mHandler;
        private int mState;
        private Context mContext;
        private double mFPS;
        private long mLastDrawTime, mNowTime;
        private boolean mRun = false;
        private final Object mRunLock = new Object();

        private SurfaceHolder mSurfaceHolder;

        private Tadhg tadhg;
        private ArrayList<Enemy> enemies;
        private Background bg;

        private int tadhgWidth;
        private int tadhgHeight;
        private int enemyDim;

        public GameThread(SurfaceHolder surfaceholder, Context c, Handler handler){
            mSurfaceHolder=surfaceholder;
            mContext=c;
            mHandler=handler;
        }

        public void initThreadObjects(){
            //determine suitable sizes for spr
            tadhgHeight=Utility.getSurfaceHeight()/5;
            tadhgWidth = (int)((double)tadhgHeight*TADHG_RATIO);
            enemyDim =(int)((double)tadhgHeight*ENEMY_RATIO);

            tadhg = new Tadhg(mContext, tadhgWidth, tadhgHeight);
            enemies = new ArrayList<>();
            bg=new Background(mContext);

            mLastDrawTime=System.currentTimeMillis();

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
                                mNowTime=System.currentTimeMillis();
                                tadhg.updatePhysics(mNowTime);
                                if(tadhg.isPastBingoBottom()&&tadhg.getState()==Tadhg.FALLING){
                                    tadhg.setState(Tadhg.FLYING);
                                }else if(tadhg.isPastBingoTop()&&tadhg.getState()==Tadhg.FLYING){
                                    tadhg.setState(Tadhg.FALLING);
                                }
                                bg.updatePhysics();
                                synchronized (mRunLock) {
                                    if (mRun) {
                                        mFPS=(double)1000/(double)(mNowTime-mLastDrawTime);
                                        Log.i("FPS: ",Double.toString(mFPS));
                                        bg.draw(c);
                                        tadhg.draw(c);
                                    }
                                }
                                if((mNowTime-mLastDrawTime)<MIN_WAIT){
                                    sleep(MIN_WAIT-(mNowTime-mLastDrawTime));
                                }
                                mLastDrawTime=mNowTime;
                                break;
                        }
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();                }finally{
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
                            //setState(RUNNING);
                            setState(READY);
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

    public void surfaceChanged(SurfaceHolder holder, int a, int w, int h){
        Utility.setSizes(w,h);
        mThread.initThreadObjects();
    }

    public void surfaceCreated(SurfaceHolder holder){
        mThread.setRunning(true);
        mThread.start();
    }
}
