package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.List;

/**
 * Created by Afro FX on 2/19/2018.
 */

public class ClienteRecyclerAdapter extends RecyclerView.Adapter<ClienteRecyclerAdapter.ViewHolder> {

    private List<ClienteModel> listaClients;
    private Context context;
    private DatabaseHelper db;

    public ClienteRecyclerAdapter(Context context, List<ClienteModel> listaClients){
        this.context = context;
        this.listaClients = listaClients;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ClienteModel listaClienteModel = listaClients.get(position);

        String Nome = listaClienteModel.getNomeCliente();
        holder.txt_nome.setText(Nome);

        String Letra = listaClienteModel.getNomeCliente().toUpperCase();
        holder.txt_letra.setText((Letra.toString().substring(0,1)));
        holder.txt_email.setText(listaClienteModel.getNumeroCliente()+"");

        db = new DatabaseHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if(listaClients==null)
            return 0;
        return listaClients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_nome, txt_email, txt_letra;
        public ImageView ligarFornecedor;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_nome = (TextView) itemView.findViewById(R.id.nomeCliente);
            txt_email = (TextView) itemView.findViewById(R.id.numeroCliente);
            txt_letra = (TextView) itemView.findViewById(R.id.letraCliente);
            ligarFornecedor = (ImageView) itemView.findViewById(R.id.ligarCliente);
        }
    }
}
