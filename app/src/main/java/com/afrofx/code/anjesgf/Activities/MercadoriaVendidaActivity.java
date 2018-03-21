package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.RecyclerMercadoriaVendida;
import com.afrofx.code.anjesgf.models.StockModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class MercadoriaVendidaActivity extends AppCompatActivity {

    private RecyclerMercadoriaVendida recyclerMercadoriaVendida;
    private List<StockModel> stockModelList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercadoria_vendida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMercadoriasVendidas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        listaMercadoriaVendida();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewMercadoriaVendida);
        recyclerView.setHasFixedSize(true);
        recyclerMercadoriaVendida = new RecyclerMercadoriaVendida(this, stockModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerMercadoriaVendida);
    }


    public void listaMercadoriaVendida(){

        stockModelList = new ArrayList<>();

        Cursor dados = db.listaVendas();

        if(dados.getCount()==0) {
        }else{
            while(dados.moveToNext()){
                String nome = dados.getString(dados.getColumnIndex("produto_nome"));
                String dataVenda = dados.getString(dados.getColumnIndex("venda_data"));
                int numero = Integer.parseInt(dados.getString(dados.getColumnIndex("venda_quantidade")));
                int idUsuario = Integer.parseInt(dados.getString(dados.getColumnIndex("id_usuario")));
                double data = Double.parseDouble(dados.getString(dados.getColumnIndex("produto_preco_venda")));
                StockModel stockModel = new StockModel(numero, data, nome, dataVenda, idUsuario);
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
            startActivity(new Intent(MercadoriaVendidaActivity.this, MainVendasActivity.class));
            finish();
            return true;
        }if(id == R.id.imprimirVendas){
            imprimeVendas();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void imprimeVendas(){
        Document doc = new Document(PageSize.A4);

        String outpath = Environment.getExternalStorageDirectory() + "/text3.pdf";

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outpath));

            doc.open();
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
            Font data = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);

            Chunk chunk = new Chunk("Relatório de vendas", chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph("Mercearia Pao da Familia", paragraphFont));
            chapter.add(new Paragraph("Av. Eduardo Mondlane, Nº 245", data));
            chapter.add(new Paragraph("Data: 12 de Setembro de 2018", data));
            chapter.add(new Paragraph("Contacto: 844145100", data));
            chapter.add(new Paragraph("Operador: Anabela Romao", data));

            Chunk linebreak = new Chunk(new LineSeparator());
            doc.add(chapter);
            doc.add(linebreak);

            PdfPTable table = new PdfPTable(4);
            PdfPCell dataVenda = new PdfPCell(new Phrase("Data"));
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);
            PdfPCell contat = new PdfPCell(new Phrase("Nome Produto"));
            contat.setBackgroundColor(BaseColor.GRAY);
            table.addCell(contat);

            PdfPCell nome = new PdfPCell(new Phrase("Quantidade"));
            nome.setBackgroundColor(BaseColor.GRAY);
            table.addCell(nome);


            PdfPCell age = new PdfPCell(new Phrase("Preço Total"));
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);


            table.setHeaderRows(1);


            for(int i = 0; i<stockModelList.size();i++){

                table.addCell(stockModelList.get(i).getProduto_data_registo());

                table.addCell(stockModelList.get(i).getProduto_nome());

                table.addCell(stockModelList.get(i).getProduto_quantidade()+"");


                double valor = stockModelList.get(i).getProduto_preco_venda() * stockModelList.get(i).getProduto_quantidade();

                String valor2 = format("%,.2f", valor);
                table.addCell(valor2+" MT");

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
}
