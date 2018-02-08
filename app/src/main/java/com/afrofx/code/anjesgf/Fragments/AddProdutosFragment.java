package com.afrofx.code.anjesgf.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.models.StockModel;

public class AddProdutosFragment extends Fragment {

    View v;

    private EditText edit_Nome, edit_quantidade, edit_preco_compra, edit_preco_venda;
    private Button but_reg_produto;
    private DatabaseHelper db;

    public AddProdutosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_produtos, container, false);

        db = new DatabaseHelper(getActivity());

        edit_Nome = (EditText) v.findViewById(R.id.add_nome);
        edit_quantidade = (EditText) v.findViewById(R.id.add_quantidade);
        edit_preco_compra = (EditText) v.findViewById(R.id.add_preco_compra);
        edit_preco_venda = (EditText) v.findViewById(R.id.add_preco_venda);
        but_reg_produto = (Button) v.findViewById(R.id.add_produto);

        but_reg_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduto();
            }
        });

        return v;
    }

    public void addProduto() {
        String produto_nome = edit_Nome.getText().toString();
        String pro_quant = edit_quantidade.getText().toString();
        String pro_comp = edit_preco_compra.getText().toString();
        String pro_venda = edit_preco_venda.getText().toString();

        final int produto_quantidade = !pro_quant.equals("") ? Integer.parseInt(pro_quant) : 0;
        final double produto_preco_compra = !pro_comp.equals("") ? Double.parseDouble(pro_comp) : 0.0;
        final double produto_preco_venda = !pro_venda.equals("") ? Double.parseDouble(pro_venda) : 0.0;

        StockModel stockModel = db.procuraProduto(produto_nome);

        if(produto_nome ==""|| pro_comp.length()<1|| pro_venda.length()<1){
            mensagem("Preencha os Campos");
        }else{
            if(stockModel==null) {
                stockModel = new StockModel(produto_quantidade, produto_preco_compra, produto_preco_venda, produto_nome);
                db.inserirProduto(stockModel);
                mensagem("Produto Adicionado");
            }else{
                mensagem("produto ja foi registado");
            }
        }
    }
    private void mensagem(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }
}
