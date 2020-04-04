package com.afrofx.code.anjesgf.Activities.drawer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.Dialogs.DialogInfo;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ColorChooseDialog;
import com.afrofx.code.anjesgf.ThemeSettings.ColorListener;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.Methods;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;

public class PersonalizarActivity extends AppCompatActivity {

    private Button colorButton;
    Methods methods;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ImageView info5;

    private Switch infoSwitch5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_personalizar);
        Toolbar toolbar = findViewById(R.id.toolbarPersonalizar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        colorButton = findViewById(R.id.colorButton);

        methods = new Methods();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        colorize();

        View p2 = findViewById(R.id.p2);
        p2.setBackgroundColor(Constant.color);

        info5 = findViewById(R.id.definitonsInfo5);

        infoSwitch5 = findViewById(R.id.definitionSwitch5);

        boolean value3 = (sharedPreferences.getBoolean("icon_view", true));

        if(!value3){
            infoSwitch5.setChecked(true);
        }else{
            infoSwitch5.setChecked(false);
        }

        infoSwitch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("icon_view", false);
                    editor.commit();
                }else{
                    editor.putBoolean("icon_view", true);
                    editor.commit();
                }
            }
        });

        info5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(PersonalizarActivity.this,"Icones de Ajuda", getResources().getString(R.string.info_ico_ajuda));
                dialogInfo.show();
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooseDialog dialog = new ColorChooseDialog(PersonalizarActivity.this);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        colorize();
                        Constant.color = color;

                        methods.setColorTheme();
                        editor.putInt("color", color);
                        editor.putInt("theme", Constant.theme);
                        editor.apply();

                        Intent intent = new Intent(PersonalizarActivity.this, MainScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void colorize() {
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(Constant.color);

        colorButton.setBackground(d);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(PersonalizarActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
