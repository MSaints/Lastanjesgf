package com.afrofx.code.anjesgf.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.FornecedorRecyclerAdapter;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.models.FornecedorModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FornecedorFragment extends Fragment {


    View v;

    private FloatingActionButton add_fornecedor;

    private FornecedorFragment fornecedorFragment;

    private List<FornecedorModel> fornecedorList;

    DatabaseHelper db;



    public FornecedorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_fornecedor, container, false);

        add_fornecedor = (FloatingActionButton)v.findViewById(R.id.add_fornecedor_fr);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewFornecedor);
        recyclerView.setHasFixedSize(true);
        FornecedorRecyclerAdapter fornecedorRecyclerAdapter = new FornecedorRecyclerAdapter(getContext(), fornecedorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(fornecedorRecyclerAdapter);

        db = new DatabaseHelper(getActivity());

        add_fornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View forneView = li.inflate(R.layout.add_fornecedor, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(forneView);

                final EditText nomeFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_nome);
                final EditText emailFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_email);
                final EditText numeroFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_numero);
                final EditText tipoFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_tipo);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("Registar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nomef = nomeFornecedor.getText().toString();
                        String tipof = tipoFornecedor.getText().toString();
                        String emailf = emailFornecedor.getText().toString();
                        int numerof = Integer.parseInt(numeroFornecedor.getText().toString());

                        FornecedorModel fornecedorModel = new FornecedorModel(nomef, tipof, emailf, numerof);
                        db.registarFornecedor(fornecedorModel);
                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Fornecedor Registado")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        ViewPager viewPager;
                                        PageAdapters pageAdapters;
                                        viewPager = getActivity().findViewById(R.id.pageViewer);

                                        pageAdapters = new PageAdapters(getActivity().getSupportFragmentManager());

                                        pageAdapters.AddFragment(new ListaProdutosFragment(), "Produtos");
                                        pageAdapters.AddFragment(new AddProdutosFragment(), "Registar");
                                        pageAdapters.AddFragment(new FornecedorFragment(), "Fornecedor");

                                        viewPager.setAdapter(pageAdapters);

                                        viewPager.setCurrentItem(2);

                                        sDialog.dismiss();
                                        sDialog.setCancelable(false);
                                    }
                                })
                                .show();




                    }
                }).setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                // Criar O Alerta
                AlertDialog alertDialog = alertDialogBuilder.create();

                // Mostra o alerta
                alertDialog.show();


            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getActivity());
        fornecedorList = new ArrayList<>();

        Cursor cursor = db.listFornecedor();

        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                int id_forn = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_fornecedor")));
                int cont_forn = Integer.parseInt(cursor.getString(cursor.getColumnIndex("fornecedor_contacto")));
                String nome_forn = cursor.getString(cursor.getColumnIndex("fornecedor_nome"));
                String email_forn = cursor.getString(cursor.getColumnIndex("fornecedor_email"));
                String tipo_forn = cursor.getString(cursor.getColumnIndex("fornecedor_tipo"));

                FornecedorModel listItem = new FornecedorModel(id_forn, cont_forn, nome_forn, email_forn, tipo_forn);
                fornecedorList.add(listItem);

            }
        }
    }
}
