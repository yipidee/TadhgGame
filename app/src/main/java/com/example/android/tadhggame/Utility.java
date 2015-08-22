package com.example.android.tadhggame;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Utility class
 * Collection of static methods common to objects in the
 *
 * publically accessible methods:
 *
 * getScreenExtents(Context c)
 *  returns Point of the bottom right corner
 *
 * Date     Rev  Author       Description
 * =======  ===  ===========  ===========================
 * 15.8.22    0  A. Connolly  Initial implementation
 */
public class Utility {

    //static Point variable to register size of screen
    private static Point p;

    //public method called to calculate and register screen size
    public static Point getScreenExtents(Context c){
        if(p==null) setP(c);
        return p;
    }

    //private function that actually sets the p variable
    private static void setP(Context c){
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        p = new Point();
        d.getSize(p);
    }
}
