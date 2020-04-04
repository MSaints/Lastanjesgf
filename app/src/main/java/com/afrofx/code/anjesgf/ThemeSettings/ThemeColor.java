package com.afrofx.code.anjesgf.ThemeSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;

import com.afrofx.code.anjesgf.R;

public class ThemeColor {

    Constant constant;

    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    Context context;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public ThemeColor(Context context){
        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
        appColor = app_preferences.getInt("color", 0xFFEA3438);
        appTheme = app_preferences.getInt("theme", R.style.AppTheme);
        themeColor = appColor;
        constant.color = appColor;

        if (themeColor == 0){
            context.setTheme(Constant.theme);
        }else if (appTheme == 0){
            context.setTheme(Constant.theme);
        }else{
            context.setTheme(appTheme);
        }


        Drawable but_style_red = context.getResources().getDrawable( R.drawable.but_style_red);
        but_style_red.setTint(Constant.color);

        Drawable myIcon3 = context.getResources().getDrawable( R.drawable.ic_redimentos);
        myIcon3.setTint(Constant.color);

        Drawable myIcon4 = context.getResources().getDrawable( R.drawable.ic_stock);
        myIcon4.setTint(Constant.color);

        Drawable myIcon5 = context.getResources().getDrawable( R.drawable.ic_vendas);
        myIcon5.setTint(Constant.color);

        Drawable myIcon6 = context.getResources().getDrawable( R.drawable.ic_shop);
        myIcon6.setTint(Constant.color);

        Drawable myIcon1 = context.getResources().getDrawable( R.drawable.ic_despesas);
        myIcon1.setTint(Constant.color);

        Drawable myIcon2 = context.getResources().getDrawable( R.drawable.ic_caixa);
        myIcon2.setTint(Constant.color);


        Drawable ic_add_photo = context.getResources().getDrawable(R.drawable.ic_add_photo);
        ic_add_photo.setTint(Constant.color);



        Drawable ic_loss = context.getResources().getDrawable(R.drawable.ic_loss);
        ic_loss.setTint(Constant.color);

        Drawable ic_streetview = context.getResources().getDrawable(R.drawable.ic_streetview);
        ic_streetview.setTint(Constant.color);

        Drawable ic_note_add = context.getResources().getDrawable(R.drawable.ic_note_add);
        ic_note_add.setTint(Constant.color);

        Drawable ic_description = context.getDrawable(R.drawable.ic_description);
        ic_description.setTint(Constant.color);


        Drawable ic_full_shoping_cart = context.getResources().getDrawable(R.drawable.ic_full_shoping_cart);
        ic_full_shoping_cart.setTint(Constant.color);

        Drawable ic_local = context.getDrawable(R.drawable.ic_local);
        ic_local.setTint(Constant.color);

        Drawable ic_label = context.getDrawable(R.drawable.ic_label);
        ic_label.setTint(Constant.color);

        Drawable ic_trending_up = context.getDrawable(R.drawable.ic_trending_up);
        ic_trending_up.setTint(Constant.color);

        Drawable ic_collections_bookmark = context.getDrawable(R.drawable.ic_collections_bookmark);
        ic_collections_bookmark.setTint(Constant.color);

        Drawable ic_call_black = context.getDrawable(R.drawable.ic_call_black);
        ic_call_black.setTint(Constant.color);

        this.context = context;

    }
}
