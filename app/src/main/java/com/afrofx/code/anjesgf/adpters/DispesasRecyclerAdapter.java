package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import com.afrofx.code.anjesgf.Activities.DespesasActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.DispesasModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.format;

/**
 * Created by Afro FX on 3/1/2018.
 */

public class DispesasRecyclerAdapter extends RecyclerView.Adapter<DispesasRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<DispesasModel> dispesasModelList;
    private DatabaseHelper db;

    public DispesasRecyclerAdapter(Context context, List<DispesasModel> dispesasModelList) {
        this.context = context;
        this.dispesasModelList = dispesasModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dispesas, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DispesasModel listaDispesas = dispesasModelList.get(position);

        db = new DatabaseHelper(context);

        if (listaDispesas.getCategoria_dispesa().equals("CREDELEC")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_credelec);
        }
        if (listaDispesas.getCategoria_dispesa().equals("ALIMENTOS")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_alimentos);
        }
        if (listaDispesas.getCategoria_dispesa().equals("AGUA")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_agua);
        }
        if (listaDispesas.getCategoria_dispesa().equals("ESCOLA")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_escola);
        }
        if (listaDispesas.getCategoria_dispesa().equals("RENDA")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_casa);
        }
        if (listaDispesas.getCategoria_dispesa().equals("SAUDE")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_saude);
        }
        if (listaDispesas.getCategoria_dispesa().equals("TRANSPORTE")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_transporte);
        }
        if (listaDispesas.getCategoria_dispesa().equals("OUTRA")) {
            holder.icon_categoria_dispesa.setImageResource(R.drawable.ic_despesa_outras);
        }

        String yourFormattedString = format("%,.2f", listaDispesas.getCusto_dispesa());

        if (listaDispesas.getTipo_operacao() == 2) {
            holder.estado_dispesa.setImageResource(R.drawable.ic_despesa_pendente);
            holder.valor_dispesa.setText(yourFormattedString + " MT");
        }
        if (listaDispesas.getTipo_operacao() == 0) {
            holder.estado_dispesa.setImageResource(R.drawable.ic_despesa_paga);
            holder.valor_dispesa.setText("-" + yourFormattedString + " MT");
        }


        holder.conta_dispesa.setText(listaDispesas.getConta_dispesa());


        String startDateString = listaDispesas.getData_pagamento();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate;
        try {
            startDate = df.parse(startDateString);
            String newDateString = df.format(startDate);

            holder.data_dispesa.setText(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.descricao_dispesa.setText(listaDispesas.getDescricao_dispesa());

        holder.despesasCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.despesasCard);
                //inflating menu from xml resource
                popup.inflate(R.menu.despesas_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.DespesaMenu1:
                                if (listaDispesas.getTipo_operacao() == 2) {
                                    LayoutInflater li2 = LayoutInflater.from(context);
                                    View editView = li2.inflate(R.layout.prompt_paga_despesa, null);
                                    AlertDialog.Builder alert3 = new AlertDialog.Builder(context);
                                    alert3.setView(editView);

                                    final Spinner cont = (Spinner) editView.findViewById(R.id.spinnerContaDespesa2);
                                    final TextView Saldocont = (TextView) editView.findViewById(R.id.despesaSaldoConta);

                                    db = new DatabaseHelper(context);


                                    List<String> lables = db.listContas();
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lables);
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    cont.setAdapter(adapter2);

                                    final double[] saldo = {0};

                                    cont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String label = parent.getItemAtPosition(position).toString();
                                            saldo[0] = db.SaldoTotal(label);
                                            final String sla = format("%,.2f", saldo[0]);
                                            Saldocont.setText(sla + " MT");
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });


                                    // set dialog message
                                    alert3.setCancelable(false).setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            final int id_con = db.idConta(cont.getSelectedItem().toString());
                                            final int sso = listaDispesas.getId_registo_operacao();

                                            if (saldo[0] >= listaDispesas.getCusto_dispesa()) {
                                                if (!db.pagaRendi(id_con, 0, sso)) {
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
                                                            .setTitleText("Despesa Paga!")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    context.startActivity(new Intent(context, DespesasActivity.class));
                                                                    sDialog.dismiss();
                                                                    sDialog.setCancelable(false);
                                                                    notifyDataSetChanged();
                                                                }
                                                            })
                                                            .show();
                                                }
                                            } else {
                                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Saldo Insuficiente!")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
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
                                            .setTitleText("Ja foi Paga")
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
                            case R.id.DespesaMenu2:
                                if (listaDispesas.getTipo_operacao() == 2) {
                                    if (!db.deleteDespesa(listaDispesas.getId_dispesa(), listaDispesas.getId_registo_operacao())) {
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
                                                .setTitleText("Registo Eliminado!")
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
                                            .setTitleText("Ja Possui Registo")
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
        if (dispesasModelList == null)
            return 0;
        return dispesasModelList.size();
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
