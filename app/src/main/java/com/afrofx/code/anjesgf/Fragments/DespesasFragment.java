package com.afrofx.code.anjesgf.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.AdicionarDispesaActivity;
import com.afrofx.code.anjesgf.Activities.DespesasActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.DispesasRecyclerAdapter;
import com.afrofx.code.anjesgf.models.DispesasModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


public class DespesasFragment extends Fragment {

    private DatabaseHelper db;
    private List<DispesasModel> dispesasModelList;
    private TextView txtPendente, txtPaga;

    public DespesasFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_despesas, container, false);
        db = new DatabaseHelper(getContext());

        txtPendente = (TextView)v.findViewById(R.id.despesaPendente);
        txtPaga = (TextView)v.findViewById(R.id.despesaPaga);

        double pendente = 0, pago = 0;



        ListaDespesa();

        for(int i = 0; i<dispesasModelList.size();i++){
            if(dispesasModelList.get(i).getTipo_operacao()==2){
                pendente = pendente + dispesasModelList.get(i).getCusto_dispesa();
            }if(dispesasModelList.get(i).getTipo_operacao()==0){
                pago = pago + dispesasModelList.get(i).getCusto_dispesa();
            }
        }
        String pen = format("%,.2f",pendente);
        String pag = format("%,.2f",pago);

        txtPendente.setText(pen+" MT");
        txtPaga.setText(pag+" MT");

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recyclerViewDispesa);
        recyclerView.setHasFixedSize(true);
        DispesasRecyclerAdapter dispesasRecyclerAdapter = new DispesasRecyclerAdapter(getContext(), dispesasModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(dispesasRecyclerAdapter);


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.dispesa_adicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AdicionarDispesaActivity.class));
                getActivity().finish();
            }
        });

        return v;
    }

    public void ListaDespesa(){
        db = new DatabaseHelper(getContext());

        dispesasModelList = new ArrayList<>();

        Cursor cursor = db.listaDispesas();

        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Nenhum Registo Encontrado", Toast.LENGTH_SHORT);
        }else{
            while (cursor.moveToNext()){
                int id_despesa = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_despesa")));
                double valor = Double.parseDouble(cursor.getString(cursor.getColumnIndex("operacao_valor")));
                String descricao = (cursor.getString(cursor.getColumnIndex("despesa_descricao")));
                String data  = (cursor.getString(cursor.getColumnIndex("despesa_data")));
                String categoria  = (cursor.getString(cursor.getColumnIndex("despesa_categoria_nome")));
                int est = Integer.parseInt(cursor.getString(cursor.getColumnIndex("tipo_operacao")));
                String nomeConta = cursor.getString(cursor.getColumnIndex("conta_nome"));
                int id_op = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_registo_operacao")));

                DispesasModel dispesasModel = new DispesasModel(id_despesa, est, valor, descricao, categoria, data, nomeConta, id_op);

                dispesasModelList.add(dispesasModel);
            }
        }
    }

}
