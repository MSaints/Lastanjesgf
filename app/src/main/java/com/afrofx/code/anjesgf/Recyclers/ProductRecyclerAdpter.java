package com.afrofx.code.anjesgf.Recyclers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Dialogs.DialogFastSale;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.List;


public class ProductRecyclerAdpter extends RecyclerView.Adapter<ProductRecyclerAdpter.ViewHolder> {

    private List<StockModel> listItems;
    private Context context;
    private DatabaseHelper db;
    private double saldo = 0;
    private long idConta = 0;
    private double preco;


    public ProductRecyclerAdpter(Context context, List<StockModel> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final StockModel listItem = listItems.get(position);

        holder.txt_produto.setText(listItem.getProduto_nome());
        holder.txt_categoria.setText(listItem.getCategoria());
        double p_quatidade = listItem.getProduto_quantidade();
        String p_quant = String.valueOf(p_quatidade);
        holder.txt_quantidade.setText(p_quant);
        double preco_venda = listItem.getProduto_preco_venda();
        String p_venda = String.valueOf(preco_venda) + " MZN";
        holder.txt_preco.setText(p_venda);

        db = new DatabaseHelper(context);

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                DialogFastSale dialogFastSale = new DialogFastSale(context, listItem.getProduto_nome(), listItem.getProduto_quantidade()+"".trim(),
                                        listItem.getProduto_preco_venda()+"".trim());
                                dialogFastSale.show();
                                break;
                            case R.id.menu2:
                                LayoutInflater li = LayoutInflater.from(context);
                                View quantiView = li.inflate(R.layout.prompt_stock, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                db = new DatabaseHelper(context);

                                alertDialogBuilder.setView(quantiView);



                                final EditText reporStockQuantidade = quantiView.findViewById(R.id.reporStockQuantidade);
                                final EditText reporStockPreco = quantiView.findViewById(R.id.reporStockPreco);
                                final Spinner nomeConta = quantiView.findViewById(R.id.reporVenderConta);
                                final TextView saldoDisponivel = quantiView.findViewById(R.id.reporValorDisponivel);
                                final TextView valorTotal = quantiView.findViewById(R.id.reporValorTotal);
                                final TextView fastSaleTitle = quantiView.findViewById(R.id.fastSaleTitle);
                                fastSaleTitle.setBackgroundColor(Constant.color);



                                final List<String> lables = db.listContas2();
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lables);
                                nomeConta.setAdapter(dataAdapter);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                nomeConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String label = parent.getItemAtPosition(position).toString();
                                        saldo = db.SaldoTotal(label);
                                        idConta = db.idConta(label);
                                        saldoDisponivel.setText(saldo + " MT");
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                reporStockPreco.addTextChangedListener(new TextWatcher() {
                                    public void afterTextChanged(Editable s) {
                                    }
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        preco = !String.valueOf(s).equals("") ? Double.parseDouble(String.valueOf(s)) : 0.0;
                                        valorTotal.setText(preco + "");
                                    }
                                });


                                // set dialog message
                                alertDialogBuilder.setCancelable(false).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final double quantidade = Double.parseDouble(reporStockQuantidade.getText().toString()) + listItem.getProduto_quantidade();
                                        final String nome_produto = listItem.getProduto_nome();
                                        preco = !reporStockPreco.getText().toString().equals("") ?
                                                Double.parseDouble(reporStockPreco.getText().toString()) : 0.0;

                                        if(saldo>=preco){
                                            if (quantidade!=0 && preco!=0) {
                                                ContaModel contaModel = new ContaModel(preco, idConta, 0);
                                                db.registarValor(contaModel);
                                                if(db.updateQuatidade(nome_produto, quantidade)) {
                                                    Toast.makeText(context, "Produto Actualizado", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(context, "Preencha os Campos", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(context, "Saldo Indisponivel", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                                // Criar O Alerta
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // Mostra o alerta
                                alertDialog.show();
                                break;

                            case R.id.menu4:
                                LayoutInflater l2 = LayoutInflater.from(context);
                                View v = l2.inflate(R.layout.prompt_stock_quebra, null);
                                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                                db = new DatabaseHelper(context);

                                alertDialogBuilder2.setView(v);

                                final long id_produto = listItem.getId_produto();
                                final double quantd = listItem.getProduto_quantidade();


                                final EditText quebraQuantidade = v.findViewById(R.id.quebraQuantidade);
                                final EditText quebraDescricao = v.findViewById(R.id.quebraDescricao);
                                final TextView fastSaleTitle2 = v.findViewById(R.id.fastSaleTitle2);
                                fastSaleTitle2.setBackgroundColor(Constant.color);


                                // set dialog message
                                alertDialogBuilder2.setCancelable(false).setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        final double quanQuebra = !quebraQuantidade.getText().toString().equals("") ?
                                                Double.parseDouble(quebraQuantidade.getText().toString()) : 0.0;

                                        final String desc = quebraDescricao.getText().toString().trim();


                                        boolean ins = db.inserirQuebra(id_produto, desc , quanQuebra, quanQuebra * listItem.getProduto_preco_venda() );

                                        //Toast.makeText(context, quantd-quanQuebra+" id:"+id_produto+"Nome:" +desc, Toast.LENGTH_SHORT).show();
                                        if(ins && quanQuebra>0 && db.updateQuatidade(listItem.getProduto_nome(), quantd-quanQuebra)){
                                            Toast.makeText(context, "Quebra Adicionada", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context, "Erro Ao Adicionar", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                // Criar O Alerta
                                AlertDialog alertDialog1 = alertDialogBuilder2.create();

                                // Mostra o alerta
                                alertDialog1.show();
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_produto, txt_categoria, txt_quantidade, txt_preco;
        public ImageButton buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_produto = (TextView) itemView.findViewById(R.id.lista_titulo);
            txt_categoria = (TextView) itemView.findViewById(R.id.list_item_dec);
            txt_quantidade = (TextView) itemView.findViewById(R.id.list_item_quantidade);
            txt_preco = (TextView) itemView.findViewById(R.id.list_item_preco);
            buttonViewOption = (ImageButton) itemView.findViewById(R.id.butViewOptions);
        }
    }


    public void aumentarStock(){

    }


    public void vender(){

    }

}
