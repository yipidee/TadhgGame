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
import java.util.Iterator;

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
 * 15.8.30    1  A. Connolly  Added RUNNING mode
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    class GameThread extends Thread{

        //Game states
        public final static int PAUSE = 0;
        public final static int READY = 1;
        public final static int RUNNING = 2;
        public final static int GAMEOVER = 3;

        //Game Constants
        private final static long SPAWN_TIME = 2000;
        private final static long GHOST_TIME = 7500;
        private final static double TADHG_RATIO = 1.4427;
        private final static double ENEMY_RATIO = 0.75;
        private final static long MIN_WAIT = 40;

        //member variables
        private Handler mHandler;
        private int mState;
        private Context mContext;
        private double mFPS;
        private long mLastDrawTime, mNowTime, mLastSpawnTime;
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
            tadhgHeight=(int)((double)Utility.getSurfaceHeight()/5.5);
            tadhgWidth = (int)((double)tadhgHeight*TADHG_RATIO);
            enemyDim =(int)((double)tadhgHeight*ENEMY_RATIO);

            tadhg = new Tadhg(mContext, tadhgWidth, tadhgHeight);
            enemies = new ArrayList<>();
            bg=new Background(mContext);

            mLastDrawTime=System.currentTimeMillis()+100;
            mLastSpawnTime = mLastDrawTime;

            setState(RUNNING);
        }

        //starts the game thread running
        public void run(){
            while(mRun){
                Canvas c = null;
                long delta=0;
                Iterator<Enemy> iterator;
                Enemy enemy;
                try{
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder){
                        switch(mState){
                            /**********************************************
                             * The READY mode is a demo of the game that
                             * runs by itself with no collision detection
                             * or scoring
                             */
                            case READY:
                                mNowTime=System.currentTimeMillis();
                                delta = mNowTime-mLastDrawTime;
                                if((mNowTime-mLastSpawnTime)>=SPAWN_TIME) {
                                    enemies.add(Enemy.spawn(mContext,Enemy.NORMAL,enemyDim,enemyDim));
                                    mLastSpawnTime=mNowTime;
                                }
                                iterator=enemies.iterator();
                                while(iterator.hasNext()){
                                    enemy=iterator.next();
                                    enemy.updatePhysics(delta);
                                    if(enemy.getX()<-enemyDim){
                                        iterator.remove();
                                    }
                                }
                                tadhg.updatePhysics(delta);
                                if(tadhg.isPastBingoBottom()&&tadhg.getState()==Tadhg.FALLING){
                                    tadhg.setState(Tadhg.FLYING);
                                }else if(tadhg.isPastBingoTop()&&tadhg.getState()==Tadhg.FLYING){
                                    tadhg.setState(Tadhg.FALLING);
                                }
                                bg.updatePhysics();
                                synchronized (mRunLock) {
                                    if (mRun) {
                                        bg.draw(c);
                                        tadhg.draw(c);
                                        iterator=enemies.iterator();
                                        while(iterator.hasNext()){
                                            iterator.next().draw(c);
                                        }
                                    }
                                }
                                mFPS=(double)1000/(double)(delta);
                                Log.i("FPS: ",Double.toString(mFPS));
                                //Log.i("iterator size: ",Integer.toString(enemies.size()));
                                break;

                            /**********************************************
                             * The RUNNING mode is the actual game.
                             * This has collision detection, user input
                             * score keeping and ghost time
                             */
                            case RUNNING:
                                mNowTime=System.currentTimeMillis();
                                delta = mNowTime-mLastDrawTime;
                                if((mNowTime-mLastSpawnTime)>=SPAWN_TIME) {
                                    enemies.add(Enemy.spawn(mContext,Enemy.NORMAL,enemyDim,enemyDim));
                                    mLastSpawnTime=mNowTime;
                                }
                                iterator=enemies.iterator();
                                while(iterator.hasNext()){
                                    enemy=iterator.next();
                                    if(enemy.getState()!=Enemy.EXPLODED) {
                                        enemy.updatePhysics(delta);
                                        if (enemy.getX() < -enemyDim) {
                                            iterator.remove();
                                        }
                                    }
                                    //Collision detection
                                    if(tadhg.intersect(enemy.getBB())){
                                        enemy.setState(Enemy.EXPLODED);
                                    }
                                }
                                tadhg.updatePhysics(delta);
                                if(tadhg.isPastBingoBottom()&&tadhg.getState()==Tadhg.FALLING){
                                    tadhg.setState(Tadhg.FLYING);
                                }else if(tadhg.isPastBingoTop()&&tadhg.getState()==Tadhg.FLYING){
                                    tadhg.setState(Tadhg.FALLING);
                                }
                                bg.updatePhysics();
                                synchronized (mRunLock) {
                                    if (mRun) {
                                        bg.draw(c);
                                        tadhg.draw(c);
                                        iterator=enemies.iterator();
                                        while(iterator.hasNext()){
                                            iterator.next().draw(c);
                                        }
                                    }
                                }
                                mFPS=(double)1000/(double)(delta);
                                Log.i("FPS: ",Double.toString(mFPS));
                                //Log.i("iterator size: ",Integer.toString(enemies.size()));
                                break;
                        }
                    }
                }finally{
                    if(c!=null){
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                if(delta<MIN_WAIT){
                    try {
                        sleep(MIN_WAIT - delta);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                mLastDrawTime=mNowTime;
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
