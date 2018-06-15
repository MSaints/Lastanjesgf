package com.afrofx.code.anjesgf.ThemeSettings;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;

import java.util.ArrayList;
import java.util.List;


public class ColorChooseDialog extends AlertDialog {

    public ColorChooseDialog(Context context) {
        super(context);
    }

    private ImageButton one;
    private ImageButton two;
    private ImageButton three;
    private ImageButton four;
    private ImageButton five;
    private ImageButton six;
    private ImageButton seven;
    private ImageButton eight;

    private List<Integer> colors;
    private List<ImageButton> buttons;

    private ColorListener myColorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt_color);

        TextView title = findViewById(R.id.definitionInfoTitle);
        title.setBackgroundColor(Constant.color);

        one = findViewById(R.id.b1);
        two =  findViewById(R.id.b2);
        three =  findViewById(R.id.b3);
        four =  findViewById(R.id.b4);
        five =  findViewById(R.id.b5);
        six =  findViewById(R.id.b6);
        seven =  findViewById(R.id.b7);
        eight =  findViewById(R.id.b8);


        colors = new ArrayList<>();
        colors.add(Yellow);
        colors.add(pink);
        colors.add(Brown);
        colors.add(Green);
        colors.add(Grey);
        colors.add(Blue);
        colors.add(Orange);
        colors.add(Teal);

        buttons = new ArrayList<>();
        buttons.add(one);
        buttons.add(two);
        buttons.add(three);
        buttons.add(four);
        buttons.add(five);
        buttons.add(six);
        buttons.add(seven);
        buttons.add(eight);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Colorize();
        } else {
            ColorizeOld();
        }

        setListeners();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (myColorListener != null)
                myColorListener.OnColorClick(v, (int) v.getTag());
            dismiss();
        }
    };

    private void setListeners() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setTag(colors.get(i));
            buttons.get(i).setOnClickListener(listener);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Colorize() {
        for (int i = 0; i < buttons.size(); i++) {
            ShapeDrawable d = new ShapeDrawable(new OvalShape());
            d.setBounds(30, 30, 30, 30);
            Log.e("Shape drown no", i + "");
            buttons.get(i).setVisibility(View.INVISIBLE);

            d.getPaint().setStyle(Paint.Style.FILL);
            d.getPaint().setColor(colors.get(i));

            buttons.get(i).setBackground(d);
        }
        animate();
    }

    private void ColorizeOld() {
        for (int i = 0; i < buttons.size(); i++) {
            ShapeDrawable d = new ShapeDrawable(new OvalShape());
            d.getPaint().setColor(colors.get(i));
            d.getPaint().setStrokeWidth(1f);
            d.setBounds(30, 30, 30, 30);
            buttons.get(i).setVisibility(View.INVISIBLE);
            d.getPaint().setStyle(Paint.Style.FILL);
            d.getPaint().setColor(colors.get(i));
            buttons.get(i).setBackgroundDrawable(d);
        }
        animate();
    }


    private void animate() {
        Log.e("animate", "true");
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                Log.e("animator 1", "r");
                animator(one);
                animator(five);
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                animator(two);
                animator(six);
            }
        };

        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                animator(three);
                animator(seven);
            }
        };

        Runnable r4 = new Runnable() {
            @Override
            public void run() {
                animator(four);
                animator(eight);
            }
        };

        android.os.Handler handler = new android.os.Handler();

        int counter = 85;
        handler.postDelayed(r1, counter);
        handler.postDelayed(r2, counter);
        handler.postDelayed(r3, counter);
        handler.postDelayed(r4, counter);

    }


    private void animator(final ImageButton imageButton) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.color_item);
        animation.setInterpolator(new AccelerateInterpolator());
        imageButton.setAnimation(animation);
        imageButton.setVisibility(View.VISIBLE);
        animation.start();
    }

    //CONSTANTS
    private final int pink = 0xFFEA3438;
    private final int Blue = 0xFF03A9F4;
    private final int Teal = 0xFF009688;
    private final int Green = 0xFF4CAF50;
    private final int Yellow = 0xFF3F51B5;
    private final int Orange = 0xFFFF5722;
    private final int Brown = 0xff795548;
    private final int Grey = 0xFF212121;


    public void setColorListener(ColorListener listener) {
        this.myColorListener = listener;
    }

}
