package com.afrofx.code.anjesgf.Recyclers.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.List;

/**
 * Created by Afro FX on 2/27/2018.
 */

public class ReportClientesRegistadosRecycler extends RecyclerView.Adapter<ReportClientesRegistadosRecycler.viewHolder> {

    private List<ClienteModel> clienteModelList;
    private Context context;

    public ReportClientesRegistadosRecycler(Context context, List<ClienteModel> clienteModelList){
        this.context = context;
        this.clienteModelList = clienteModelList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_clientes_registados, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final ClienteModel clienteModel = clienteModelList.get(position);

        holder.clienteNome.setText(clienteModel.getNomeCliente());
        holder.clienteNumero.setText("+258 "+clienteModel.getNumeroCliente());
    }

    @Override
    public int getItemCount() {
        if(clienteModelList==null)
            return 0;
        return clienteModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView clienteNome, clienteNumero;

        public viewHolder(View itemView) {
            super(itemView);
            clienteNome = (TextView)itemView.findViewById(R.id.clienteNome);
            clienteNumero = (TextView)itemView.findViewById(R.id.clienteNumero);
        }
    }
}
