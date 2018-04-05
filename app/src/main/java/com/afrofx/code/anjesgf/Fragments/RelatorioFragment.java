package com.afrofx.code.anjesgf.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.ReportClientes1Activity;
import com.afrofx.code.anjesgf.Activities.ReportProdutosEmStockActivity;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ExpandableListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class RelatorioFragment extends Fragment {

    private View view;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private TextView dataInicio, dataFim;
    private LinearLayout butDataInicio, butDataFim;

    private String dataIni, dataF;

    private boolean click;

    private Calendar myCalendar = Calendar.getInstance();

    public RelatorioFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_relatorio, container, false);

        dataInicio = (TextView) view.findViewById(R.id.txtDataDe);
        dataFim = (TextView) view.findViewById(R.id.txtDataPara);
        butDataFim = (LinearLayout) view.findViewById(R.id.dataPara);
        butDataInicio = (LinearLayout) view.findViewById(R.id.dataInicio);


        listaDeDados();
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.expandGroup(0);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "Clientes Registados") {
                    startActivity(new Intent(getContext(), ReportClientes1Activity.class));
                }if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "Produtos em Stock"){
                    startActivity(new Intent(getContext(), ReportProdutosEmStockActivity.class));
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("dataInicio", dataIni);
                    editor.putString("dataFim", dataF);
                    editor.commit();
                }

                return false;
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (click) {
                    String myFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    dataInicio.setText(sdf.format(myCalendar.getTime()));
                    dataIni = sdf.format(myCalendar.getTime());
                }
                if (!click) {
                    String myFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    dataFim.setText(sdf.format(myCalendar.getTime()));
                    dataF = sdf.format(myCalendar.getTime());
                }
            }

        };

        butDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                click = true;
            }
        });

        butDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                click = false;
            }
        });

        return view;
    }


    private void listaDeDados() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        /*------------Cabecalhos nas listas-------------------*/
        listDataHeader.add("Relatorio de Vendas");
        listDataHeader.add("Relatorio de Stock");
        listDataHeader.add("Relatorios Financeiros");
        listDataHeader.add("Relatorio de Clientes");
        listDataHeader.add("Relatorio de Usuarios");

        /* ---------Adicionando sub itens nas listas------------*/
        List<String> relatorioVendas = new ArrayList<String>();
        relatorioVendas.add("Relatorio de Vendas");
        relatorioVendas.add("Total de Vendas");

        List<String> relatorioStock = new ArrayList<String>();
        relatorioStock.add("Produtos em Stock");
        relatorioStock.add("Produtos e Precos");

        List<String> relatorioFinanceiro = new ArrayList<String>();
        relatorioFinanceiro.add("Fluxo do Caixa");
        relatorioFinanceiro.add("Saldos das Contas");

        List<String> relatorioClientes = new ArrayList<String>();
        relatorioClientes.add("Clientes Registados");
        relatorioClientes.add("Clientes Com Dividas");
        relatorioClientes.add("Clientes Sem Dividas");

        List<String> relatorioUsuarios = new ArrayList<String>();
        relatorioUsuarios.add("Usuarios Registados");
        relatorioUsuarios.add("Usuario e Receita");

        /*-----------------Ordem das Listas-------------------------*/
        listDataChild.put(listDataHeader.get(0), relatorioVendas);
        listDataChild.put(listDataHeader.get(1), relatorioStock);
        listDataChild.put(listDataHeader.get(2), relatorioFinanceiro);
        listDataChild.put(listDataHeader.get(3), relatorioClientes);
        listDataChild.put(listDataHeader.get(4), relatorioUsuarios);
    }


}
