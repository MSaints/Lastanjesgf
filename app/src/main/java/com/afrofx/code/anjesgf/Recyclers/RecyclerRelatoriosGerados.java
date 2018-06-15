package com.afrofx.code.anjesgf.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.drawer.RelatoriosGeradosActivity;
import com.afrofx.code.anjesgf.Activities.drawer.VisualizadorPDFActivity;
import com.afrofx.code.anjesgf.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class RecyclerRelatoriosGerados extends RecyclerView.Adapter<RecyclerRelatoriosGerados.ViewHolder> {

    private Context context;
    private ArrayList<File> al_pdf;

    public RecyclerRelatoriosGerados(RelatoriosGeradosActivity context, ArrayList<File> al_pdf) {
        this.context = context;
        this.al_pdf = al_pdf;
    }

    @Override
    public RecyclerRelatoriosGerados.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ficheiros, parent, false);
        RecyclerRelatoriosGerados.ViewHolder viewHolder = new RecyclerRelatoriosGerados.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtNome.setText(al_pdf.get(position).getName());

        Locale localeBR = new Locale("pt", "BR");
        SimpleDateFormat fmt = new SimpleDateFormat("dd 'de' MMMM, HH:mm", localeBR);
        final Date lastModDate = new Date(al_pdf.get(position).lastModified());
        //DateFormat df = new SimpleDateFormat("dd/MM/yyyy ");
        String newDateString = fmt.format(lastModDate);
        holder.txtData.setText(newDateString);

        final String[] pdfFileName = new String[1];

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VisualizadorPDFActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        holder.visualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.visualizar);
                popup.inflate(R.menu.options_ficheiro_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ficheiroMenu1:
                                Toast.makeText(context, lastModDate+"", Toast.LENGTH_LONG).show();
                                break;

                            case R.id.ficheiroMenu2:

                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (al_pdf == null)
            return 0;
        return al_pdf.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNome, txtData;
        private ImageButton visualizar;
        private LinearLayout cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (LinearLayout) itemView.findViewById(R.id.cardFicheiro);
            txtNome = (TextView) itemView.findViewById(R.id.nome_ficheiro);
            txtData = (TextView) itemView.findViewById(R.id.data_criacao_ficheiro);
            visualizar = (ImageButton) itemView.findViewById(R.id.ficheiro_visualisar);
        }
    }

}
