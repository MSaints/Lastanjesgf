package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.DispesasModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.format;

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

        String categoria = "";
        if(listaDispesas.getCategoria_dispesa().equals("CREDELEC")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_credelec);
        }if(listaDispesas.getCategoria_dispesa().equals("ALIMENTOS")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_alimentos);
        }if(listaDispesas.getCategoria_dispesa().equals("AGUA")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_agua);
        }if(listaDispesas.getCategoria_dispesa().equals("ESCOLA")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_escola);
        }if(listaDispesas.getCategoria_dispesa().equals("RENDA")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_casa);
        }if(listaDispesas.getCategoria_dispesa().equals("SAUDE")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_saude);
        }if(listaDispesas.getCategoria_dispesa().equals("TRANSPORTE")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_transporte);
        }if(listaDispesas.getCategoria_dispesa().equals("OUTRA")){
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_outras);
        }

        String yourFormattedString = format("%,.2f",listaDispesas.getCusto_dispesa());

        if(listaDispesas.getTipo_operacao()==2){
            holder.estado_dispesa.setImageResource(R.drawable.ic_despesa_pendente);
            holder.valor_dispesa.setText(yourFormattedString+" MT");
        }if(listaDispesas.getTipo_operacao()==0){
            holder.estado_dispesa.setImageResource(R.drawable.ic_despesa_paga);
            holder.valor_dispesa.setText("-"+yourFormattedString+" MT");
        }


        holder.conta_dispesa.setText(listaDispesas.getConta_dispesa());
        holder.data_dispesa.setText(listaDispesas.getData_pagamento());
        holder.descricao_dispesa.setText(listaDispesas.getDescricao_dispesa());

        holder.valor_dispesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Mouzinho", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dispesasModelList == null)
            return 0;
        return dispesasModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView descricao_dispesa, valor_dispesa, conta_dispesa, data_dispesa;

        private ImageView icon_categoria_dispesa, estado_dispesa;

        public ViewHolder(View itemView) {
            super(itemView);
            descricao_dispesa = (TextView)itemView.findViewById(R.id.despesaDescricao);
            valor_dispesa = (TextView)itemView.findViewById(R.id.despesa_valor);
            conta_dispesa = (TextView)itemView.findViewById(R.id.dispesa_conta_usar);
            data_dispesa = (TextView)itemView.findViewById(R.id.despesa_data);
            icon_categoria_dispesa = (ImageView)itemView.findViewById(R.id.icon_dispesa_categoria);
            estado_dispesa = (ImageView)itemView.findViewById(R.id.icon_despesa_estado);
        }
    }
}
