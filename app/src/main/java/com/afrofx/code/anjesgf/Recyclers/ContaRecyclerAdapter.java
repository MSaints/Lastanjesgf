package com.afrofx.code.anjesgf.Recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.ContaModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


public class ContaRecyclerAdapter extends RecyclerView.Adapter<ContaRecyclerAdapter.ViewHolder> {


    private List<ContaModel> listaConta = new ArrayList<>();
    private Context contex;


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView conta_valor;
        private ImageView contaImg;

        public ViewHolder(View itemView) {
            super(itemView);
            conta_valor = (TextView)itemView.findViewById(R.id.conta_saldo);
            contaImg = (ImageView)itemView.findViewById(R.id.contaImg);
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

        String yourFormattedString = format("%,.2f",lista_Conta.getSaldoConta());

        if(lista_Conta.getNomeConta().equals("BCI")){
            holder.contaImg.setImageResource(R.drawable.ic_bci);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("M-PESA")){
            holder.contaImg.setImageResource(R.drawable.ic_mpesa);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("M-KESH")){
            holder.contaImg.setImageResource(R.drawable.ic_mkesh);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Moza Banco")){
            holder.contaImg.setImageResource(R.drawable.ic_moza);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Standard Bank")){
            holder.contaImg.setImageResource(R.drawable.ic_standard);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("MIllenium BIM")){
            holder.contaImg.setImageResource(R.drawable.ic_mbim);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("FNB")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Banco Unico")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Barclays")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("EMola")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Ecobank")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Socremo")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }if(lista_Conta.getNomeConta().equals("Capital Bank")){
            holder.contaImg.setImageResource(R.drawable.ic_fnb);
            holder.conta_valor.setText(yourFormattedString);
        }

    }





    @Override
    public int getItemCount() {
        if (listaConta == null) {
            return 0;
        }
        return listaConta.size();
    }
}
