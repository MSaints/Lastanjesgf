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

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.List;

/**
 * Created by Afro FX on 2/19/2018.
 */

public class ClienteRecyclerAdapter extends RecyclerView.Adapter<ClienteRecyclerAdapter.ViewHolder> {

    private List<ClienteModel> listaClients;
    private Context context;

    public ClienteRecyclerAdapter(Context context, List<ClienteModel> listaClients){
        this.context = context;
        this.listaClients = listaClients;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ClienteModel listaClienteModel = listaClients.get(position);

        String Nome = listaClienteModel.getNomeCliente();
        holder.txt_nome.setText(Nome);
        holder.txt_email.setText(listaClienteModel.getNumeroCliente()+"");

        holder.ligarFornecedor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String numero = listaClienteModel.getNumeroCliente()+"";
                callIntent.setData(Uri.parse("tel:"+numero));
                context.startActivity(callIntent);
            }
        });

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

        private TextView txt_nome, txt_email;
        public ImageView ligarFornecedor;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_nome = (TextView) itemView.findViewById(R.id.nomeCliente);
            txt_email = (TextView) itemView.findViewById(R.id.numeroCliente);
            ligarFornecedor = (ImageView) itemView.findViewById(R.id.ligarCliente);
        }
    }
}
