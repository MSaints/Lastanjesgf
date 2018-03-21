package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;

import static java.lang.String.format;


public class RecyclerMercadoriaVendida extends RecyclerView.Adapter<RecyclerMercadoriaVendida.viewHolder> {

    private List<StockModel> stockModelList;
    private Context context;

    public RecyclerMercadoriaVendida(Context context, List<StockModel> stockModelList) {
        this.context = context;
        this.stockModelList = stockModelList;
    }

    @Override
    public RecyclerMercadoriaVendida.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.llista_mercadoria_vendida, parent, false);
        RecyclerMercadoriaVendida.viewHolder viewHolder = new RecyclerMercadoriaVendida.viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final StockModel stockModel = stockModelList.get(position);

        double valor = stockModel.getProduto_quantidade() * stockModel.getProduto_preco_venda();
        String nome = stockModel.getProduto_nome();


        String valor2 = format("%,.2f", valor);

        holder.valor.setText(valor2+ " MT");
        holder.quantidade.setText(stockModel.getProduto_quantidade()+ "");
        holder.nome.setText(nome);
    }

    @Override
    public int getItemCount() {
        if (stockModelList == null)
            return 0;
        return stockModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView nome, quantidade, valor;

        public viewHolder(View itemView) {
            super(itemView);

            nome = (TextView) itemView.findViewById(R.id.m_v_produto);
            quantidade = (TextView) itemView.findViewById(R.id.m_v_quatidade);
            valor = (TextView) itemView.findViewById(R.id.m_v_preco);
        }
    }
}
