package com.afrofx.code.anjesgf.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;

public class DialogSecurityPin extends AlertDialog {

    private EditText password;
    private TextView definitionInfoTitle;
    private Button entrar;

    private DatabaseHelper db;
    private Context context;

    public DialogSecurityPin(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_lock_screen);

        db = new DatabaseHelper(context);

        entrar = findViewById(R.id.desbloquear);
        password = findViewById(R.id.promptPin);
        definitionInfoTitle = findViewById(R.id.definitionInfoTitle);

        entrar.setBackgroundColor(Constant.color);
        definitionInfoTitle.setBackgroundColor(Constant.color);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
