package com.afrofx.code.anjesgf.Activities.root;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_nome, edit_numero, edit_pin, edit_repetir_pin;
    Button but_registar;
    ImageView perfilImagem;

    final int REQUEST_CODE_GALLERY = 999;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.register_screen);

        Toolbar toolbar = findViewById(R.id.toolbarRegistarUser);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        edit_nome = findViewById(R.id.edit_nome_completo);
        edit_pin = findViewById(R.id.edit_pin);
        edit_repetir_pin = findViewById(R.id.edit_repetir_pin);
        edit_numero = findViewById(R.id.edit_numero);
        perfilImagem = findViewById(R.id.perfilImagem);

        perfilImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        but_registar = findViewById(R.id.but_registar);
        but_registar.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_registar:
                adicionarUser();
                break;
            default:
        }
    }

    public void adicionarUser() {

        String nome = edit_nome.getText().toString();
        String p = edit_pin.getText().toString().trim();
        String rp = edit_repetir_pin.getText().toString().trim();
        String un = edit_numero.getText().toString().trim();

        final long pin = !p.equals("") ? Long.parseLong(p) : 0;
        final long re_pin = !rp.equals("") ? Long.parseLong(rp) : 0;
        final long u_numero = !un.equals("") ? Long.parseLong(un) : 0;

        UserModel userModel = db.procurar(u_numero);

        if (nome.equals("") || pin != re_pin || edit_numero.length() != 9 || edit_pin.length() != 4) {
            janelaErro(null, "Preencha o Nome, \n Numero e Pin");
        } else {
            if (userModel == null) {
                userModel = new UserModel(u_numero, pin, nome, imageViewToByte(perfilImagem));
                db.inserirUser(userModel);
                janelaSucesso(null, "Usuário Registado");
            } else {
                janelaErro("Ooops", "Número já Registado");
            }
        }
    }


    public static byte[] imageViewToByte(ImageView image) {
        byte[] byteArray = null;
        try {
            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();

        }catch (Exception e){
            e.printStackTrace();
        }
        return byteArray;
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
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                perfilImagem.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
