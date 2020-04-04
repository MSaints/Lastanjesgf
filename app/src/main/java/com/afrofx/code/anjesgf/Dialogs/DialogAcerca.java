package com.afrofx.code.anjesgf.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;

public class DialogAcerca extends AlertDialog {

    private String text1, text2;

    public DialogAcerca(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_acerca);



        TextView title = findViewById(R.id.definitionInfoTitle);
        TextView version = findViewById(R.id.aboutVersion);

        version.setText(R.string.app_version);

        title.setBackgroundColor(Constant.color);

    }
}
