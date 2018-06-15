package com.afrofx.code.anjesgf.Activities.root;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.UserModel;
import com.afrofx.code.anjesgf.sessionController;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button but_autenticar, but_login_registar;
    EditText edit_number, edit_pin;
    TextView recuperaPin;
    LinearLayout loginDescription;

    private DatabaseHelper db;

    private sessionController se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.login_screen);

        db = new DatabaseHelper(this);

        se = new sessionController(this);

        but_autenticar = findViewById(R.id.but_login_entrar);
        but_autenticar.setOnClickListener(this);

        but_login_registar = findViewById(R.id.but_login_registar);
        but_login_registar.setOnClickListener(this);


        loginDescription = findViewById(R.id.loginDescription);
        loginDescription.setBackgroundColor(Constant.color);

        edit_number = findViewById(R.id.edit_login_number);
        edit_pin = findViewById(R.id.edit_login_pin);

        recuperaPin = findViewById(R.id.recuperaPin);
        recuperaPin.setOnClickListener(this);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                displayData();
//            }
//        }, 5000);
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
            case R.id.recuperaPin:
                recoverPin();
                break;
            default:
        }
    }

    private void login() {

        String num = edit_number.getText().toString();
        String pinn = edit_pin.getText().toString();

        if (num.isEmpty() || pinn.isEmpty() || pinn == "" || num == "") {
            janelaErro("Ooops", "Preencha Todos Campos!");
        } else {
            long userNumber = Long.parseLong(num);
            long userPin = Integer.parseInt(pinn);

            UserModel userModel = db.userLogin(userNumber, userPin);

            if (userModel != null) {
                se.setLoggedIn(true);
                Intent s = new Intent(this, MainScreenActivity.class);
                String nome = userModel.getUsuario_nome();
                byte [] imagem = userModel.getUsuario_imagem();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name_user", nome);
                editor.putLong("id_user", userModel.getId_usuario());
                editor.putLong("user_numero", userModel.getUsuario_numero());
                editor.commit();
                startActivity(s);
            }else{
                janelaErro("Ooops", "Dados Errados");
            }
        }
    }

    public void janelaErro(String titlo, String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .show();
    }

    public void janelaSucesso(String titlo, String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .show();
    }

    public void recoverPin() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.prompt_recupera_pin, null);
        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setView(v);

        final EditText recuperaNumero = v.findViewById(R.id.recupera_numero);
        final LinearLayout reccuperaEcra = v.findViewById(R.id.reccuperaEcra);
        final Button submeterPin = v.findViewById(R.id.submeterPin);

        reccuperaEcra.setBackgroundColor(Constant.color);

        db = new DatabaseHelper(this);

        submeterPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = recuperaNumero.getText().toString().trim();

                long Number =  !num.equals("") ? Long.parseLong(num) : 0;

                long pinnn;

                if (num.isEmpty() || num.length() < 9) {
                    janelaErro("Ooops", "Número Inválido");
                } else {
                    Cursor cursor = db.procUser();
                    if (cursor.getCount() == 0) {
                        janelaErro("Ooops", "Número não registado");
                    } else {
                        while (cursor.moveToNext()) {
                            if (Number == Long.parseLong(cursor.getString(4))) {
                                pinnn = Long.parseLong(cursor.getString(6));
                                sendSMS(Number + "", "O seu PIN E-CONTA é \n" + pinnn);
                                janelaSucesso("Concluido", "Verifique a Mensagem");
                            }
                        }

                    }
                }
            }
        });

        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
    }


    private void sendSMS(String phoneNumber, String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }
}
