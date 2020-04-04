package com.afrofx.code.anjesgf.Recyclers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.home.DespesasActivity;
import com.afrofx.code.anjesgf.Activities.home.RendimentosActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.models.RedimentosModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.String.format;

/**
 * Created by Afro FX on 3/13/2018.
 */

public class RendimentosRecyclerAdapter extends RecyclerView.Adapter<RendimentosRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<RedimentosModel> redimentosModelList;

    private DatabaseHelper db;

    public RendimentosRecyclerAdapter(Context context, List<RedimentosModel> redimentosModelList) {
        this.context = context;
        this.redimentosModelList = redimentosModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dispesas, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RedimentosModel listaRendimentos = redimentosModelList.get(position);

        String yourFormattedString = format("%,.2f", listaRendimentos.getRendimento_valor());

        if (listaRendimentos.getTipoOpe() == 2) {
            holder.estado_dispesa.setImageResource(R.drawable.ic_despesa_pendente);
            holder.valor_dispesa.setText(yourFormattedString + " MT");
        }
        if (listaRendimentos.getTipoOpe() == 1) {
            holder.estado_dispesa.setImageResource(R.drawable.ic_despesa_paga);
            holder.valor_dispesa.setText("+" + yourFormattedString + " MT");
        }
        db = new DatabaseHelper(context);

        holder.conta_dispesa.setText(listaRendimentos.getConta());
        holder.data_dispesa.setText(listaRendimentos.getRendimentoData());
        holder.descricao_dispesa.setText(listaRendimentos.getRedimentoDescricao());


        holder.despesasCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.despesasCard);
                //inflating menu from xml resource
                popup.inflate(R.menu.rendimentos_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.RendimentoMenu1:
                                if (listaRendimentos.getTipoOpe() == 2) {
                                    LayoutInflater li2 = LayoutInflater.from(context);
                                    View editView = li2.inflate(R.layout.prompt_paga_rendimento, null);
                                    AlertDialog.Builder alert3 = new AlertDialog.Builder(context);
                                    alert3.setView(editView);

                                    final String[] contaa = new String[0];
                                    final long[] id_con = new long[1];

                                    final Spinner cont = (Spinner) editView.findViewById(R.id.spinnerContaRendimento2);

                                    db = new DatabaseHelper(context);

                                    TextView view = (TextView) editView.findViewById(R.id.definitionInfoTitle);
                                    view.setBackgroundColor(Constant.color);


                                    List<String> lables = db.listContas();
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lables);
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    cont.setAdapter(adapter2);

                                    cont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String label = parent.getItemAtPosition(position).toString();
                                            id_con[0] = db.idConta(label);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                    final int sso = listaRendimentos.getId_registo_operacao();
                                    alert3.setCancelable(false).setPositiveButton("Receber", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            if (!db.pagaRendi(id_con[0], 1, sso)) {
                                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Erro Tecnico!")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                                sDialog.setCancelable(false);
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Recebido!")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                context.startActivity(new Intent(context, RendimentosActivity.class));
                                                                sDialog.dismiss();
                                                                sDialog.setCancelable(false);
                                                                notifyDataSetChanged();
                                                            }
                                                        })
                                                        .show();
                                            }

                                        }
                                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                                    // create alert dialog
                                    AlertDialog alertDialog3 = alert3.create();

                                    // show it
                                    alertDialog3.show();
                                } else {
                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Valor Recebido")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                    sDialog.setCancelable(false);
                                                }
                                            })
                                            .show();
                                }

                                break;
                            case R.id.RendimentoMenu2:
                                if (listaRendimentos.getTipoOpe() == 2) {
                                    if (!db.deleteDespesa(listaRendimentos.getId_rendimento(), listaRendimentos.getId_registo_operacao())) {
                                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Erro Tecnico!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                        sDialog.setCancelable(false);
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Rendimento Eliminado!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        context.startActivity(new Intent(context, DespesasActivity.class));
                                                        sDialog.dismiss();
                                                        sDialog.setCancelable(false);
                                                    }
                                                })
                                                .show();
                                    }
                                } else {
                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Ja Possui Registos")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                    sDialog.setCancelable(false);
                                                }
                                            })
                                            .show();
                                }

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
    public int getItemCount() {
        if (redimentosModelList == null)
            return 0;
        return redimentosModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView descricao_dispesa, valor_dispesa, conta_dispesa, data_dispesa;

        private ImageView icon_categoria_dispesa, estado_dispesa;

        private CardView despesasCard;

        public ViewHolder(View itemView) {
            super(itemView);
            despesasCard = (CardView) itemView.findViewById(R.id.despaesasCard);
            descricao_dispesa = (TextView) itemView.findViewById(R.id.despesaDescricao);
            valor_dispesa = (TextView) itemView.findViewById(R.id.despesa_valor);
            conta_dispesa = (TextView) itemView.findViewById(R.id.dispesa_conta_usar);
            data_dispesa = (TextView) itemView.findViewById(R.id.despesa_data);
            icon_categoria_dispesa = (ImageView) itemView.findViewById(R.id.icon_dispesa_categoria);
            estado_dispesa = (ImageView) itemView.findViewById(R.id.icon_despesa_estado);
        }
    }

}
