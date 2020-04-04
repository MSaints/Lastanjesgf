package com.afrofx.code.anjesgf.Activities.mercadoria;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.home.MercadoriaActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.FornecedorRecyclerAdapter;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.FornecedorModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FornecedoresActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    private Toolbar toolbar;

    private FloatingActionButton add_fornecedor;

    private List<FornecedorModel> fornecedorList;
    private FornecedorRecyclerAdapter fornecedorRecyclerAdapter;
    private RecyclerView recyclerView;

    private final int REQUEST_CODE_ASK_PERMISSIONS = 999;

    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_forncedores);

        toolbar = findViewById(R.id.toolbarFornecedores);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaFornecedores();

        add_fornecedor = findViewById(R.id.add_fornecedor_fr);

        recyclerView = findViewById(R.id.recycleViewFornecedor);
        recyclerView.setHasFixedSize(true);
        fornecedorRecyclerAdapter = new FornecedorRecyclerAdapter(this, fornecedorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(fornecedorRecyclerAdapter);

        add_fornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFornecedor();
            }
        });


        requestPermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Procurar...");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(FornecedoresActivity.this, MercadoriaActivity.class));
            finish();
            return true;
        }
        return false;
    }

    public void listaFornecedores(){
        db = new DatabaseHelper(this);
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


    public void addFornecedor() {
        LayoutInflater li = LayoutInflater.from(this);
        View forneView = li.inflate(R.layout.add_fornecedor, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(forneView);

        final EditText nomeFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_nome);
        final EditText numeroFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_numero);
        final EditText tipoFornecedor = (EditText) forneView.findViewById(R.id.add_fornecedor_tipo);
        TextView fastSaleTitle = forneView.findViewById(R.id.fastSaleTitle);

        fastSaleTitle.setBackgroundColor(Constant.color);
        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Registar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nomef = nomeFornecedor.getText().toString();
                String tipof = tipoFornecedor.getText().toString();
                int numerof = Integer.parseInt(numeroFornecedor.getText().toString());

                FornecedorModel fornecedorModel = new FornecedorModel(nomef, tipof, numerof);
                db.registarFornecedor(fornecedorModel);

                new SweetAlertDialog(FornecedoresActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Fornecedor Registado")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                sDialog.setCancelable(false);
                                startActivity(new Intent(FornecedoresActivity.this, FornecedoresActivity.class));
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        List<FornecedorModel>lista = new ArrayList<>(fornecedorList);
        for (FornecedorModel value : fornecedorList) {
            if (!value.getFornecedor_nome().toLowerCase().contains(newText.toLowerCase())) {
                lista.remove(value);
            }
        }
        recyclerView = findViewById(R.id.recycleViewFornecedor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        fornecedorRecyclerAdapter = new FornecedorRecyclerAdapter(this, lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fornecedorRecyclerAdapter);


        return false;
    }

    public void resetSearch() {
        recyclerView = findViewById(R.id.recycleViewFornecedor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        fornecedorRecyclerAdapter = new FornecedorRecyclerAdapter(this, fornecedorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fornecedorRecyclerAdapter);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Acesso Aceite", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, "Acesso Recusado", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }
}
