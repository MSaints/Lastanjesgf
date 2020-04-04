package com.afrofx.code.anjesgf.Activities.relatorios;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.Report.ReportForaStockRecycler;
import com.afrofx.code.anjesgf.ThemeSettings.Constant;
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


public class ReportProdutosForaStockActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ReportForaStockRecycler reportForaStockRecycler;
    private List<StockModel> stockModelList ;
    private CompanyModel companyModel;
    private String path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_relatorio_fora_stock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRelatorioForaStock);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        TextView dat1 = (TextView) findViewById(R.id.stockDataInicial);
        View line = (View)findViewById(R.id.line);

        line.setBackgroundColor(Constant.color);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data1 = (mSharedPreference.getString("dataInicio", "2018-01-01"));
        String data2 = (mSharedPreference.getString("dataFim", "2018-12-02"));

        listaProdutosRelatorio(data1, data2);

        dat1.setText("Do dia "+data1+" Para o dia "+data2);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewForaStockReport);
        recyclerView.setHasFixedSize(true);
        reportForaStockRecycler = new ReportForaStockRecycler(this, stockModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(reportForaStockRecycler);
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
            startActivity(new Intent(ReportProdutosForaStockActivity.this, MainScreenActivity.class));
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

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ECONTA - Registo produtos fora de stock");

                startActivity(Intent.createChooser(intentShareFile, "Enviar Ficheiro"));
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void listaProdutosRelatorio(String d1, String d2){
        stockModelList = new ArrayList<>();
        Cursor dados = db.listProdutoRelatorio(d1, d2);

        if(dados.getCount()==0) {
            Mensagem("Nenhum Registo Encontrado");
        }else{
            while(dados.moveToNext()){
                int numero = Integer.parseInt(dados.getString(dados.getColumnIndex("produto_quantidade")));
                if(numero==0){
                    String nome = dados.getString(dados.getColumnIndex("produto_nome"));
                    String cate = dados.getString(dados.getColumnIndex("categoria_nome"));
                    double data = Double.parseDouble(dados.getString(dados.getColumnIndex("produto_preco_venda")));
                    StockModel stockModel = new StockModel(numero, data, nome, cate);
                    stockModelList.add(stockModel);
                }
            }
        }
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
            path = folder + "/Fora de Stock " + timeStamp + ".pdf";
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

            Paragraph p0 = new Paragraph("Relatório dos produtos fora de Stock", chapterFont);
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

            PdfPCell dataVenda = new PdfPCell(new Phrase("Nome Produto", head));
            dataVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);

            PdfPCell contat = new PdfPCell(new Phrase("Categoria", head));
            contat.setHorizontalAlignment(Element.ALIGN_CENTER);
            contat.setBackgroundColor(BaseColor.GRAY);
            table.addCell(contat);

            PdfPCell nome = new PdfPCell(new Phrase("Quantidade", head));
            nome.setHorizontalAlignment(Element.ALIGN_CENTER);
            nome.setBackgroundColor(BaseColor.GRAY);
            table.addCell(nome);

            PdfPCell age = new PdfPCell(new Phrase("Preço Unitário", head));
            age.setHorizontalAlignment(Element.ALIGN_CENTER);
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);

            table.setHeaderRows(1);

            double money = 0;

            for (int i = 0; i < stockModelList.size(); i++) {
                table.addCell(stockModelList.get(i).getProduto_nome());
                table.addCell(stockModelList.get(i).getCategoria());
                table.addCell(stockModelList.get(i).getProduto_quantidade() + "");
                double valor = stockModelList.get(i).getProduto_preco_venda();
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
    public void Mensagem(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }


}
