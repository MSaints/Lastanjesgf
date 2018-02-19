package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.sessionController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Afro FX on 2/1/2018.
 */

public class SplashActivity extends AppCompatActivity {


    private sessionController se;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        se = new sessionController(this);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (!se.loggedIn()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                    finish();
                }

            }
        }, 3000);

    }
}
