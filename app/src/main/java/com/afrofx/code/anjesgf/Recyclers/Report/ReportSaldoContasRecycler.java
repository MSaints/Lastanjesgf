package com.afrofx.code.anjesgf.Recyclers.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ContaModel;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Afro FX on 2/27/2018.
 */

public class ReportSaldoContasRecycler extends RecyclerView.Adapter<ReportSaldoContasRecycler.viewHolder> {

    private List<ContaModel> contaModelList;
    private Context context;

    public ReportSaldoContasRecycler(Context context, List<ContaModel> contaModelList){
        this.context = context;
        this.contaModelList = contaModelList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_saldo_contas, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final ContaModel contaModel = contaModelList.get(position);
        double valor = contaModel.getSaldoConta();
        String valor2 = format("%,.2f", valor);

        holder.designacaoConta.setText(contaModel.getNomeConta());
        holder.saldoDisponivel.setText(valor2+" MT");
    }

    @Override
    public int getItemCount() {
        if(contaModelList==null)
            return 0;
        return contaModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView designacaoConta, saldoDisponivel;

        public viewHolder(View itemView) {
            super(itemView);
            designacaoConta = (TextView)itemView.findViewById(R.id.nomeConta);
            saldoDisponivel = (TextView)itemView.findViewById(R.id.saldoDisponivel);
        }
    }
}
