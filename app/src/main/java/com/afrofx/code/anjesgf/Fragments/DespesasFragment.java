package com.afrofx.code.anjesgf.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.DespesasActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.DispesasRecyclerAdapter;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
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
import com.itextpdf.text.Image;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.String.format;


public class DespesasFragment extends Fragment {

    private DatabaseHelper db;
    private List<DispesasModel> dispesasModelList;
    private TextView txtPendente, txtPaga;
    private CompanyModel companyModel;
    private String path;
    private DispesasModel dispesasModel;
    private ViewPager viewPager;
    private PageAdapters pageAdapters;
    private TabLayout tab1;

    private List<String> dispesaListaCategoria = new ArrayList<>();
    private Calendar myCalendar = Calendar.getInstance();
    private String data;

    public DespesasFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_despesas, container, false);
        db = new DatabaseHelper(getContext());

        txtPendente = (TextView) v.findViewById(R.id.despesaPendente);
        txtPaga = (TextView) v.findViewById(R.id.despesaPaga);

        double pendente = 0, pago = 0;

        setHasOptionsMenu(true);

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

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewDispesa);
        recyclerView.setHasFixedSize(true);
        DispesasRecyclerAdapter dispesasRecyclerAdapter = new DispesasRecyclerAdapter(getContext(), dispesasModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(dispesasRecyclerAdapter);


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.dispesa_adicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistarDispesa();
            }
        });

        return v;
    }

    public void ListaDespesa() {
        db = new DatabaseHelper(getContext());

        dispesasModelList = new ArrayList<>();

        Cursor cursor = db.listaDispesas();

        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Nenhum Registo Encontrado", Toast.LENGTH_SHORT);
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

                dispesasModel = new DispesasModel(id_despesa, estado, valor, descricao, categoria, data, nomeConta, id_op);

                dispesasModelList.add(dispesasModel);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mercadoria_vendida, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Mozê - Lista de Despesas");

                startActivity(Intent.createChooser(intentShareFile, "Enviar Ficheiro"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void imprimeVendas() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());
        path = Environment.getExternalStorageDirectory() + "/Lista de Despesas " + timeStamp + ".pdf";

        dados();

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
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

            Chunk chunk = new Chunk("Lista de Despesas", chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            chapter.add((new Paragraph(companyModel.getNome_empresa(), paragraphFont)));
            chapter.add(new Paragraph("Endereço: " + companyModel.getEndereco(), data));
            chapter.add(new Paragraph("Data: " + timeStamp2, data));
            chapter.add(new Paragraph("Contacto: " + companyModel.getContacto(), data));
            chapter.add(new Paragraph("Operador: " + value, data));

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

            Toast.makeText(getContext(), "Documento Criado", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }

    public void dados() {
        db = new DatabaseHelper(getContext());

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
                    String email = cursor.getString(cursor.getColumnIndex("email_empresa"));
                    String local = cursor.getString(cursor.getColumnIndex("localidade_empresa"));

                    companyModel = new CompanyModel(id_user, contacto, nuit, empresa, nome, email, local);

                }
            }
        }
    }

    public void RegistarDispesa() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptDespesa = li.inflate(R.layout.prompt_add_despesa, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptDespesa);

        db = new DatabaseHelper(getContext());

        List<String> lables = db.listContas2();

        List<String> itens_despesa = db.listaDespesaCategoria();

        final EditText txtValorDispesa = (EditText) promptDespesa.findViewById(R.id.txtCustoDispesa);
        final EditText txtdescricaoDispesa = (EditText) promptDespesa.findViewById(R.id.txtDescricaoDespesa);
        final EditText txtDataDispesa = (EditText) promptDespesa.findViewById(R.id.txtdataDispesa);

        final Spinner spinnerCategoriaDispesa = (Spinner) promptDespesa.findViewById(R.id.spinnerCategoriaDispesa);
        final Spinner spinnerContaDispesa = (Spinner) promptDespesa.findViewById(R.id.spinnerContaDispesa);

        final CheckBox checkEstadoDispesa = (CheckBox) promptDespesa.findViewById(R.id.checkEstadoDispesa);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, itens_despesa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDispesa.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lables);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContaDispesa.setAdapter(adapter2);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                txtDataDispesa.setText(sdf.format(myCalendar.getTime()));
                data = sdf.format(myCalendar.getTime());
            }

        };

        txtDataDispesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

                final int id_conta = db.idConta(Conta);
                final int id_categoria = db.idCategoriaDispesa(Categoria);
                int estadoPagamento = 0;

                if (valorDispesa == 0.0) {
                    Toast.makeText(getContext(), "Adicione o Valor", Toast.LENGTH_LONG).show();
                } else {
                    if (checkEstadoDispesa.isChecked()) {
                        if (db.SaldoTotal(Conta) >= valorDispesa) {
                            estadoPagamento = 0;
                            ContaModel dispesasModel = new ContaModel(valorDispesa, id_conta, estadoPagamento);
                            db.registarValor(dispesasModel);


                            DispesasModel dispesasModel1 = new DispesasModel(id_categoria, db.idOperacao(), descricao, data);
                            db.apenasRegistaDespesa(dispesasModel1);

                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Dispesa Paga!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            tab1 = (TabLayout) getActivity().findViewById(R.id.tabLayoutDespesas);
                                            viewPager = (ViewPager) getActivity().findViewById(R.id.pageViewerDespesas);
                                            pageAdapters = new PageAdapters(getFragmentManager());
                                            pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
                                            pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
                                            viewPager.setAdapter(pageAdapters);

                                            viewPager.setCurrentItem(0);
                                            sDialog.dismiss();
                                            sDialog.setCancelable(false);
                                        }
                                    })
                                    .show();
                        } else {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
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

                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Despesa Registada!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        tab1 = (TabLayout) getActivity().findViewById(R.id.tabLayoutDespesas);
                                        viewPager = (ViewPager) getActivity().findViewById(R.id.pageViewerDespesas);
                                        pageAdapters = new PageAdapters(getFragmentManager());
                                        pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
                                        pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
                                        viewPager.setAdapter(pageAdapters);

                                        viewPager.setCurrentItem(0);
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
