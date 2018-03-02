package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.DispesasModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Afro FX on 3/1/2018.
 */

public class DispesasRecyclerAdapter extends RecyclerView.Adapter<DispesasRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<DispesasModel> dispesasModelList;


    public DispesasRecyclerAdapter(Context context, List<DispesasModel> dispesasModelList){
        this.context = context;
        this.dispesasModelList = dispesasModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dispesas, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DispesasModel listaDispesas = dispesasModelList.get(position);

        holder.estado_dispesa.setBackgroundColor(324);
        holder.icon_categoria_dispesa.setImageDrawable(null);
        holder.conta_dispesa.setText(listaDispesas.getConta_dispesa());
        holder.data_dispesa.setText(listaDispesas.getData_pagamento());
        holder.valor_dispesa.setText(listaDispesas.getCusto_dispesa()+" MT");
        holder.descricao_dispesa.setText(listaDispesas.getDescricao_dispesa());
    }

    @Override
    public int getItemCount() {
        if (dispesasModelList == null)
            return 0;
        return dispesasModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView descricao_dispesa, valor_dispesa, conta_dispesa, data_dispesa;

        private CircleImageView icon_categoria_dispesa;

        private FloatingActionButton estado_dispesa;

        public ViewHolder(View itemView) {
            super(itemView);
            descricao_dispesa = (TextView)itemView.findViewById(R.id.despesaDescricao);
            valor_dispesa = (TextView)itemView.findViewById(R.id.despesa_valor);
            conta_dispesa = (TextView)itemView.findViewById(R.id.dispesa_conta_usar);
            data_dispesa = (TextView)itemView.findViewById(R.id.despesa_data);
            icon_categoria_dispesa = (CircleImageView)itemView.findViewById(R.id.icon_dispesa_categoria);
            estado_dispesa = (FloatingActionButton)itemView.findViewById(R.id.dispesa_estado);
        }
    }
}
