package com.afrofx.code.anjesgf.Activities.home;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.*;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.DispesasRecyclerAdapter;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.DispesasModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.String.format;

public class DespesasActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private List<String> dispesaListaCategoria = new ArrayList<>();
    private List<DispesasModel> dispesasModelList;
    private Calendar myCalendar = Calendar.getInstance();
    private String data;
    private CompanyModel companyModel;
    private String path;

    Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.topbar_despesas_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDespesas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        TextView txtPendente = findViewById(R.id.despesaPendente);
        TextView txtPaga = findViewById(R.id.despesaPaga);

        double pendente = 0, pago = 0;

        ListaDespesa();

        for (int i = 0; i < dispesasModelList.size(); i++) {
            if (dispesasModelList.get(i).getTipo_operacao() == 2) {
                pendente = pendente + dispesasModelList.get(i).getCusto_dispesa();
            }
            if (dispesasModelList.get(i).getTipo_operacao() == 0) {
                pago = pago + dispesasModelList.get(i).getCusto_dispesa();
            }
        }
        String pen = format("%,.2f", pendente);
        String pag = format("%,.2f", pago);

        txtPendente.setText(pen + " MT");
        txtPaga.setText(pag + " MT");

        RecyclerView recyclerView =  findViewById(R.id.recyclerViewDispesa);
        recyclerView.setHasFixedSize(true);
        DispesasRecyclerAdapter dispesasRecyclerAdapter = new DispesasRecyclerAdapter(this, dispesasModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(dispesasRecyclerAdapter);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.dispesa_adicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistarDispesa();
            }
        });
    }

    public void ListaDespesa() {
        db = new DatabaseHelper(this);

        dispesasModelList = new ArrayList<>();

        Cursor cursor = db.listaDispesas();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Sem Despesas Registadas", Toast.LENGTH_SHORT);
        } else {
            while (cursor.moveToNext()) {
                int id_despesa = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_despesa")));
                double valor = Double.parseDouble(cursor.getString(cursor.getColumnIndex("operacao_valor")));
                String descricao = (cursor.getString(cursor.getColumnIndex("despesa_descricao")));
                String data = (cursor.getString(cursor.getColumnIndex("despesa_data")));
                String categoria = (cursor.getString(cursor.getColumnIndex("despesa_categoria_nome")));
                int estado = Integer.parseInt(cursor.getString(cursor.getColumnIndex("tipo_operacao")));
                String nomeConta = cursor.getString(cursor.getColumnIndex("conta_nome"));
                int id_op = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_registo_operacao")));

                DispesasModel dispesasModel = new DispesasModel(id_despesa, estado, valor, descricao, categoria, data, nomeConta, id_op);

                dispesasModelList.add(dispesasModel);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mercadoria_vendida, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(DespesasActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        if (id == R.id.imprimirVendas) {
            imprimeVendas();
            return true;
        }
        if (id == R.id.partilhaVendas) {
            imprimeVendas();
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(path);

            if (fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ECONTA - Lista de Despesas");

                startActivity(Intent.createChooser(intentShareFile, "Enviar Ficheiro"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void imprimeVendas() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Econta_Relatorios");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            path = folder + "/Lista de Despesas" + timeStamp + ".pdf";
        } else {
            Toast.makeText(this, "Erro Encontrado", Toast.LENGTH_SHORT).show();
        }

        dados();

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        String value = (mSharedPreference.getString("name_user", "Default_Value"));

        Locale localeBR = new Locale("pt", "BR");
        SimpleDateFormat fmt = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", localeBR);
        String timeStamp2 = fmt.format(Calendar.getInstance().getTime());

        Document doc = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(path));

            MyFooter event = new MyFooter();
            writer.setPageEvent(event);

            doc.open();

            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font data = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
            Font head = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font estado = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE);

            Paragraph p0 = new Paragraph("Lista de Despesas", chapterFont);
            p0.setAlignment(Element.ALIGN_CENTER);
            Chapter chapter = new Chapter(p0, 1);
            chapter.setNumberDepth(0);

            if (companyModel != null) {
                Paragraph p1 = new Paragraph(companyModel.getNome_empresa(), paragraphFont);
                p1.setAlignment(Element.ALIGN_CENTER);
                chapter.add((p1));

                Paragraph p2 =new Paragraph("Endereço: " + companyModel.getEndereco(), data);
                p2.setAlignment(Element.ALIGN_CENTER);
                chapter.add(p2);

                Paragraph p3 = new Paragraph("Data: " + timeStamp2, data);
                p3.setAlignment(Element.ALIGN_CENTER);
                chapter.add(p3);

                Paragraph p4 = new Paragraph("Contacto: " + companyModel.getContacto(), data);
                p4.setAlignment(Element.ALIGN_CENTER);
                chapter.add(p4);

                Paragraph p5 = new Paragraph("Operador: " + value, data);
                p5.setAlignment(Element.ALIGN_CENTER);
                chapter.add(p5);
            }

            Chunk linebreak = new Chunk(new LineSeparator());
            doc.add(chapter);
            doc.add(linebreak);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            PdfPCell dataVenda = new PdfPCell(new Phrase("Descrição", head));
            dataVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataVenda.setVerticalAlignment(Element.ALIGN_CENTER);
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);

            PdfPCell contat = new PdfPCell(new Phrase("Categoria", head));
            contat.setHorizontalAlignment(Element.ALIGN_CENTER);
            contat.setVerticalAlignment(Element.ALIGN_CENTER);
            contat.setBackgroundColor(BaseColor.GRAY);
            table.addCell(contat);

            PdfPCell nome = new PdfPCell(new Phrase("Conta", head));
            nome.setHorizontalAlignment(Element.ALIGN_CENTER);
            nome.setVerticalAlignment(Element.ALIGN_CENTER);
            nome.setBackgroundColor(BaseColor.GRAY);
            table.addCell(nome);

            PdfPCell age = new PdfPCell(new Phrase("Estado", head));
            age.setHorizontalAlignment(Element.ALIGN_CENTER);
            age.setVerticalAlignment(Element.ALIGN_CENTER);
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);

            PdfPCell age8 = new PdfPCell(new Phrase("Data", head));
            age8.setHorizontalAlignment(Element.ALIGN_CENTER);
            age8.setVerticalAlignment(Element.ALIGN_CENTER);
            age8.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age8);

            PdfPCell age7 = new PdfPCell(new Phrase("Custo", head));
            age7.setHorizontalAlignment(Element.ALIGN_CENTER);
            age7.setVerticalAlignment(Element.ALIGN_CENTER);
            age7.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age7);

            table.setHeaderRows(1);

            double money = 0;

            for (int i = 0; i < dispesasModelList.size(); i++) {

                table.addCell(dispesasModelList.get(i).getDescricao_dispesa());

                PdfPCell categ = new PdfPCell(new Phrase(dispesasModelList.get(i).getCategoria_dispesa()));
                categ.setHorizontalAlignment(Element.ALIGN_CENTER);
                categ.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(categ);

                PdfPCell cont = new PdfPCell(new Phrase(dispesasModelList.get(i).getConta_dispesa()));
                cont.setHorizontalAlignment(Element.ALIGN_CENTER);
                cont.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cont);


                if (dispesasModelList.get(i).getTipo_operacao() == 2) {
                    PdfPCell age2 = new PdfPCell(new Phrase("PENDENTE", estado));
                    age2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    age2.setVerticalAlignment(Element.ALIGN_CENTER);
                    age2.setBackgroundColor(BaseColor.RED);
                    table.addCell(age2);

                }
                if (dispesasModelList.get(i).getTipo_operacao() == 0) {
                    PdfPCell age2 = new PdfPCell(new Phrase("Paga", estado));
                    age2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    age2.setVerticalAlignment(Element.ALIGN_CENTER);
                    age2.setBackgroundColor(BaseColor.GREEN);
                    table.addCell(age2);
                }

                table.addCell(dispesasModelList.get(i).getData_pagamento());
                double valor = dispesasModelList.get(i).getCusto_dispesa();
                money = money + valor;
                String valor2 = format("%,.2f", valor);
                table.addCell(valor2 + " MT");
            }

            doc.add(table);
            doc.close();

            Toast.makeText(this, "Documento Criado", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void dados() {
        db = new DatabaseHelper(this);

        Cursor cursor = db.empresaDetalhes();

        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                if (cursor != null && cursor.getString(1) != null) {
                    int id_user = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_usuario")));
                    int nuit = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nuit_empresa")));
                    int contacto = Integer.parseInt(cursor.getString(cursor.getColumnIndex("numero_empresa")));
                    String nome = cursor.getString(cursor.getColumnIndex("dona_empresa"));
                    String empresa = cursor.getString(cursor.getColumnIndex("nome_empresa"));
                    String local = cursor.getString(cursor.getColumnIndex("localidade_empresa"));

                    companyModel = new CompanyModel(id_user, contacto, nuit, empresa, nome, local);

                }
            }
        }
    }

    public void RegistarDispesa() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptDespesa = li.inflate(R.layout.prompt_add_despesa, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptDespesa);

        db = new DatabaseHelper(this);

        TextView view = (TextView) promptDespesa.findViewById(R.id.definitionInfoTitle);
        view.setBackgroundColor(Constant.color);

        List<String> lables = db.listContas2();

        List<String> itens_despesa = db.listaDespesaCategoria();

        final EditText txtValorDispesa =  promptDespesa.findViewById(R.id.txtCustoDispesa);
        final EditText txtdescricaoDispesa = promptDespesa.findViewById(R.id.txtDescricaoDespesa);
        final EditText txtDataDispesa =  promptDespesa.findViewById(R.id.txtdataDispesa);

        final Spinner spinnerCategoriaDispesa = promptDespesa.findViewById(R.id.spinnerCategoriaDispesa);
        final Spinner spinnerContaDispesa = promptDespesa.findViewById(R.id.spinnerContaDispesa);

        final CheckBox checkEstadoDispesa =  promptDespesa.findViewById(R.id.checkEstadoDispesa);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itens_despesa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDispesa.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContaDispesa.setAdapter(adapter2);
/*
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                txtDataDispesa.setText(sdf.format(myCalendar.getTime()));
                data = sdf.format(myCalendar.getTime());
            }

        };*/

        txtDataDispesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH);
            int ano = calendar.get(Calendar.YEAR);

            DatePickerDialog dialog = new android.app.DatePickerDialog(DespesasActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendario = new java.util.GregorianCalendar(year, month, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                txtDataDispesa.setText(sdf.format(calendario.getTime()));
                data = sdf.format(myCalendar.getTime());
                }
            }, ano, mes, dia);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( android.graphics.Color.TRANSPARENT));
        dialog.show();
            }
        });

        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                final String descricao = txtdescricaoDispesa.getText().toString().trim();
                final String Conta = spinnerContaDispesa.getSelectedItem().toString();
                String Categoria = spinnerCategoriaDispesa.getSelectedItem().toString();
                final String data = txtDataDispesa.getText().toString().trim();

                String valor = txtValorDispesa.getText().toString().trim();
                final double valorDispesa = !valor.equals("") ? Double.parseDouble(valor) : 0.0;

                final long id_conta = db.idConta(Conta);
                final long id_categoria = db.idCategoriaDispesa(Categoria);
                int estadoPagamento = 0;

                if (valorDispesa == 0.0) {
                    Toast.makeText(DespesasActivity.this, "Adicione o Valor", Toast.LENGTH_LONG).show();
                } else {
                    if (checkEstadoDispesa.isChecked()) {
                        if (db.SaldoTotal(Conta) >= valorDispesa) {
                            estadoPagamento = 0;
                            ContaModel dispesasModel = new ContaModel(valorDispesa, id_conta, estadoPagamento);
                            db.registarValor(dispesasModel);


                            DispesasModel dispesasModel1 = new DispesasModel(id_categoria, db.idOperacao(), descricao, data);
                            db.apenasRegistaDespesa(dispesasModel1);

                            new SweetAlertDialog(DespesasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Dispesa Paga!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            finish();
                                            startActivity(new Intent(DespesasActivity.this, DespesasActivity.class));
                                            sDialog.dismiss();
                                            sDialog.setCancelable(false);
                                        }
                                    })
                                    .show();
                        } else {
                            new SweetAlertDialog(DespesasActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Saldo Insuficiente!")
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

                    } else {
                        estadoPagamento = 2;
                        ContaModel dispesasModel = new ContaModel(valorDispesa, id_conta, estadoPagamento);
                        db.registarValor(dispesasModel);

                        DispesasModel dispesasModel1 = new DispesasModel(id_categoria, db.idOperacao(), descricao, data);
                        db.apenasRegistaDespesa(dispesasModel1);

                        new SweetAlertDialog(DespesasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Despesa Registada!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                        startActivity(new Intent(DespesasActivity.this, DespesasActivity.class));
                                        sDialog.dismiss();
                                        sDialog.setCancelable(false);
                                    }
                                })
                                .show();

                    }
                }
            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }


}
