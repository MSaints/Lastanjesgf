package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.UserModel;
import com.afrofx.code.anjesgf.sessionController;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Afro FX on 2/2/2018.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button but_autenticar, but_login_registar;
    EditText edit_number, edit_pin;

    private DatabaseHelper db;

    private sessionController se;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        db = new DatabaseHelper(this);

        se = new sessionController(this);

        but_autenticar = (Button) findViewById(R.id.but_login_entrar);
        but_autenticar.setOnClickListener(this);

        but_login_registar = (Button) findViewById(R.id.but_login_registar);
        but_login_registar.setOnClickListener(this);

        edit_number = (EditText) findViewById(R.id.edit_login_number);
        edit_pin = (EditText) findViewById(R.id.edit_login_pin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_login_entrar:
                login();
                break;

            case R.id.but_login_registar:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            default:
        }
    }

    private void login() {

        String num = edit_number.getText().toString();
        String pinn = edit_pin.getText().toString();

        if (num.isEmpty() || pinn.isEmpty() || pinn == "" || num == "") {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Preencha Todos Campos!")
                    .show();
        } else {
            int userNumber = Integer.parseInt(num);
            int userPin = Integer.parseInt(pinn);

            UserModel userModel = db.userLogin(userNumber, userPin);

            if (userModel != null) {
                se.setLoggedIn(true);
                Intent s = new Intent(this, MainScreenActivity.class);
                String nome = userModel.getUsuario_nome();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name_user", nome);
                editor.putInt("id_user", userModel.getId_usuario());
                editor.putInt("user_numero", userModel.getUsuario_numero());
                editor.commit();
                startActivity(s);
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Dados Errados")
                        .show();
            }
        }
    }
}
