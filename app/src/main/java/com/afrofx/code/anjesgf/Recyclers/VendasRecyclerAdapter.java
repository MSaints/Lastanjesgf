package com.afrofx.code.anjesgf.Recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.VendasModel;

import java.util.ArrayList;

/**
 * Created by Afro FX on 2/22/2018.
 */

public class VendasRecyclerAdapter extends RecyclerView.Adapter<VendasRecyclerAdapter.ViewHolder> {

    private ArrayList<VendasModel> vendasModelList;
    private Context context;
    private DatabaseHelper db;

    public VendasRecyclerAdapter(Context context, ArrayList<VendasModel> vendasModelList) {
        this.context = context;
        this.vendasModelList = vendasModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_compra_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VendasRecyclerAdapter.ViewHolder holder, int position) {
        final VendasModel listaVendasModel = vendasModelList.get(position);
        String nome = listaVendasModel.getNomeProduto();
        double quantidade = listaVendasModel.getVenda_quantidade();
        double preco = listaVendasModel.getVenda_preco();

        holder.nome_produto.setText(nome);
        holder.quantidade_produto.setText(quantidade+"");
        holder.preco_produto.setText(preco+"");
    }

    @Override
    public int getItemCount() {
        if(vendasModelList == null)
            return 0;
        return vendasModelList.size();
    }

    public void notifyData(ArrayList<VendasModel> myList) {
        this.vendasModelList = myList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nome_produto, quantidade_produto, preco_produto;

        public ViewHolder(View itemView) {
            super(itemView);
            nome_produto = (TextView)itemView.findViewById(R.id.compraProduto);
            quantidade_produto = (TextView)itemView.findViewById(R.id.compraQuantidade);
            preco_produto = (TextView)itemView.findViewById(R.id.compraPreco);
        }
    }
}
