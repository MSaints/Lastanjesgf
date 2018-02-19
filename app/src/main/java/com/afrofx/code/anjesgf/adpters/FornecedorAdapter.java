package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.FornecedorModel;

import java.util.List;

/**
 * Created by Afro FX on 2/19/2018.
 */

public class FornecedorAdapter extends RecyclerView.Adapter<FornecedorAdapter.ViewHolder> {

    private List<FornecedorModel> listaFornecedor;
    private Context context;
    private DatabaseHelper db;

    public FornecedorAdapter(Context context, List<FornecedorModel> listItems) {
        this.listaFornecedor = listItems;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FornecedorModel listaFornecedorModel = listaFornecedor.get(position);

        String Nome = listaFornecedorModel.getFornecedor_nome();
        holder.txt_nome.setText(Nome);

        String Letra = listaFornecedorModel.getFornecedor_nome();
        holder.txt_letra.setText((Letra.toString().substring(0,1)));
        holder.txt_tipo.setText(listaFornecedorModel.getFornecedor_tipo());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fornecedor_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if(listaFornecedor == null)
            return 0;
        return listaFornecedor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_nome, txt_tipo, txt_letra;
        public ImageButton ligarFornecedor;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_nome = (TextView) itemView.findViewById(R.id.nomeFornecedor);
            txt_tipo = (TextView) itemView.findViewById(R.id.tipoFornecedor);
            txt_letra = (TextView) itemView.findViewById(R.id.letraFornecedor);
            ligarFornecedor = (ImageButton) itemView.findViewById(R.id.ligarFornecedor);
        }
    }

}
