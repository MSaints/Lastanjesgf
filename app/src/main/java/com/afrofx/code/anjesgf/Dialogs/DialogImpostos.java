package com.afrofx.code.anjesgf.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;

public class DialogImpostos extends AlertDialog {

    private Switch impostoSwitch1, impostoSwitch2, impostoSwitch3;

    private EditText imposto;

    private Context context;

    public DialogImpostos(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_impostos);

        dadosImposto();

        TextView definitionInfoTitle =  findViewById(R.id.definitionInfoTitle);
        definitionInfoTitle.setBackgroundColor(Constant.color);

    }


    private void dadosImposto() {
        impostoSwitch1 = findViewById(R.id.impostoSwitch1);
        impostoSwitch2 = findViewById(R.id.impostoSwitch2);
        impostoSwitch3 = findViewById(R.id.impostoSwitch3);

        ImageView info1 = findViewById(R.id.impostoInfo1);
        ImageView info2 = findViewById(R.id.impostoInfo2);
        ImageView info3 = findViewById(R.id.impostoInfo3);

        imposto = findViewById(R.id.customImposto);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = mSharedPreference.edit();

        boolean value = (mSharedPreference.getBoolean("activa_iva", false));
        float impostoValor = (mSharedPreference.getFloat("valor", 0));

        if (value && impostoValor == 17) {
            impostoSwitch1.setChecked(true);
        }
        if (value && impostoValor == 3) {
            impostoSwitch2.setChecked(true);
        }
        if (value && impostoValor != 17 && impostoValor != 3 && impostoValor != 0) {

            impostoSwitch3.setChecked(true);
            imposto.setText(impostoValor + "");
            imposto.setEnabled(false);
        }

        impostoSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("activa_iva", true);
                    editor.putFloat("valor", 17);
                    editor.commit();
                    impostoSwitch2.setChecked(false);
                    impostoSwitch3.setChecked(false);
                } else {
                    editor.putBoolean("activa_iva", false);
                    editor.commit();
                }
            }
        });

        impostoSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("activa_iva", true);
                    editor.putFloat("valor", 3);
                    editor.commit();
                    impostoSwitch1.setChecked(false);
                    impostoSwitch3.setChecked(false);
                } else {
                    editor.putBoolean("activa_iva", false);
                    editor.commit();
                }
            }
        });

        impostoSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final float customImposto = !imposto.getText().toString().trim().equals("") ?
                        Float.parseFloat(imposto.getText().toString().trim()) : 0;
                if (isChecked && customImposto > 0) {
                    editor.putBoolean("activa_iva", true);
                    editor.putFloat("valor", customImposto);
                    editor.commit();
                    imposto.setEnabled(false);//Estaou a desativar textfield
                    impostoSwitch1.setChecked(false);//Estaou a desativar switch de 17%
                    impostoSwitch2.setChecked(false);//Estaou a desativar switch de 3%
                } else {
                    imposto.setEnabled(true);//Estaou a activar switch
                    editor.putBoolean("activa_iva", false);
                    editor.commit();
                }
            }
        });

        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(context, "Cobrança do IVA", context.getResources().getString(R.string.info_iva));
                dialogInfo.show();
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(context, "Cobrança do IVA", context.getResources().getString(R.string.info_simplificado));
                dialogInfo.show();
            }
        });

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo dialogInfo = new DialogInfo(context, "Imposto Simplificado", context.getResources().getString(R.string.info_costum));
                dialogInfo.show();
            }
        });
    }
}
