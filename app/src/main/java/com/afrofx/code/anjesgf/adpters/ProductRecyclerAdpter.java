package com.afrofx.code.anjesgf.adpters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

import java.util.ArrayList;
import java.util.List;


public class ProductRecyclerAdpter extends RecyclerView.Adapter<ProductRecyclerAdpter.ViewHolder> {

    private List<StockModel> listItems;
    private Context context;
    private DatabaseHelper db;

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
                            case R.id.menu2:
                                LayoutInflater li = LayoutInflater.from(context);
                                View quantiView = li.inflate(R.layout.prompt_stock, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                // set prompts.xml to alertdialog builder
                                alertDialogBuilder.setView(quantiView);

                                final EditText userInput = (EditText) quantiView.findViewById(R.id.editTextDialogUserInput);

                                // set dialog message
                                alertDialogBuilder.setCancelable(false).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        double quantidade = Double.parseDouble(userInput.getText().toString()) + listItem.getProduto_quantidade();
                                        String nome_produto = listItem.getProduto_nome();
                                        if (db.updateQuatidade(nome_produto, quantidade)) {
                                            Toast.makeText(context, "Produto Actualizado", Toast.LENGTH_SHORT).show();
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
                                String nome = listItem.getProduto_nome();
                                double quant = listItem.getProduto_quantidade();

                                if (quant == 0) {
                                    if (db.eliminarProduto(nome)) {
                                        Toast.makeText(context, "O produto foi eliminado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "O produto ja foi eliminado", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "O produto ainda nao Acabou", Toast.LENGTH_SHORT).show();
                                }

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

}
