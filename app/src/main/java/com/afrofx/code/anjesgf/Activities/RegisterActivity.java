package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Afro FX on 2/2/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edit_nome, edit_numero, edit_pin, edit_repetir_pin, edit_email;
    Button but_registar;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        db = new DatabaseHelper(this);

        edit_nome = (EditText) findViewById(R.id.edit_nome_completo);
        edit_pin = (EditText) findViewById(R.id.edit_pin);
        edit_repetir_pin = (EditText) findViewById(R.id.edit_repetir_pin);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_numero = (EditText) findViewById(R.id.edit_numero);

        but_registar = (Button) findViewById(R.id.but_registar);
        but_registar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_registar:
                adicionarUser();
                break;
            default:
        }
    }

    public void adicionarUser(){

        String nome = edit_nome.getText().toString();
        String email = edit_email.getText().toString();

        String p = edit_pin.getText().toString().trim();
        String rp =  edit_repetir_pin.getText().toString().trim();
        String un =  edit_numero.getText().toString().trim();

        final int pin = !p.equals("")?Integer.parseInt(p) : 0;
        final int re_pin = !rp.equals("")?Integer.parseInt(rp) : 0;
        final int u_numero = !un.equals("")?Integer.parseInt(un) : 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        UserModel userModel = db.procurar(u_numero);

        if(nome ==""|| pin!=re_pin || edit_numero.length()!=9 || edit_pin.length()!=4){

            mensagem("Verifique as credenciais");
        }else{
            if(userModel==null) {
                userModel = new UserModel(nome, u_numero,pin,email, strDate);
                db.inserirUser(userModel);
                mensagem("Usuario Registado");
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }else {
                mensagem("O Numero ja foi usado");
            }
        }
    }

    private void mensagem(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }


}
