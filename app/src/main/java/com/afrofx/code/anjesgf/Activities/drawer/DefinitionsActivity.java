package com.afrofx.code.anjesgf.Activities.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.Dialogs.DialogInfo;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.sessionController;

public class DefinitionsActivity extends AppCompatActivity{

    private sessionController se;

    private  Toolbar toolbar;

    private ImageView info1, info3, info4, info2;

    private Switch infoSwitch1, infoSwitch3,infoSwitch2, infoSwitch4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }
        setContentView(R.layout.activity_definitions);

        se = new sessionController(this);


        toolbar =  findViewById(R.id.toolbarDefinitions);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        info1 = findViewById(R.id.definitonsInfo1);
        info3 = findViewById(R.id.definitonsInfo3);
        info4 = findViewById(R.id.definitonsInfo4);
        info2 = findViewById(R.id.definitonsInfo2);

        infoSwitch1 = findViewById(R.id.definitionSwitch1);
        infoSwitch3 = findViewById(R.id.definitionSwitch3);
        infoSwitch4 = findViewById(R.id.definitionSwitch4);
        infoSwitch2 = findViewById(R.id.definitionSwitch2);


        infoSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        boolean value1 = (mSharedPreference.getBoolean("activa_limite", false));
        boolean value2 = (mSharedPreference.getBoolean("activa_seguranca", false));


        infoSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("activa_seguranca", true);
                    editor.commit();
                }else{
                    editor.putBoolean("activa_seguranca", false);
                    editor.commit();
                }
            }
        });

        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(DefinitionsActivity.this,"Receber Notificações", getResources().getString(R.string.info_notifications));
                dialogInfo.show();
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(DefinitionsActivity.this,"Recibos e Facturas SMS", getResources().getString(R.string.info_sms));
                dialogInfo.show();
            }
        });

        info4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(DefinitionsActivity.this,"Limite nas Vendas", getResources().getString(R.string.info_vendas));
                dialogInfo.show();
            }
        });

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(DefinitionsActivity.this,"Controlo de Segurança", getResources().getString(R.string.info_seguranca));
                dialogInfo.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(DefinitionsActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
