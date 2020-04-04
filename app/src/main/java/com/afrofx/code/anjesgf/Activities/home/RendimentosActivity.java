package com.afrofx.code.anjesgf.Activities.home;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.RendimentosRecyclerAdapter;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RedimentosModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class RendimentosActivity extends AppCompatActivity {

    private TextView rendimentoRecebidos, rendimentoPendentes;
    private List<RedimentosModel> redimentosModels;
    private DatabaseHelper db;
    private FloatingActionButton addRendime;
    private CompanyModel companyModel;

    private String data;
    private String path;
        Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_rendimentos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRedimentos);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        rendimentoPendentes = (TextView) findViewById(R.id.rendimentoPendente);
        rendimentoRecebidos = (TextView) findViewById(R.id.rendimentoTotal);

        addRendime = (FloatingActionButton) findViewById(R.id.rendimento_adicionar);

        double pendente = 0, pago = 0;

        ListaRendimentos();

        for (int i = 0; i < redimentosModels.size(); i++) {
            if (redimentosModels.get(i).getTipoOpe() == 2) {
                pendente = pendente + redimentosModels.get(i).getRendimento_valor();
            }
            if (redimentosModels.get(i).getTipoOpe() == 1) {
                pago = pago + redimentosModels.get(i).getRendimento_valor();
            }
        }
        String pen = format("%,.2f", pendente);
        String pag = format("%,.2f", pago);

        rendimentoPendentes.setText(pen + " MT");
        rendimentoRecebidos.setText(pag + " MT");

        addRendime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRendimento();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRendimentos);
        recyclerView.setHasFixedSize(true);
        RendimentosRecyclerAdapter rendimentosRecyclerAdapter = new RendimentosRecyclerAdapter(this, redimentosModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(rendimentosRecyclerAdapter);
    }

    public void addRendimento() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptRendimento = li.inflate(R.layout.prompt_add_rendimento, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptRendimento);

        TextView view = (TextView) promptRendimento.findViewById(R.id.definitionInfoTitle);
        view.setBackgroundColor(Constant.color);

        db = new DatabaseHelper(this);

        List<String> lables = db.listContas();

        final Spinner spinnerRendimentoConta = (Spinner) promptRendimento.findViewById(R.id.spinnerContaRendimento);
        final EditText rendimentoData = (EditText) promptRendimento.findViewById(R.id.txtdataRendimento);
        final EditText add_valor = (EditText) promptRendimento.findViewById(R.id.valorRendimento);
        final EditText rendimentoDescricao = (EditText) promptRendimento.findViewById(R.id.txtDescricaoRendimento);

        final CheckBox estadoRendimento = (CheckBox) promptRendimento.findViewById(R.id.recebidoRendimento);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRendimentoConta.setAdapter(adapter2);

        final Calendar myCalendar = Calendar.getInstance();

        int id_conta = 0;

        rendimentoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH);
            int ano = calendar.get(Calendar.YEAR);

            DatePickerDialog dialog = new android.app.DatePickerDialog(RendimentosActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendario = new java.util.GregorianCalendar(year, month, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                rendimentoData.setText(sdf.format(calendario.getTime()));
                data = sdf.format(myCalendar.getTime());
                }
            }, ano, mes, dia);

        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable( android.graphics.Color.TRANSPARENT));
        dialog.show();
            }
        });


        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String val = add_valor.getText().toString().trim();
                final double valorRend = !val.equals("") ? Double.parseDouble(val) : 0.0;

                String descri = rendimentoDescricao.getText().toString().trim();

                final long idConta = db.idConta(spinnerRendimentoConta.getSelectedItem().toString());

                if (estadoRendimento.isChecked()) {
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 1);
                    db.registarValor(contaModel);

                    RedimentosModel redimentosModel = new RedimentosModel(descri, data, db.idOperacao());
                    db.registoRendimento(redimentosModel);
                    new SweetAlertDialog(RendimentosActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Recebido")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                    startActivity(new Intent(RendimentosActivity.this, RendimentosActivity.class));
                                    sDialog.dismiss();
                                    sDialog.setCancelable(false);
                                }
                            })
                            .show();
                } else {
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 2);
                    db.registarValor(contaModel);
                    final RedimentosModel redimentosModel = new RedimentosModel(descri, data, db.idOperacao());
                    db.registoRendimento(redimentosModel);
                    new SweetAlertDialog(RendimentosActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Registado")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                    startActivity(new Intent(RendimentosActivity.this, RendimentosActivity.class));
                                    sDialog.dismiss();
                                    sDialog.setCancelable(false);
                                }
                            })
                            .show();
                }


            }
        });

        // Criar O Alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra o alerta
        alertDialog.show();
    }

    public void ListaRendimentos() {
        db = new DatabaseHelper(this);

        redimentosModels = new ArrayList<>();

        Cursor cursor = db.listaRendimentos();

        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                int id_rend = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_rendimento")));
                int id_op = Integer.parseInt(cursor.getString(cursor.getColumnIndex("tipo_operacao")));
                int id_reg_operacao = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_registo_operacao")));
                double valor = Double.parseDouble(cursor.getString(cursor.getColumnIndex("operacao_valor")));
                String descricao = (cursor.getString(cursor.getColumnIndex("rendimento_descricao")));
                String data = (cursor.getString(cursor.getColumnIndex("rendimento_data")));
                String nomeConta = cursor.getString(cursor.getColumnIndex("conta_nome"));

                RedimentosModel rendimentosModel = new RedimentosModel(descricao, data, nomeConta, id_rend, valor, id_op, id_reg_operacao);

                redimentosModels.add(rendimentosModel);
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
            startActivity(new Intent(RendimentosActivity.this, MainScreenActivity.class));
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

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ECONTA - Lista de Rendimentos");

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
            path = folder + "/Lista de Rendimentos" + timeStamp + ".pdf";
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

            Paragraph p0 = new Paragraph("Lista de Rendimentos", chapterFont);
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

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            PdfPCell dataVenda = new PdfPCell(new Phrase("Descrição", head));
            dataVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);


            PdfPCell nome = new PdfPCell(new Phrase("Conta", head));
            nome.setHorizontalAlignment(Element.ALIGN_CENTER);
            nome.setBackgroundColor(BaseColor.GRAY);
            table.addCell(nome);

            PdfPCell age = new PdfPCell(new Phrase("Estado", head));
            age.setHorizontalAlignment(Element.ALIGN_CENTER);
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);

            PdfPCell age8 = new PdfPCell(new Phrase("Data", head));
            age8.setHorizontalAlignment(Element.ALIGN_CENTER);
            age8.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age8);

            PdfPCell age7 = new PdfPCell(new Phrase("Valor", head));
            age7.setHorizontalAlignment(Element.ALIGN_CENTER);
            age7.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age7);

            table.setHeaderRows(1);

            double money = 0;

            for (int i = 0; i < redimentosModels.size(); i++) {
                table.addCell(redimentosModels.get(i).getRedimentoDescricao());
                table.addCell(redimentosModels.get(i).getConta());

                if (redimentosModels.get(i).getTipoOpe() == 2) {
                    PdfPCell age2 = new PdfPCell(new Phrase("Não Recebido", estado));
                    age2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    age2.setBackgroundColor(BaseColor.RED);
                    table.addCell(age2);
                }

                if (redimentosModels.get(i).getTipoOpe() == 1) {
                    PdfPCell age2 = new PdfPCell(new Phrase("Recebido", estado));
                    age2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    age2.setBackgroundColor(BaseColor.GREEN);
                    table.addCell(age2);
                }

                table.addCell(redimentosModels.get(i).getRendimentoData());
                double valor = redimentosModels.get(i).getRendimento_valor();
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

}
