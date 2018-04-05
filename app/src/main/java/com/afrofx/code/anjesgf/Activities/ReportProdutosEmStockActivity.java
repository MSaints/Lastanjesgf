package com.afrofx.code.anjesgf.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.Fragments.RelatorioFragment;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.ReportCliente1Recycler;
import com.afrofx.code.anjesgf.adpters.ReportStockRecycler;
import com.afrofx.code.anjesgf.models.ClienteModel;
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
import com.itextpdf.text.Rectangle;
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


public class ReportProdutosEmStockActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ReportStockRecycler reportStockRecycler;
    private List<StockModel> stockModelList ;
    private TextView dat1, dat2;
    private CompanyModel companyModel;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_produtos_stock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRelatorioStock);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);


        dat1 = (TextView)findViewById(R.id.stockDataInicial);
        dat2 = (TextView)findViewById(R.id.stockDataFinal);



        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data1 = (mSharedPreference.getString("dataInicio", "2018-01-01"));
        String data2 = (mSharedPreference.getString("dataFim", "2018-12-02"));

        listaProdutosRelatorio(data1, data2);

        dat1.setText(data1);
        dat2.setText(data2);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewStockReport);
        recyclerView.setHasFixedSize(true);
        reportStockRecycler = new ReportStockRecycler(this, stockModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(reportStockRecycler);
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
            startActivity(new Intent(ReportProdutosEmStockActivity.this, MainScreenActivity.class));
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

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Mozê - Registo de Vendas");

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
                String nome = dados.getString(dados.getColumnIndex("produto_nome"));
                String cate = dados.getString(dados.getColumnIndex("categoria_nome"));
                int numero = Integer.parseInt(dados.getString(dados.getColumnIndex("produto_quantidade")));
                double data = Double.parseDouble(dados.getString(dados.getColumnIndex("produto_preco_venda")));
                StockModel stockModel = new StockModel(numero, data, nome, cate);
                stockModelList.add(stockModel);
            }
        }
    }


    public void imprimeVendas() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());
        path = Environment.getExternalStorageDirectory() + "/Produtos em Stock " + timeStamp + ".pdf";

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

            Chunk chunk = new Chunk("Produtos em Stock", chapterFont);
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

            PdfPCell age = new PdfPCell(new Phrase("Preço Total", head));
            age.setHorizontalAlignment(Element.ALIGN_CENTER);
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);

            table.setHeaderRows(1);

            double money = 0;

            for (int i = 0; i < stockModelList.size(); i++) {
                table.addCell(stockModelList.get(i).getProduto_nome());
                table.addCell(stockModelList.get(i).getCategoria());
                table.addCell(stockModelList.get(i).getProduto_quantidade() + "");
                double valor = stockModelList.get(i).getProduto_preco_venda() * stockModelList.get(i).getProduto_quantidade();
                money = money + valor;
                String valor2 = format("%,.2f", valor);
                table.addCell(valor2 + " MT");
            }

            PdfPCell cellr = new PdfPCell();
            cellr.setBorder(Rectangle.NO_BORDER);
            table.addCell(cellr);

            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Valor Total:"));
            cell3.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell3);

            String valor4 = format("%,.2f", money);

            PdfPCell cell4 = new PdfPCell(new Phrase(valor4 + " MT", head));
            table.addCell(cell4);

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
                    String email = cursor.getString(cursor.getColumnIndex("email_empresa"));
                    String local = cursor.getString(cursor.getColumnIndex("localidade_empresa"));

                    companyModel = new CompanyModel(id_user, contacto, nuit, empresa, nome, email, local);

                }
            }
        }
    }
    public void Mensagem(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }
}
