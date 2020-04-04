package com.afrofx.code.anjesgf.Recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.NotificationModel;

import java.util.List;


public class RecyclerNotificacoes extends RecyclerView.Adapter<RecyclerNotificacoes.viewHolder> {

    private List<NotificationModel> notificationModelList;
    private Context context;

    public RecyclerNotificacoes(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }

    @Override
    public RecyclerNotificacoes.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_notificacoes, parent, false);
        RecyclerNotificacoes.viewHolder viewHolder = new RecyclerNotificacoes.viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final NotificationModel notificationModel = notificationModelList.get(position);

        holder.msgNotificacao.setText(notificationModel.getMsg_notificacao());
        holder.tipoNotificacao.setText(notificationModel.getTipo_notificacao());
        holder.dataNotificacao.setText(notificationModel.getData_notificacao());
        holder.horaNotificacao.setText(notificationModel.getHora_notificacao());
    }

    @Override
    public int getItemCount() {
        if (notificationModelList == null)
            return 0;
        return notificationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView msgNotificacao, tipoNotificacao, dataNotificacao, horaNotificacao;

        public viewHolder(View itemView) {
            super(itemView);

            msgNotificacao = (TextView) itemView.findViewById(R.id.msgNotificacao);
            tipoNotificacao = (TextView) itemView.findViewById(R.id.tipoNotificacao);
            dataNotificacao = (TextView) itemView.findViewById(R.id.dataNotificacao);
            horaNotificacao = (TextView) itemView.findViewById(R.id.horaNotificacao);
        }
    }
}
