package com.example.android.tadhggame;

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

    //static canvas width and height variables
    private static boolean isSet = false;
    private static int w,h;

    //public method called to calculate and register screen size
    public static int getSurfaceWidth(){
        if(!isSet) return -1;
        return w;
    }

    public static int getSurfaceHeight(){
        if(!isSet) return -1;
        return h;
    }

    public static void setSizes(int width, int height){
        w=width;
        h=height;
        isSet=true;
    }
}
