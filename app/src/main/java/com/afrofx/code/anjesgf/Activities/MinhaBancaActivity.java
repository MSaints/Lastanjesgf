package com.afrofx.code.anjesgf.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.CompanyModel;

public class MinhaBancaActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private CompanyModel companyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_banca);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMinhaBanca);
        setSupportActionBar(toolbar);

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
        final int id_user = (mSharedPreference.getInt("user_numero", 0));
        String value = (mSharedPreference.getString("name_user", "Default_Value"));

        TextView txt12 = (TextView)findViewById(R.id.txt_empresa_num_user);
        TextView txt13 = (TextView)findViewById(R.id.txt_empresa_perfil);

        txt12.setText(id_user+"");
        txt13.setText(value);

        mostrarInfo();

    }


    public void mostrarInfo(){
        db = new DatabaseHelper(this);

        Cursor cursor = db.empresaDetalhes();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Sem Dados do Negocio", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor != null && cursor.getString(1) != null){
                    int id_user = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_usuario")));
                    int nuit = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nuit_empresa")));
                    int contacto = Integer.parseInt(cursor.getString(cursor.getColumnIndex("numero_empresa")));
                    String nome = cursor.getString(cursor.getColumnIndex("dona_empresa"));
                    String empresa = cursor.getString(cursor.getColumnIndex("nome_empresa"));
                    String email= cursor.getString(cursor.getColumnIndex("email_empresa"));
                    String local= cursor.getString(cursor.getColumnIndex("localidade_empresa"));

                    TextView txt1 = (TextView)findViewById(R.id.txt_empresa_nome);
                    TextView txt2 = (TextView)findViewById(R.id.txt_empresa_proprietario);
                    TextView txt3 = (TextView)findViewById(R.id.txt_empresa_email);
                    TextView txt4 = (TextView)findViewById(R.id.txt_empresa_endereco);
                    TextView txt5 = (TextView)findViewById(R.id.txt_empresa_nuit);
                    TextView txt6 = (TextView)findViewById(R.id.txt_empresa_contacto);

                    companyModel = new CompanyModel(id_user, contacto, nuit, empresa, nome, email, local);

                    txt1.setText(companyModel.getNome_empresa());
                    txt2.setText(companyModel.getNome_proprietario());
                    txt3.setText(companyModel.getEmail());
                    txt4.setText(companyModel.getEndereco());
                    txt5.setText(companyModel.getNuit()+"");
                    txt6.setText(companyModel.getContacto()+"");
                }
            }
        }
    }


    public void editInformação(){
        LayoutInflater li = LayoutInflater.from(this);
        View v = li.inflate(R.layout.prompt_info_banca, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(v);

        final EditText emp_nuit = (EditText)v.findViewById(R.id.edit_empresa_nuit);
        final EditText emp_nome = (EditText)v.findViewById(R.id.edit_empresa_nome);
        final EditText emp_prop = (EditText)v.findViewById(R.id.edit_empresa_proprietario);
        final EditText emp_email = (EditText)v.findViewById(R.id.edit_empresa_email);
        final EditText emp_local = (EditText)v.findViewById(R.id.edit_empresa_endereco);
        final EditText emp_contact = (EditText)v.findViewById(R.id.edit_empresa_contacto);



        db = new DatabaseHelper(this);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final int id_user = (mSharedPreference.getInt("id_user", 0));

        alertDialogBuilder.setCancelable(true).setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Cursor cursor = db.empresaDetalhes();

                if(cursor.getCount()==0){
                    int nuit = Integer.parseInt(emp_nuit.getText().toString());
                    int contacto = Integer.parseInt(emp_contact.getText().toString());
                    String nome = emp_nome.getText().toString();
                    String proprietario = emp_prop.getText().toString();
                    String email = emp_email.getText().toString();
                    String local = emp_local.getText().toString();
                    companyModel =  new CompanyModel(id_user, contacto, nuit, nome, proprietario, email, local);
                    db.registaEmpresa(companyModel);
                    Toast.makeText(MinhaBancaActivity.this, "Dados Registados", Toast.LENGTH_SHORT).show();
                }else{

                }

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_topbar_icon, menu);
        return true;
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
