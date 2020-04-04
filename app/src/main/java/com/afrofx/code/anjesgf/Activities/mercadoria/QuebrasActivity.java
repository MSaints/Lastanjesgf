package com.afrofx.code.anjesgf.Activities.mercadoria;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrofx.code.anjesgf.Activities.home.MercadoriaActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.Recyclers.RecyclerQuebras;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.QuebrasModel;

import java.util.ArrayList;
import java.util.List;

public class QuebrasActivity extends AppCompatActivity {


    private List<QuebrasModel> quebrasModelList;
    private DatabaseHelper db;
    private CompanyModel companyModel;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_quebras);

        db= new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarQuebras);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        listaQuebras();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewQuebras);
        recyclerView.setHasFixedSize(true);
        RecyclerQuebras recyclerQuebras = new RecyclerQuebras(this, quebrasModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerQuebras);


    }

    public void listaQuebras() {
        db = new DatabaseHelper(this);

        quebrasModelList = new ArrayList<>();

        Cursor dados = db.listaQuebras();

        if (dados.getCount() == 0) {
        } else {
            while (dados.moveToNext()) {
                String quebraProdutoNome = dados.getString(dados.getColumnIndex("produto_nome"));
                String quebraDescricao = dados.getString(dados.getColumnIndex("quebra_descricao"));
                double quebraQuantidade = Double.parseDouble(dados.getString(dados.getColumnIndex("quantidade_valor")));
                double quebraPreco = Double.parseDouble(dados.getString(dados.getColumnIndex("quebra_valor")));
                String quebraDataRegisto = dados.getString(dados.getColumnIndex("quebra_data_registo"));

                QuebrasModel quebrasModel = new QuebrasModel(quebraDescricao, quebraDataRegisto, quebraProdutoNome, quebraQuantidade, quebraPreco);
                quebrasModelList.add(quebrasModel);
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
            startActivity(new Intent(QuebrasActivity.this, MercadoriaActivity.class));
            finish();
            return true;
        }
        if (id == R.id.imprimirVendas) {
            /*imprimeVendas();
            return true;*/
        }
        if(id == R.id.partilhaVendas){
           /* imprimeVendas();
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(path);

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Mozê - Produtos Mais Vendidos");

                startActivity(Intent.createChooser(intentShareFile, "Enviar Ficheiro"));
            }*/
        }
        return super.onOptionsItemSelected(item);
    }

/*
    public void imprimeVendas() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());
        path = Environment.getExternalStorageDirectory() + "/Produtos Mais Vendidos " + timeStamp + ".pdf";

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

            Chunk chunk = new Chunk("Produtos Mais Vendidos", chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);

            if(companyModel!=null){
                chapter.add((new Paragraph(companyModel.getNome_empresa(), paragraphFont)));
                chapter.add(new Paragraph("Endereço: " + companyModel.getEndereco(), data));
                chapter.add(new Paragraph("Data: " + timeStamp2, data));
                chapter.add(new Paragraph("Contacto: " + companyModel.getContacto(), data));
                chapter.add(new Paragraph("Operador: " + value, data));
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

            for (int i = 0; i < quebrasModelList.size(); i++) {
                PdfPCell age1 = new PdfPCell(new Phrase(i+""));
                age1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(age1);
                table.addCell(quebrasModelList.get(i).getProduto_nome());
                table.addCell(quebrasModelList.get(i).getCategoria());

                double valor = stockModelList.get(i).getProduto_quantidade();
                PdfPCell age2 = new PdfPCell(new Phrase(valor+""));
                age2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(age2);
            }


            doc.add(table);
            doc.close();

            Toast.makeText(this, "Documento Criado", Toast.LENGTH_SHORT).show();


            try{
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("file://"+path), "text/pdf");
                startActivity(i);
            }catch (Exception e){
                Toast.makeText(this, "Sem Aplicativo", Toast.LENGTH_SHORT).show();
            }

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
    }*/
}
