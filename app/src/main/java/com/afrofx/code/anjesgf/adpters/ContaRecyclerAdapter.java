package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ContaModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


public class ContaRecyclerAdapter extends RecyclerView.Adapter<ContaRecyclerAdapter.ViewHolder> {


    private List<ContaModel> listaConta = new ArrayList<>();
    private Context contex;


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView conta_valor, conta_nome, title;
        private ImageView historico;

        public ViewHolder(View itemView) {
            super(itemView);
//            title = (TextView) itemView.findViewById(R.id.title1);
            conta_nome = (TextView)itemView.findViewById(R.id.conta_nome);
            conta_valor = (TextView)itemView.findViewById(R.id.conta_saldo);
            historico = (ImageView)itemView.findViewById(R.id.historico);
        }
    }

    public ContaRecyclerAdapter(Context context, List<ContaModel> listaConta){
        this.listaConta = listaConta;
        this.contex = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conta_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContaRecyclerAdapter.ViewHolder holder, int position) {
        final ContaModel lista_Conta = listaConta.get(position);
//        holder.title.setText(lista_Conta.getNomeConta().substring(0,1));

        String yourFormattedString = format("%,.2f",lista_Conta.getSaldoConta());
        holder.conta_nome.setText(lista_Conta.getNomeConta());
        holder.conta_valor.setText(yourFormattedString);
    }





    @Override
    public int getItemCount() {
        if(listaConta == null)
            return 0;
        return listaConta.size();
    }
}
