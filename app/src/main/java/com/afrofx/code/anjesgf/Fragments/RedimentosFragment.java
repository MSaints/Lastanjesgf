package com.afrofx.code.anjesgf.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RedimentosModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RedimentosFragment extends Fragment {

    private Spinner spinnerRedimentoMes;
    private TextView rendimentoTota;
    private List<String> meses;
    private DatabaseHelper db;
    private FloatingActionButton addRendime;

    public RedimentosFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_redimentos, container, false);

        meses = new ArrayList<>();

        meses.add("Janeiro");
        meses.add("Fevereiro");
        meses.add("Marco");
        meses.add("Abril");
        meses.add("Maio");
        meses.add("Junho");
        meses.add("Julho");
        meses.add("Agosto");
        meses.add("Setembro");
        meses.add("Outubro");
        meses.add("Novembro");
        meses.add("Dezembro");

        addRendime = (FloatingActionButton)v.findViewById(R.id.rendimento_adicionar);
        spinnerRedimentoMes = (Spinner)v.findViewById(R.id.rendimentoMes) ;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, meses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRedimentoMes.setAdapter(adapter);

        addRendime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRendimento();;
            }
        });

        return v;
    }


    public void addRendimento(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptRendimento = li.inflate(R.layout.prompt_add_rendimento, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptRendimento);
        db = new DatabaseHelper(getContext());

        List<String> lables = db.listContas();
        final Spinner spinnerRendimentoConta = (Spinner)promptRendimento.findViewById(R.id.spinnerContaRendimento);
        final EditText rendimentoData = (EditText) promptRendimento.findViewById(R.id.txtdataRendimento);
        final EditText add_valor = (EditText) promptRendimento.findViewById(R.id.valorRendimento);
        final EditText rendimentoDescricao = (EditText) promptRendimento.findViewById(R.id.txtDescricaoRendimento);
        final CheckBox estadoRendimento = (CheckBox)promptRendimento.findViewById(R.id.recebidoRendimento);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lables);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRendimentoConta.setAdapter(adapter2);
        final Calendar myCalendar = Calendar.getInstance();
        final String[] data = {""};

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM-dd-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                rendimentoData.setText(sdf.format(myCalendar.getTime()));
                data[0] = sdf.format(myCalendar.getTime());
            }
        };

        int id_conta = 0;

        rendimentoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String  val = add_valor.getText().toString().trim();
                final double valorRend = !val.equals("") ? Double.parseDouble(val) : 0.0;
                String descri = rendimentoDescricao.getText().toString().trim();
                final int idConta = db.idConta(spinnerRendimentoConta.getSelectedItem().toString());
                if(estadoRendimento.isChecked()){
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 1);
                    db.registarValor(contaModel);

                    RedimentosModel redimentosModel = new RedimentosModel(descri, data[0], db.idOperacao());
                    db.registoRendimento(redimentosModel);
                }else{
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 2);
                    db.registarValor(contaModel);
                    RedimentosModel redimentosModel = new RedimentosModel(descri, data[0], db.idOperacao());
                    db.registoRendimento(redimentosModel);
                }
            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }
}
