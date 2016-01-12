package me.sudar.zxing;

import android.app.Activity;

/**
 * Created on 12/1/16.
 * @author  Sudar Abisheck < sudarabisheck@gmail.com >
 */
public class Zxing extends Activity {

    private static Zxing instance = new Zxing();

    private Integer orientation = null;

    //constructor
    private Zxing(){} // private because it prevents from instantiating more than once

    //method to get the instance
    public static Zxing getInstance(){ return instance;}

    public Zxing setOrientation (final Integer pref) {
        orientation = pref;
        return this;
    }

}
