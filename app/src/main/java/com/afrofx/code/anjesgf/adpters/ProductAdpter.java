package com.afrofx.code.anjesgf.adpters;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afrofx.code.anjesgf.Activities.StockActivity;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;

/**
 * Created by Afro FX on 2/6/2018.
 */

public class ProductAdpter extends RecyclerView.Adapter<ProductAdpter.ViewHolder>{

    private List<StockModel> listItems;
    private Context context;

    public ProductAdpter(Context context,List<StockModel> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StockModel listItem = listItems.get(position);

        holder.txt_produto.setText(listItem.getProduto_nome());
        holder.txt_categoria.setText(listItem.getProduto_categoria());
        int p_quatidade = listItem.getProduto_quantidade();
        String p_quant = String.valueOf(p_quatidade);
        holder.txt_quantidade.setText(p_quant);
        double preco_venda = listItem.getProduto_preco_venda();
        String p_venda = String.valueOf(preco_venda);
        holder.txt_preco.setText(p_venda);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_produto, txt_categoria, txt_quantidade, txt_preco;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_produto = (TextView)itemView.findViewById(R.id.lista_titulo);
            txt_categoria = (TextView)itemView.findViewById(R.id.lista_descricao);
            txt_quantidade = (TextView)itemView.findViewById(R.id.list_item_quantidade);
            txt_preco = (TextView)itemView.findViewById(R.id.list_item_preco);
        }
    }

}
