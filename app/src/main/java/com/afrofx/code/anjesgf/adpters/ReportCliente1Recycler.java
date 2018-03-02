package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.List;

/**
 * Created by Afro FX on 2/27/2018.
 */

public class ReportCliente1Recycler extends RecyclerView.Adapter<ReportCliente1Recycler.viewHolder> {

    private List<ClienteModel> listClientes;
    private Context context;
    public ReportCliente1Recycler(Context context, List<ClienteModel> listClientes){
        this.context = context;
        this.listClientes = listClientes;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_cliente_1, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final ClienteModel clienteModel = listClientes.get(position);

        holder.nomeClienteR1.setText(clienteModel.getNomeCliente());
        holder.dataRegistoR1.setText(clienteModel.getDataRegistoCliente());
        holder.numeroClienteR1.setText(clienteModel.getNumeroCliente()+"");
    }

    @Override
    public int getItemCount() {
        if(listClientes==null)
            return 0;
        return listClientes.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView nomeClienteR1, dataRegistoR1, numeroClienteR1;

        public viewHolder(View itemView) {
            super(itemView);

            nomeClienteR1 = (TextView)itemView.findViewById(R.id.nomeClienteR1);
            dataRegistoR1 = (TextView)itemView.findViewById(R.id.dataRegistoR1);
            numeroClienteR1 = (TextView)itemView.findViewById(R.id.numeroClienteR1);
        }
    }
}
