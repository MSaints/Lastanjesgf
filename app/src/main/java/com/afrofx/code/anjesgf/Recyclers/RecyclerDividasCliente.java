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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.vendas.DividasClienteActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.String.format;


public class RecyclerDividasCliente extends RecyclerView.Adapter<RecyclerDividasCliente.viewHolder> {

    private List<StockModel> stockModelList;
    private Context context;
    private DatabaseHelper db;

    public RecyclerDividasCliente(Context context, List<StockModel> stockModelList) {
        this.context = context;
        this.stockModelList = stockModelList;
    }

    @Override
    public RecyclerDividasCliente.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_divida_cliente, parent, false);
        RecyclerDividasCliente.viewHolder viewHolder = new RecyclerDividasCliente.viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
        final StockModel stockModel = stockModelList.get(position);

        double valor = stockModel.getProduto_quantidade() * stockModel.getProduto_preco_venda();
        String nome = stockModel.getProduto_nome();


        String valor2 = format("%,.2f", valor);

        holder.valor.setText(valor2+ " MT");
        holder.nome.setText(nome);

        holder.actionDivida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.actionDivida);
                //inflating menu from xml resource
                popup.inflate(R.menu.dividas_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dividaMenu1:
                                if (stockModel.getId_produto() == 0) {
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

                                    final long sso = stockModel.getId_registo_operacao();
                                    alert3.setCancelable(false).setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            if (!db.pagaDivida( 1, sso)) {
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
                                                        .setTitleText("Divida Paga!")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                context.startActivity(new Intent(context, DividasClienteActivity.class));
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
        if (stockModelList == null)
            return 0;
        return stockModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView nome, valor;
        private LinearLayout actionDivida;

        public viewHolder(View itemView) {
            super(itemView);

            nome = (TextView) itemView.findViewById(R.id.d_c_produto);
            valor = (TextView) itemView.findViewById(R.id.d_c_preco);
            actionDivida = (LinearLayout) itemView.findViewById(R.id.actionDivida);
        }
    }
}
