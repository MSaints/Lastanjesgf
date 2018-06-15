package com.afrofx.code.anjesgf.Activities.vendas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.home.VendasActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.RecyclerMaisVendida;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.StockModel;
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

import static java.lang.String.format;

public class MaisVendidosActivity extends AppCompatActivity {


    private List<StockModel> stockModelList;
    private DatabaseHelper db;
    private CompanyModel companyModel;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_mais_vendidos);

        db= new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbarMaisVendidos);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        listaMaisVendida();

        RecyclerView recyclerView =  findViewById(R.id.recyclerViewMaisVendida);
        recyclerView.setHasFixedSize(true);
        RecyclerMaisVendida recyclerMaisVendida = new RecyclerMaisVendida(this, stockModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerMaisVendida);


    }

    public void listaMaisVendida() {
        db = new DatabaseHelper(this);

        stockModelList = new ArrayList<>();

        Cursor dados = db.listaMaisVendidos();

        if (dados.getCount() == 0) {
        } else {
            while (dados.moveToNext()) {
                String nome = dados.getString(dados.getColumnIndex("produto_nome"));
                String categoria = dados.getString(dados.getColumnIndex("categoria_nome"));
                double qtd = Double.parseDouble(dados.getString(2));
                StockModel stockModel = new StockModel(qtd, nome, categoria);
                stockModelList.add(stockModel);
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
            startActivity(new Intent(MaisVendidosActivity.this, VendasActivity.class));
            finish();
            return true;
        }
        if (id == R.id.imprimirVendas) {
            imprimeVendas();
            return true;
        }
        if(id == R.id.partilhaVendas){
            imprimeVendas();
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(path);

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ECONTA - Produtos Mais Vendidos");

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
            path = folder + "/Pro. Mais Vendidos" + timeStamp + ".pdf";
        } else {
            Toast.makeText(this, "Erro Encontrado", Toast.LENGTH_SHORT).show();
        }

        dados();

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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

            Paragraph p0 = new Paragraph("Produtos Mais Vendidos", chapterFont);
            p0.setAlignment(Element.ALIGN_CENTER);
            Chapter chapter = new Chapter(p0, 1);
            chapter.setNumberDepth(0);

            if(companyModel!=null){
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

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            PdfPCell dataVenda = new PdfPCell(new Phrase("Numero", head));
            dataVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);

            PdfPCell contat = new PdfPCell(new Phrase("Nome Produto", head));
            contat.setHorizontalAlignment(Element.ALIGN_CENTER);
            contat.setBackgroundColor(BaseColor.GRAY);
            table.addCell(contat);

            PdfPCell nome = new PdfPCell(new Phrase("Categoria", head));
            nome.setHorizontalAlignment(Element.ALIGN_CENTER);
            nome.setBackgroundColor(BaseColor.GRAY);
            table.addCell(nome);

            PdfPCell age = new PdfPCell(new Phrase("Quantidade", head));
            age.setHorizontalAlignment(Element.ALIGN_CENTER);
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);

            table.setHeaderRows(1);

            double money = 0;

            for (int i = 0; i < stockModelList.size(); i++) {
                PdfPCell age1 = new PdfPCell(new Phrase(i+""));
                age1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(age1);
                table.addCell(stockModelList.get(i).getProduto_nome());
                table.addCell(stockModelList.get(i).getCategoria());

                double valor = stockModelList.get(i).getProduto_quantidade();
                PdfPCell age2 = new PdfPCell(new Phrase(valor+""));
                age2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(age2);
            }


            doc.add(table);
            doc.close();

            Toast.makeText(this, "Documento Criado", Toast.LENGTH_SHORT).show();


            /*try{
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("file://"+path), "text/pdf");
                startActivity(i);
            }catch (Exception e){
                Toast.makeText(this, "Sem Aplicativo", Toast.LENGTH_SHORT).show();
            }*/

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
                    long id_user = Long.parseLong(cursor.getString(cursor.getColumnIndex("id_usuario")));
                    long nuit = Long.parseLong(cursor.getString(cursor.getColumnIndex("nuit_empresa")));
                    long contacto = Long.parseLong(cursor.getString(cursor.getColumnIndex("numero_empresa")));
                    String nome = cursor.getString(cursor.getColumnIndex("dona_empresa"));
                    String empresa = cursor.getString(cursor.getColumnIndex("nome_empresa"));
                    String local = cursor.getString(cursor.getColumnIndex("localidade_empresa"));

                    companyModel = new CompanyModel(id_user, contacto, nuit, empresa, nome, local);

                }
            }
        }
    }
}
