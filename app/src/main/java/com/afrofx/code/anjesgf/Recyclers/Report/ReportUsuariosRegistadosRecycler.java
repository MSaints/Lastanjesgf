package com.afrofx.code.anjesgf.Recyclers.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.*;

import java.util.List;

/**
 * Created by Afro FX on 2/27/2018.
 */

public class ReportUsuariosRegistadosRecycler extends RecyclerView.Adapter<ReportUsuariosRegistadosRecycler.viewHolder> {

    private List<UserModel> userModelList;
    private Context context;

    public ReportUsuariosRegistadosRecycler(Context context, List<UserModel> userModelList){
        this.context = context;
        this.userModelList = userModelList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_clientes_registados, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final UserModel userModel = userModelList.get(position);

        holder.clienteNome.setText(userModel.getUsuario_nome());
        holder.clienteNumero.setText("+258 "+userModel.getUsuario_numero());
    }

    @Override
    public int getItemCount() {
        if(userModelList==null)
            return 0;
        return userModelList.size();
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
