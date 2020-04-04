package com.afrofx.code.anjesgf.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.afrofx.code.anjesgf.Activities.relatorios.ReportClientesRegistadosActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportDiarioActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportProdutosEmStockActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportProdutosForaStockActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportSaldoContasActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportUsuarioReceitaActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportUsuariosRegistadosActivity;
import com.afrofx.code.anjesgf.Activities.relatorios.ReportVendasActivity;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ExpandableListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


public class RelatorioFragment extends Fragment {

    private View view;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    private String text1, text2;
    private TextView data1, data2;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd'/'MM'/'yyyy");
    String dataInicio, dataFim;

    public RelatorioFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_relatorio, container, false);

        CardView CardDataInicio = view.findViewById(R.id.dataInicio);
        CardView CardDataFim = view.findViewById(R.id.dataFim);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = mSharedPreference.edit();

        data1 = view.findViewById(R.id.data1);
        data2 = view.findViewById(R.id.data2);

        text1 = simpleDateFormat.format(calendar.getTime());
        text2 = simpleDateFormat.format(calendar.getTime());

        dataInicio = text1;
        dataFim = text2;

        data1.setText(text1);
        data2.setText(text2);

        listaDeDados();
        expListView = view.findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.expandGroup(0);
        expListView.expandGroup(1);
        expListView.expandGroup(3);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                editor.putString("dataInicio", dataInicio);
                editor.putString("dataFim", dataFim);
                editor.commit();

                 if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Total de Vendas")) {
                    startActivity(new Intent(getContext(), ReportVendasActivity.class));
                }
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Relatório Diário")) {
                    startActivity(new Intent(getContext(), ReportDiarioActivity.class));
                }



                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Produtos em Stock")) {
                    startActivity(new Intent(getContext(), ReportProdutosEmStockActivity.class));
                }
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Produtos fora de Stock")) {
                    startActivity(new Intent(getContext(), ReportProdutosForaStockActivity.class));
                }



                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Saldos das Contas")) {
                    startActivity(new Intent(getContext(), ReportSaldoContasActivity.class));
                }

                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Clientes Registados")) {
                    startActivity(new Intent(getContext(), ReportClientesRegistadosActivity.class));
                }

//=================Report Users
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Usuários Registados")) {
                    startActivity(new Intent(getContext(), ReportUsuariosRegistadosActivity.class));
                }
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Usuário e Receita")) {
                    startActivity(new Intent(getContext(), ReportUsuarioReceitaActivity.class));
                }

                return false;
            }
        });

        CardDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataInicio();
            }
        });

        CardDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataFim();
            }
        });

        return view;
    }

    public void setDataInicio() {
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendario = new GregorianCalendar(year, month, day);
                dataInicio = simpleDate.format(calendario.getTime());
                data1.setText(simpleDateFormat.format(calendario.getTime()));
                setDataFim();
            }
        }, ano, mes, dia);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void setDataFim() {
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendario = new GregorianCalendar(year, month, day);
                dataFim = simpleDate.format(calendario.getTime());
                data2.setText(simpleDateFormat.format(calendario.getTime()));
            }
        }, ano, mes, dia);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        dialog.show();


    }

    private void listaDeDados() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();
        /*------------Cabecalhos nas listas-------------------*/
        listDataHeader.add("Relatórios de Vendas");
        listDataHeader.add("Relatórios de Estoque");
        listDataHeader.add("Relatórios Financeiros");
        listDataHeader.add("Relatório de Clientes");
        listDataHeader.add("Relatório de Usuarios");

        /* ---------Adicionando sub itens nas listas------------*/
        List<String> relatorioVendas = new ArrayList<>();
        //relatorioVendas.add("Relatório Diário");
        relatorioVendas.add("Total de Vendas");

        List<String> relatorioStock = new ArrayList<>();
        relatorioStock.add("Produtos em Stock");
        relatorioStock.add("Produtos fora de Stock");

        List<String> relatorioFinanceiro = new ArrayList<>();
        relatorioFinanceiro.add("Saldos das Contas");

        List<String> relatorioClientes = new ArrayList<>();
        relatorioClientes.add("Clientes Registados");
        relatorioClientes.add("Clientes & Dividas");

        List<String> relatorioUsuarios = new ArrayList<>();
        relatorioUsuarios.add("Usuários Registados");
        relatorioUsuarios.add("Usuário e Receita");

        /*-----------------Ordem das Listas-------------------------*/
        listDataChild.put(listDataHeader.get(0), relatorioVendas);
        listDataChild.put(listDataHeader.get(1), relatorioStock);
        listDataChild.put(listDataHeader.get(2), relatorioFinanceiro);
        listDataChild.put(listDataHeader.get(3), relatorioClientes);
        listDataChild.put(listDataHeader.get(4), relatorioUsuarios);
    }
}
