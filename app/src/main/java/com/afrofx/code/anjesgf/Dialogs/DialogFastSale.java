package com.afrofx.code.anjesgf.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.adpters.ProdutoAdapter;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RegistoVendaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.VendasModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogFastSale extends AlertDialog implements AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView nomeProduto;
    private EditText quantidadeProduto;
    private Spinner nomeConta;
    private TextView precoUn;
    private TextView precoTo;

    private DatabaseHelper db;
    private Context context;
    private List<StockModel> produStockModels;

    private double quantidade, preco, total;

    private String nomeProdutoStock, quantidadeProdutoStock, precoUnitarioStock;

    public DialogFastSale(Context context) {
        super(context);
        this.context = context;
    }

    public DialogFastSale(Context context, String n1, String q1, String p1) {
        super(context);
        this.context = context;
        this.nomeProdutoStock = n1;
        this.quantidadeProdutoStock = q1;
        this.precoUnitarioStock = p1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prompt_vender);

        nomeProduto = findViewById(R.id.MainVenderNome);
        quantidadeProduto = findViewById(R.id.MainVenderQuantidade);
        nomeConta = findViewById(R.id.MainVenderConta);
        precoUn = findViewById(R.id.MainPrecoUn);
        precoTo = findViewById(R.id.MainPrecoTo);
        TextView fastSaleTitle = findViewById(R.id.fastSaleTitle);
        View fastSaleLine1 = findViewById(R.id.fastSaleLine1);
        View fastSaleLine2 = findViewById(R.id.fastSaleLine2);
        Button concluir = findViewById(R.id.fastSaleConcluir);

        fastSaleTitle.setBackgroundColor(Constant.color);
        fastSaleLine1.setBackgroundColor(Constant.color);
        fastSaleLine2.setBackgroundColor(Constant.color);

        nomeConta.setOnItemSelectedListener(this);

        db = new DatabaseHelper(context);

        produStockModels = procuraProduto();
        nomeProduto.setThreshold(1);
        ProdutoAdapter produtoAdapter = new ProdutoAdapter(context, R.layout.prompt_vender, android.R.layout.simple_spinner_item, produStockModels);
        nomeProduto.setAdapter(produtoAdapter);

        List<String> listaContas = db.listContas();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaContas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        nomeConta.setAdapter(dataAdapter);

        nomeProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                quantidade = produStockModels.get(pos).getProduto_quantidade();
                quantidadeProduto.setHint("" + quantidade);
                preco = produStockModels.get(pos).getProduto_preco_venda();
                precoUn.setText(preco + "");
            }
        });


        quantidadeProduto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double quantiPro = !String.valueOf(s).equals("") ? Double.parseDouble(String.valueOf(s)) : 0.0;
                total = quantiPro * preco;
                precoTo.setText(total + "");
            }
        });

        if ((nomeProdutoStock != null) && quantidadeProdutoStock != null && precoUnitarioStock != null) {
            precoUn.setText(precoUnitarioStock);
            nomeProduto.setText(nomeProdutoStock);
            quantidadeProduto.setHint(quantidadeProdutoStock);
        }

        concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
                double quant = !quantidadeProduto.getText().toString().equals("") ?
                        Double.parseDouble(quantidadeProduto.getText().toString()) : 0.0;
                long id_user = (mSharedPreference.getLong("id_user", 0));
                long idConta = db.idConta(nomeConta.getSelectedItem().toString());

                if (quant == 0.0 || nomeProduto.equals("")) {
                    Toast.makeText(context, "Preencha Todos Campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (quantidade >= quant) {
                        ContaModel contaModel = new ContaModel(total, idConta, 1);
                        db.registarValor(contaModel);

                        int idRegistoConta = db.idOperacao();
                        RegistoVendaModel registoVendaModel = new RegistoVendaModel(id_user, idRegistoConta, 1);
                        db.registoVenda(registoVendaModel);


                        VendasModel vendasModel = new VendasModel(db.idProduto(nomeProduto.getText().toString()), db.idRegistoVenda(), quant, preco);
                        db.registoVendas(vendasModel);

                        if (db.updateQuatidade(nomeProduto.getText().toString(), quantidade - quant)) {
                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Produto Vendido")
                                    .show();
                            dismiss();
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Erro Tecnico")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Quantidade Inexistente")
                                .show();

                        dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private List<StockModel> procuraProduto() {
        List<StockModel> lista = new ArrayList<>();
        db = new DatabaseHelper(context);
        Cursor data = db.listProduto();
        if (data.getCount() == 0) {
            Toast.makeText(context, "Nao Produtos para vender", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                int id_produto = Integer.parseInt(data.getString(0));
                double quantide_produto = Double.parseDouble(data.getString(5));
                double preco_venda = Double.parseDouble(data.getString(8));
                String nome_produto = data.getString(4);
                StockModel listItem = new StockModel(id_produto, quantide_produto, preco_venda, nome_produto);
                lista.add(listItem);
            }
        }
        return lista;
    }
}
