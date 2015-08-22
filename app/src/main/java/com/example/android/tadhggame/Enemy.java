package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.WindowManager;

import java.util.Random;

/**
 * Enenmy class
 * Has a static spawn() function that randomly creates and places new Enemy characters on the screen
 * <p>
 * Extends: Sprite
 * <p>
 * Date     Rev  Author       Description
 * =======  ===  ===========  ===========================
 * 15.8.14    0  A. Connolly  Initial implementation
 */
public class Enemy extends Sprite {

    //Enemy types
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

    //member variable
    final private static Random rand = new Random(); //static random number generator
    private int mType;
    private Context mContext;
    private int mState = NORMAL; //default to normal
    private long lastDrawTime;
    private int amplitude;
    private double phase;
    private double frequency;
    private double widthCoefficient;
    private int initialY;

    //member drawables
    private Drawable img;
    private Drawable popped;
    private Drawable ghost;

    public Enemy(Context c) {
        super();
        init(c);
    }

    public static Enemy spawn(Context c, int state, int w, int h) {
        Enemy enemy = new Enemy(c);
        enemy.setWidth(w);
        enemy.setHeight(h);

        //get width of display
        WindowManager wm = (WindowManager) enemy.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);

        enemy.setX(p.x - w);
        enemy.setY(enemy.getAmplitude() + rand.nextInt(p.y - h - 2 * enemy.getAmplitude()));
        enemy.setState(state);
        enemy.setWidthCoefficient(Math.PI * 6 / (double) p.x);
        return enemy;
    }

    private double getWidthCoefficient() {
        return widthCoefficient;
    }

    private void setWidthCoefficient(double d) {
        widthCoefficient = d;
    }

    private void init(Context c) {
        this.setType(rand.nextInt(7));
        mContext = c;
        this.loadDrawables();
        amplitude = (int) (100 + rand.nextDouble() * 100);
        frequency = rand.nextDouble() * 10;
        phase = rand.nextDouble() * Math.PI / 2;
        lastDrawTime = System.currentTimeMillis();
        setInitialY(getY());
        setDx(-10);
    }

    private int getInitialY() {
        return initialY;
    }

    private void setInitialY(int y) {
        initialY = y;
    }

    private Context getContext() {
        return mContext;
    }

    public int getAmplitude() {
        return this.amplitude;
    }

    public void draw(Canvas c) {
        Drawable d;
        switch (mState) {
            case NORMAL:
                d = img;
                break;
            case GHOST:
                d = ghost;
                break;
            default:
                d = popped;
                break;
        }
        d.setBounds(
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth(),
                this.getY() + this.getHeight()
        );
        d.draw(c);
    }

    private void loadDrawables() {
        popped = ContextCompat.getDrawable(mContext, R.drawable.img_pop);
        ghost = ContextCompat.getDrawable(mContext, R.drawable.img_ghost);
        switch (mType) {
            case APPLE:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_apple);
                break;

            case WAN:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_wan);
                break;

            case NYAN:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_nyan);
                break;

            case BANANA:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_banana);
                break;

            case SHOE:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_shoe);
                break;

            case FISH:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_fish);
                break;

            case PUMPKIN:
                img = ContextCompat.getDrawable(mContext, R.drawable.img_pumpkin);
                break;
        }
    }

    public void updatePhysics(long timeNow) {
        long dt = timeNow - lastDrawTime;
        int newX, newY;
        newX = this.getX() + (int) (getDx() * dt);

        switch (mState) {
            default:
                newY = (int) (this.getInitialY() +
                        amplitude * Math.sin(frequency * ((double) newX * getWidthCoefficient()) + phase));
                break;

            case GHOST:
                newY = (int) (this.getInitialY() +
                        amplitude * Math.sin(frequency * ((double) newX * getWidthCoefficient()) + phase * rand.nextInt(10)));
                break;
        }
        this.setX(newX);
        this.setY(newY);
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