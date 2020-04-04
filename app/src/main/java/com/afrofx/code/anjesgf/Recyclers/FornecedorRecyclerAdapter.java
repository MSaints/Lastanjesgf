package com.afrofx.code.anjesgf.Recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.FornecedorModel;

import java.util.List;

/**
 * Created by Afro FX on 2/19/2018.
 */

public class FornecedorRecyclerAdapter extends RecyclerView.Adapter<FornecedorRecyclerAdapter.ViewHolder> {

    private List<FornecedorModel> listaFornecedor;
    private Context context;
    private DatabaseHelper db;

    public FornecedorRecyclerAdapter(Context context, List<FornecedorModel> listItems) {
        this.listaFornecedor = listItems;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FornecedorModel listaFornecedorModel = listaFornecedor.get(position);

        String Nome = listaFornecedorModel.getFornecedor_nome();
        holder.txt_nome.setText(Nome);

        String Letra = listaFornecedorModel.getFornecedor_nome().toUpperCase();
        holder.txt_letra.setText((Letra.toString().substring(0,1)));
        holder.txt_tipo.setText(listaFornecedorModel.getFornecedor_tipo());

        db = new DatabaseHelper(context);



        holder.ligarFornecedor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String numero = listaFornecedorModel.getFornecedor_contacto()+"";
                callIntent.setData(Uri.parse("tel:"+numero));
                context.startActivity(callIntent);
            }
        });
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
        public ImageView ligarFornecedor;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_nome = (TextView) itemView.findViewById(R.id.nomeFornecedor);
            txt_tipo = (TextView) itemView.findViewById(R.id.tipoFornecedor);
            txt_letra = (TextView) itemView.findViewById(R.id.letraFornecedor);
            ligarFornecedor = (ImageView) itemView.findViewById(R.id.ligarFornecedor);
        }
    }

}
