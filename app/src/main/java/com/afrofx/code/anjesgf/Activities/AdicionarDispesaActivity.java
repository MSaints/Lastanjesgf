package com.afrofx.code.anjesgf.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.DispesasModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdicionarDispesaActivity extends AppCompatActivity {

    private EditText txtValorDispesa, txtdescricaoDispesa, txtDataDispesa;
    private CheckBox checkEstadoDispesa;
    private Spinner spinnerCategoriaDispesa, spinnerContaDispesa;
    private List<String> dispesaListaCategoria = new ArrayList<>();
    private DatabaseHelper db;
    private DispesasModel dispesasModel;
    private Calendar myCalendar = Calendar.getInstance();
    private String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_dispesa);

        db = new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar9);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> lables = db.listContas();
        List<String> itens_despesa = db.listaDespesaCategoria();

        txtValorDispesa = (EditText) findViewById(R.id.txtCustoDispesa);
        txtdescricaoDispesa = (EditText) findViewById(R.id.txtDescricaoDespesa);
        txtDataDispesa = (EditText) findViewById(R.id.txtdataDispesa);

        spinnerCategoriaDispesa = (Spinner) findViewById(R.id.spinnerCategoriaDispesa);
        spinnerContaDispesa = (Spinner) findViewById(R.id.spinnerContaDispesa);

        checkEstadoDispesa = (CheckBox) findViewById(R.id.checkEstadoDispesa);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itens_despesa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDispesa.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContaDispesa.setAdapter(adapter2);
        spinnerContaDispesa.setPrompt("Escolha a Conta");

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM-dd-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                txtDataDispesa.setText(sdf.format(myCalendar.getTime()));
                data = sdf.format(myCalendar.getTime());
            }

        };

        txtDataDispesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AdicionarDispesaActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.concluirDispesa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistarDispesa();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }


    public void RegistarDispesa() {
        String descricao = txtdescricaoDispesa.getText().toString().trim();
        String Conta = spinnerContaDispesa.getSelectedItem().toString();
        String Categoria = spinnerCategoriaDispesa.getSelectedItem().toString();
        String data = txtDataDispesa.getText().toString().trim();
        String valor = txtValorDispesa.getText().toString().trim();
        double valorDispesa = !valor.equals("") ? Double.parseDouble(valor) : 0.0;

        db = new DatabaseHelper(this);

        int id_conta = db.idConta(Conta);
        int id_categoria= db.idCategoriaDispesa(Categoria);
        int estadoPagamento = 0;

        if (valorDispesa == 0.0) {
            Toast.makeText(this, "Adicione o Valor", Toast.LENGTH_LONG).show();
        } else {
            if (checkEstadoDispesa.isChecked()) {
                if(db.SaldoTotal(Conta)>=valorDispesa) {
                    estadoPagamento = 0;
                    ContaModel dispesasModel = new ContaModel(valorDispesa, id_conta, estadoPagamento);
                    db.registarValor(dispesasModel);


                    DispesasModel dispesasModel1 = new DispesasModel(id_categoria, db.idOperacao(), descricao, data);
                    db.apenasRegistaDespesa(dispesasModel1);

                    startActivity(new Intent(this, DespesasActivity.class));
                }else {
                    Toast.makeText(this, "Saldo Insuficiente", Toast.LENGTH_LONG).show();
                }

            } else {
                estadoPagamento = 2;
                ContaModel dispesasModel = new ContaModel(valorDispesa, id_conta, estadoPagamento);
                db.registarValor(dispesasModel);

                DispesasModel dispesasModel1 = new DispesasModel(id_categoria, db.idOperacao(), descricao, data);
                db.apenasRegistaDespesa(dispesasModel1);

                Toast.makeText(this, "Dispesa Registada", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DespesasActivity.class));
            }
        }
    }
}
