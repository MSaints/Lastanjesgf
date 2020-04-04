package com.afrofx.code.anjesgf.Recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.QuebrasModel;

import java.util.List;


public class RecyclerQuebras extends RecyclerView.Adapter<RecyclerQuebras.viewHolder> {

    private List<QuebrasModel> quebrasModelList;
    private Context context;

    public RecyclerQuebras(Context context, List<QuebrasModel> quebrasModelList) {
        this.context = context;
        this.quebrasModelList = quebrasModelList;
    }

    @Override
    public RecyclerQuebras.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_quebras, parent, false);
        RecyclerQuebras.viewHolder viewHolder = new RecyclerQuebras.viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final QuebrasModel quebrasModel = quebrasModelList.get(position);

        holder.quebraDescricao.setText(quebrasModel.getQuebraDescricao());
        holder.quebra_valor.setText(quebrasModel.getQuebraPreco()+"");
        holder.quebraProduto.setText(quebrasModel.getQuebraNomeProduto());
        holder.quebra_data.setText(quebrasModel.getQuebraDataRegisto());
    }

    @Override
    public int getItemCount() {
        if (quebrasModelList == null)
            return 0;
        return quebrasModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView quebraDescricao, quebraProduto, quebra_valor, quebra_data;

        public viewHolder(View itemView) {
            super(itemView);

            quebraDescricao = (TextView) itemView.findViewById(R.id.quebraDescricao);
            quebra_valor = (TextView) itemView.findViewById(R.id.quebra_valor);
            quebraProduto = (TextView) itemView.findViewById(R.id.quebraProduto);
            quebra_data = (TextView) itemView.findViewById(R.id.quebra_data);
        }
    }
}
