package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.sessionController;

public class DefinitionsActivity extends AppCompatActivity{

    private sessionController se;

    private  Toolbar toolbar;

    private ImageView info1, info2, info3, info4, info5;

    private Switch infoSwitch1, infoSwitch2, infoSwitch3, infoSwitch4, infoSwitch5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);
        se = new sessionController(this);


        toolbar = (Toolbar) findViewById(R.id.toolbarDefinitions);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        info1 = (ImageView)findViewById(R.id.definitonsInfo1);
        info2 = (ImageView)findViewById(R.id.definitonsInfo2);
        info3 = (ImageView)findViewById(R.id.definitonsInfo3);
        info4 = (ImageView)findViewById(R.id.definitonsInfo4);
        info5 = (ImageView)findViewById(R.id.definitonsInfo5);

        infoSwitch1 = (Switch)findViewById(R.id.definitionSwitch1);
        infoSwitch2 = (Switch)findViewById(R.id.definitionSwitch2);
        infoSwitch3 = (Switch)findViewById(R.id.definitionSwitch3);
        infoSwitch4 = (Switch)findViewById(R.id.definitionSwitch4);
        infoSwitch5 = (Switch)findViewById(R.id.definitionSwitch5);


        infoSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        boolean value = (mSharedPreference.getBoolean("activa_iva", false));
        boolean value1 = (mSharedPreference.getBoolean("activa_limite", false));
        boolean value2 = (mSharedPreference.getBoolean("activa_seguranca", false));

        if(value==true){
            infoSwitch2.setChecked(true);
        }
        if(value1==true){
            infoSwitch5.setChecked(true);
        }

        infoSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("activa_iva", true);
                    editor.commit();
                }else{
                    editor.putBoolean("activa_iva", false);
                    editor.commit();
                }
            }
        });

        infoSwitch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        infoSwitch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("activa_limite", true);
                    editor.commit();
                }else{
                    editor.putBoolean("activa_limite", false);
                    editor.commit();
                }
            }
        });



        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janelaInfo("Receber Notificações", getResources().getString(R.string.info_notifications));
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janelaInfo("Cobrança do IVA", getResources().getString(R.string.info_iva));
            }
        });

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janelaInfo("Recibos e Facturas SMS", getResources().getString(R.string.info_sms));
            }
        });

        info4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janelaInfo("Controlo de Segurança", getResources().getString(R.string.info_seguranca));
            }
        });

        info5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                janelaInfo("Limite de Quantidades", getResources().getString(R.string.info_vendas));
            }
        });
    }


    public void janelaInfo(String title, String description){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.prompt_definition_info, null);
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setView(v);

        TextView txt_title = (TextView)v.findViewById(R.id.definitionInfoTitle);
        TextView txt_description = (TextView)v.findViewById(R.id.definitionInfoDescription);

        txt_description.setText(description);
        txt_title.setText(title);

        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
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
