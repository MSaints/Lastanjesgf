package com.afrofx.code.anjesgf;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Afro FX on 2/4/2018.
 */

public class sessionController {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Context ctx;

    public sessionController(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();

    }

    public void setLoggedIn(boolean logggedIn){
        editor.putBoolean("loggedInmode", logggedIn);
        editor.commit();
    }

    public boolean loggedIn(){
        return prefs.getBoolean("loggedInmode", false);
    }
}
