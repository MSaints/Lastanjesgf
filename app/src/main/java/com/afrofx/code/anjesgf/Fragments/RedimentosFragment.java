package com.afrofx.code.anjesgf.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.adpters.RendimentosRecyclerAdapter;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RedimentosModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.String.format;

public class RedimentosFragment extends Fragment {

    private Spinner spinnerRedimentoMes;
    private TextView rendimentoRecebidos, rendimentoPendentes;
    private List<RedimentosModel> redimentosModels;
    private DatabaseHelper db;
    private FloatingActionButton addRendime;
    private RedimentosFragment redimentosFragment;
    private ViewPager viewPager;
    private PageAdapters pageAdapters;
    private TabLayout tab1;

    public RedimentosFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_redimentos, container, false);
        db = new DatabaseHelper(getContext());

        rendimentoPendentes = (TextView) v.findViewById(R.id.rendimentoPendente);
        rendimentoRecebidos = (TextView) v.findViewById(R.id.rendimentoTotal);

        addRendime = (FloatingActionButton) v.findViewById(R.id.rendimento_adicionar);

        double pendente = 0, pago = 0;

        ListaRendimentos();

        for (int i = 0; i < redimentosModels.size(); i++) {
            if (redimentosModels.get(i).getTipoOpe() == 2) {
                pendente = pendente + redimentosModels.get(i).getRendimento_valor();
            }
            if (redimentosModels.get(i).getTipoOpe() == 1) {
                pago = pago + redimentosModels.get(i).getRendimento_valor();
            }
        }
        String pen = format("%,.2f", pendente);
        String pag = format("%,.2f", pago);

        rendimentoPendentes.setText(pen + " MT");
        rendimentoRecebidos.setText(pag + " MT");

        addRendime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRendimento();
            }
        });

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewRendimentos);
        recyclerView.setHasFixedSize(true);
        RendimentosRecyclerAdapter rendimentosRecyclerAdapter = new RendimentosRecyclerAdapter(getContext(), redimentosModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(rendimentosRecyclerAdapter);

        return v;
    }


    public void addRendimento() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptRendimento = li.inflate(R.layout.prompt_add_rendimento, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptRendimento);
        db = new DatabaseHelper(getContext());

        List<String> lables = db.listContas();
        final Spinner spinnerRendimentoConta = (Spinner) promptRendimento.findViewById(R.id.spinnerContaRendimento);
        final EditText rendimentoData = (EditText) promptRendimento.findViewById(R.id.txtdataRendimento);
        final EditText add_valor = (EditText) promptRendimento.findViewById(R.id.valorRendimento);
        final EditText rendimentoDescricao = (EditText) promptRendimento.findViewById(R.id.txtDescricaoRendimento);
        final CheckBox estadoRendimento = (CheckBox) promptRendimento.findViewById(R.id.recebidoRendimento);

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

                String val = add_valor.getText().toString().trim();
                final double valorRend = !val.equals("") ? Double.parseDouble(val) : 0.0;
                String descri = rendimentoDescricao.getText().toString().trim();
                final int idConta = db.idConta(spinnerRendimentoConta.getSelectedItem().toString());
                if (estadoRendimento.isChecked()) {
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 1);
                    db.registarValor(contaModel);

                    RedimentosModel redimentosModel = new RedimentosModel(descri, data[0], db.idOperacao());
                    db.registoRendimento(redimentosModel);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Rendimento Recebido")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    tab1 = (TabLayout) getActivity().findViewById(R.id.tabLayout2);
                                    viewPager = (ViewPager) getActivity().findViewById(R.id.pageViewer2);
                                    pageAdapters = new PageAdapters(getFragmentManager());
                                    pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
                                    pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
                                    viewPager.setAdapter(pageAdapters);

                                    viewPager.setCurrentItem(1);
                                    sDialog.dismiss();
                                    sDialog.setCancelable(false);
                                }
                            })
                            .show();
                } else {
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 2);
                    db.registarValor(contaModel);
                    final RedimentosModel redimentosModel = new RedimentosModel(descri, data[0], db.idOperacao());
                    db.registoRendimento(redimentosModel);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Rendimento Adicionado")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    tab1 = (TabLayout) getActivity().findViewById(R.id.tabLayout2);
                                    viewPager = (ViewPager) getActivity().findViewById(R.id.pageViewer2);
                                    pageAdapters = new PageAdapters(getFragmentManager());
                                    pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
                                    pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
                                    viewPager.setAdapter(pageAdapters);

                                    viewPager.setCurrentItem(1);
                                    sDialog.dismiss();
                                    sDialog.setCancelable(false);
                                }
                            })
                            .show();
                }


            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }

    public void ListaRendimentos() {
        db = new DatabaseHelper(getContext());

        redimentosModels = new ArrayList<>();

        Cursor cursor = db.listaRendimentos();

        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                int id_rend = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_rendimento")));
                int id_op = Integer.parseInt(cursor.getString(cursor.getColumnIndex("tipo_operacao")));
                int id_reg_operacao = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_registo_operacao")));
                double valor = Double.parseDouble(cursor.getString(cursor.getColumnIndex("operacao_valor")));
                String descricao = (cursor.getString(cursor.getColumnIndex("rendimento_descricao")));
                String data = (cursor.getString(cursor.getColumnIndex("rendimento_data")));
                String nomeConta = cursor.getString(cursor.getColumnIndex("conta_nome"));

                RedimentosModel rendimentosModel = new RedimentosModel(descricao, data, nomeConta, id_rend, valor, id_op, id_reg_operacao);

                redimentosModels.add(rendimentosModel);
            }
        }
    }
}
