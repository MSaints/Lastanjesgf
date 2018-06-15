package com.afrofx.code.anjesgf.Activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Dialogs.DialogFastSale;
import com.afrofx.code.anjesgf.Dialogs.DialogImpostos;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.CompanyModel;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MinhaBancaActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private CompanyModel companyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_minha_banca);

        Toolbar toolbar = findViewById(R.id.toolbarMinhaBanca);
        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMinhaBanca);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editInformação();
            }
        });

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final long id_user = (mSharedPreference.getLong("user_numero", 0));
        final long id_use = (mSharedPreference.getLong("id_user", 0));
        String value = (mSharedPreference.getString("name_user", "Default_Value"));

        TextView txt12 =  findViewById(R.id.txt_empresa_num_user);
        TextView txt13 =  findViewById(R.id.txt_empresa_perfil);

        CircleImageView imagePerfil = (CircleImageView) findViewById(R.id.imagePerfil2);

        try {
            imagePerfil.setImageBitmap(db.getImage(id_use));
        }catch (Exception e){
            e.printStackTrace();
        }



        TextView definitionInfoTitle =  findViewById(R.id.definitionInfoTitle);
        definitionInfoTitle.setBackgroundColor(Constant.color);

        View v1 =  findViewById(R.id.v1);
        v1.setBackgroundColor(Constant.color);

        Button butImposto =  findViewById(R.id.butImposto);

        txt12.setText(id_user + "");
        txt13.setText(value);

        mostrarInfo();

        Button fastsale = (Button) findViewById(R.id.fastSale);

        fastsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFastSale dialogFastSale = new DialogFastSale(MinhaBancaActivity.this);
                dialogFastSale.show();
            }
        });

        butImposto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogImpostos dialogImpostos = new DialogImpostos(MinhaBancaActivity.this);
                dialogImpostos.show();
            }
        });


    }



    public void mostrarInfo() {
        db = new DatabaseHelper(this);

        Cursor cursor = db.empresaDetalhes();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Sem Dados do Negocio", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor != null && cursor.getString(1) != null) {
                    long id_user = Long.parseLong(cursor.getString(cursor.getColumnIndex("id_usuario")));
                    long nuit = Long.parseLong(cursor.getString(cursor.getColumnIndex("nuit_empresa")));
                    long contacto = Long.parseLong(cursor.getString(cursor.getColumnIndex("numero_empresa")));
                    String nome = cursor.getString(cursor.getColumnIndex("dona_empresa"));
                    String empresa = cursor.getString(cursor.getColumnIndex("nome_empresa"));
                    String local = cursor.getString(cursor.getColumnIndex("localidade_empresa"));

                    TextView txt1 =  findViewById(R.id.txt_empresa_nome);
                    TextView txt2 =  findViewById(R.id.txt_empresa_proprietario);
                    TextView txt4 =  findViewById(R.id.txt_empresa_endereco);
                    TextView txt5 =  findViewById(R.id.txt_empresa_nuit);
                    TextView txt6 =  findViewById(R.id.txt_empresa_contacto);

                    companyModel = new CompanyModel(id_user, contacto, nuit, empresa, nome, local);

                    txt1.setText(companyModel.getNome_empresa());
                    txt2.setText(companyModel.getNome_proprietario());
                    txt4.setText(companyModel.getEndereco());
                    txt5.setText(companyModel.getNuit() + "");
                    txt6.setText(companyModel.getContacto() + "");
                }
            }
        }
    }


    public void editInformação() {
        LayoutInflater li = LayoutInflater.from(this);
        View v = li.inflate(R.layout.prompt_info_banca, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(v);

        final EditText emp_nuit = (EditText) v.findViewById(R.id.edit_empresa_nuit);
        final EditText emp_nome = (EditText) v.findViewById(R.id.edit_empresa_nome);
        final EditText emp_prop = (EditText) v.findViewById(R.id.edit_empresa_proprietario);
        final EditText emp_local = (EditText) v.findViewById(R.id.edit_empresa_endereco);
        final EditText emp_contact = (EditText) v.findViewById(R.id.edit_empresa_contacto);

        TextView definitionInfoTitle =  v.findViewById(R.id.definitionInfoTitle);
        definitionInfoTitle.setBackgroundColor(Constant.color);

        if(companyModel!=null){
            emp_nuit.setText(companyModel.getNuit()+"");
            emp_nome.setText(companyModel.getNome_empresa()+"");
            emp_prop.setText(companyModel.getNome_proprietario()+"");
            emp_local.setText(companyModel.getEndereco()+"");
            emp_contact.setText(companyModel.getContacto()+"");
        }

        db = new DatabaseHelper(this);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final long id_user = (mSharedPreference.getLong("id_user", 0));

        alertDialogBuilder.setCancelable(true).setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Cursor cursor = db.empresaDetalhes();

                long nuit = Long.parseLong(emp_nuit.getText().toString());
                long contacto = Long.parseLong(emp_contact.getText().toString());
                String nome = emp_nome.getText().toString();
                String proprietario = emp_prop.getText().toString();
                String local = emp_local.getText().toString();

                if (cursor.getCount() == 0) {
                    companyModel = new CompanyModel(id_user, contacto, nuit, nome, proprietario, local);
                    db.registaEmpresa(companyModel);
                    janelaSucesso(null, "Dados Registados");
                } else {
                    companyModel = new CompanyModel(id_user, contacto, nuit, nome, proprietario, local);
                    if(db.updateEmpresa(companyModel)){
                        janelaSucesso(null, "Dados Actualizados");
                    }
                }
                }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void janelaSucesso(String titlo, String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titlo)
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(MinhaBancaActivity.this, MinhaBancaActivity.class));
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(MinhaBancaActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
