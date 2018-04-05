package com.afrofx.code.anjesgf.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.RecyclerMercadoriaVendida;
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

public class MercadoriaVendidaActivity extends AppCompatActivity {

    private RecyclerMercadoriaVendida recyclerMercadoriaVendida;
    private List<StockModel> stockModelList;
    private DatabaseHelper db;
    private CompanyModel companyModel;
    private TextView totalAmount;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercadoria_vendida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMercadoriasVendidas);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        totalAmount = (TextView)findViewById(R.id.vendasTotal);


        listaMercadoriaVendida();

        double p = 0;

        for(int i = 0; i<stockModelList.size(); i++){
            p = p + (stockModelList.get(i).getProduto_preco_venda()* stockModelList.get(i).getProduto_quantidade());
        }

        String valor2 = format("%,.2f", p);
        totalAmount.setText(valor2+" MT");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMercadoriaVendida);
        recyclerView.setHasFixedSize(true);
        recyclerMercadoriaVendida = new RecyclerMercadoriaVendida(this, stockModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerMercadoriaVendida);
    }


    public void listaMercadoriaVendida() {

        stockModelList = new ArrayList<>();

        Cursor dados = db.listaVendas();

        if (dados.getCount() == 0) {
        } else {
            while (dados.moveToNext()) {
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


    public void imprimeVendas() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());
        path = Environment.getExternalStorageDirectory() + "/Registo de Vendas " + timeStamp + ".pdf";

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

            Chunk chunk = new Chunk("Registo de vendas", chapterFont);
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

            PdfPCell dataVenda = new PdfPCell(new Phrase("Data e Hora", head));
            dataVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);

            PdfPCell contat = new PdfPCell(new Phrase("Nome Produto", head));
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
                table.addCell(stockModelList.get(i).getProduto_data_registo());
                table.addCell(stockModelList.get(i).getProduto_nome());
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

            PdfPCell cell4 = new PdfPCell(new Phrase(valor4 + "MZN", head));
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
}
