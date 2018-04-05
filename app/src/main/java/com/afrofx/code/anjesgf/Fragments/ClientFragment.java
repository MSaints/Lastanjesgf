package com.afrofx.code.anjesgf.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.afrofx.code.anjesgf.adpters.ClienteRecyclerAdapter;
import com.afrofx.code.anjesgf.models.ClienteModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ClientFragment extends Fragment {

    private View v;

    private List<ClienteModel> listaClients;

    private FloatingActionButton butAddCliente;

    private DatabaseHelper db;

    private ClientFragment clientFragment;


    public ClientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getActivity());

        listaClients = new ArrayList<>();

        Cursor cursor = db.listCliente();

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Nao Existem Clientes", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                int id_forn = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_cliente")));
                int cont_forn = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cliente_cell")));
                String nome_forn = cursor.getString(cursor.getColumnIndex("cliente_nome"));
                String email_forn = cursor.getString(cursor.getColumnIndex("cliente_email"));
                String data_registo = cursor.getString(cursor.getColumnIndex("cliente_data_registo"));

                ClienteModel listItem = new ClienteModel(nome_forn, email_forn, data_registo, id_forn, cont_forn);
                listaClients.add(listItem);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_client, container, false);

        butAddCliente = (FloatingActionButton) v.findViewById(R.id.add_clienteFragment);
        db = new DatabaseHelper(getActivity());


        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewClients);
        recyclerView.setHasFixedSize(true);
        final ClienteRecyclerAdapter clienteRecyclerAdapter = new ClienteRecyclerAdapter(getContext(), listaClients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(clienteRecyclerAdapter);

        butAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View forneView = li.inflate(R.layout.add_cliente, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder.setView(forneView);

                final EditText nomeCliente = (EditText) forneView.findViewById(R.id.add_cliente_nome);
                final EditText numeroCliente = (EditText) forneView.findViewById(R.id.add_cliente_numero);

                alertDialogBuilder.setCancelable(false).setPositiveButton("Registar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nomef = nomeCliente.getText().toString();
                        int numerof = Integer.parseInt(numeroCliente.getText().toString());

                        ClienteModel clienteModel = new ClienteModel(nomef, numerof);
                        db.registarCliente(clienteModel);
                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Cliente Registado")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        clientFragment = new ClientFragment();
                                        setFragment(clientFragment);
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }).setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        return v;
    }

    private void setFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void mensagem(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
