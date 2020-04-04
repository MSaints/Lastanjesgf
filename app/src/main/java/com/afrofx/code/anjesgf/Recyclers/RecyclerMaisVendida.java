package com.afrofx.code.anjesgf.Recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;


public class RecyclerMaisVendida extends RecyclerView.Adapter<RecyclerMaisVendida.viewHolder> {

    private List<StockModel> stockModelList;
    private Context context;

    public RecyclerMaisVendida(Context context, List<StockModel> stockModelList) {
        this.context = context;
        this.stockModelList = stockModelList;
    }

    @Override
    public RecyclerMaisVendida.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_mais_vendida, parent, false);
        RecyclerMaisVendida.viewHolder viewHolder = new RecyclerMaisVendida.viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final StockModel stockModel = stockModelList.get(position);

        holder.quantidade.setText(stockModel.getProduto_quantidade()+ "");
        holder.nome.setText(stockModel.getProduto_nome());
    }

    @Override
    public int getItemCount() {
        if (stockModelList == null)
            return 0;
        return stockModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView nome, quantidade;

        public viewHolder(View itemView) {
            super(itemView);

            nome = (TextView) itemView.findViewById(R.id.mais_v_produto);
            quantidade = (TextView) itemView.findViewById(R.id.mais_v_quatidade);
        }
    }
}
