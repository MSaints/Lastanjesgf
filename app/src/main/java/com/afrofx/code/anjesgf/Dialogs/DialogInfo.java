package com.afrofx.code.anjesgf.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;

public class DialogInfo extends AlertDialog {

    private String text1, text2;

    SharedPreferences sharedPreferences, app_preferences;
    SharedPreferences.Editor editor;

    int appTheme;
    int appColor;

    public DialogInfo(Context context, String text1, String text2) {
        super(context);
        this.text1 = text1;
        this.text2 = text2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_definition_info);
        setCancelable(true);

        TextView title = findViewById(R.id.definitionInfoTitle);
        TextView description = findViewById(R.id.definitionInfoDescription);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);


        title.setText(text1);
        description.setText(text2);

        title.setBackgroundColor(appColor);
    }
}
