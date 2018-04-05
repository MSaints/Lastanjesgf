package com.afrofx.code.anjesgf.adpters;

/*import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.FicheirosModel;

import org.w3c.dom.Text;

import java.util.List;*/


public class RecyclerRelatoriosGerados {

   /* private Context context;
    private List<FicheirosModel> ficheirosModels;


    public RecyclerRelatoriosGerados(Context context, List<FicheirosModel> ficheirosModels) {
        this.context = context;
        this.ficheirosModels = ficheirosModels;
    }


    @Override
    public RecyclerRelatoriosGerados.viewHolder onCreateViewHolder(ViewGroup viewGroup, int position){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_ficheiros, viewGroup, false);
        RecyclerRelatoriosGerados.viewHolder viewHolder = new RecyclerRelatoriosGerados.viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position){
        final FicheirosModel ficheirosModel = ficheirosModels.get(position);

        holder.txtNome.setText(ficheirosModel.getNome_ficheiro());
        holder.txtData.setText(ficheirosModel.getData_criacao());

    }

    @Override
    public int getItemCount() {
        if (ficheirosModels == null)
            return 0;
        return ficheirosModels.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView txtNome, txtData;
        private ImageView visualizar;

        public viewHolder(View itemView) {
            super(itemView);
            txtNome = (TextView)itemView.findViewById(R.id.nome_ficheiro);
            txtData = (TextView)itemView.findViewById(R.id.data_criacao_ficheiro) ;

        }
    }*/
}
