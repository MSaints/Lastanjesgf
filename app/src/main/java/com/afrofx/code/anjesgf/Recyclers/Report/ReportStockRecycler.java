package com.afrofx.code.anjesgf.Recyclers.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Afro FX on 2/27/2018.
 */

public class ReportStockRecycler extends RecyclerView.Adapter<ReportStockRecycler.viewHolder> {

    private List<StockModel> stockModelList;
    private Context context;
    public ReportStockRecycler(Context context, List<StockModel> stockModelList){
        this.context = context;
        this.stockModelList = stockModelList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorios_stock, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final StockModel stockModel = stockModelList.get(position);

        double valor = stockModel.getProduto_quantidade()*stockModel.getProduto_preco_venda();
        String nome = stockModel.getProduto_nome();


        String valor2 = format("%,.2f", valor);

        holder.valorStock.setText(valor2);
        holder.quantidadeStock.setText(stockModel.getProduto_quantidade()+"");
        holder.nomeProdutoStock.setText(nome);
    }

    @Override
    public int getItemCount() {
        if(stockModelList==null)
            return 0;
        return stockModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView nomeProdutoStock, quantidadeStock, valorStock;

        public viewHolder(View itemView) {
            super(itemView);

            nomeProdutoStock = (TextView)itemView.findViewById(R.id.nomeProdutoStock);
            quantidadeStock = (TextView)itemView.findViewById(R.id.quantidadeStock);
            valorStock = (TextView)itemView.findViewById(R.id.valorStock);
        }
    }
}
