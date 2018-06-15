package com.afrofx.code.anjesgf.Activities.relatorios;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import android.content.*;
import android.view.*;
import android.preference.*;
import android.os.*;
import android.net.*;
import android.support.v7.widget.Toolbar;


import java.io.*;
import java.util.*;
import java.text.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.*;


import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.Activities.root.*;
import com.afrofx.code.anjesgf.Recyclers.Report.*;
import com.afrofx.code.anjesgf.ThemeSettings.*;
import com.afrofx.code.anjesgf.models.*;
import com.afrofx.code.anjesgf.*;

import java.util.ArrayList;
import java.util.List;


public class ReportUsuariosRegistadosActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ReportUsuariosRegistadosRecycler reportCliente1Recycler;
    private List<UserModel> userModelList ;
    private CompanyModel companyModel;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_usuarios_registados);

        Toolbar toolbar = findViewById(R.id.toolbarRelatorioUsuariosRegistados);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        android.widget.TextView dat1 = (android.widget.TextView) findViewById(R.id.dataRelatorio);
        View line = (View)findViewById(R.id.line);

        line.setBackgroundColor(Constant.color);

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String data1 = (mSharedPreference.getString("dataInicio", "2018-01-01"));
        String data2 = (mSharedPreference.getString("dataFim", "2018-12-02"));

        dat1.setText("Do dia "+data1+" Para o dia "+data2);

        listaUsuarios(data1, data2);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewUsuariosRegistados);
        recyclerView.setHasFixedSize(true);
        reportCliente1Recycler = new ReportUsuariosRegistadosRecycler(this, userModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reportCliente1Recycler);

    }


    public void listaUsuarios(String d1, String d2){
        userModelList = new ArrayList<>();
        Cursor dados = db.listaUsuarios(d1, d2);

        if(dados.getCount()==0) {
            Mensagem("Nenhum Registo Encontrado");
        }else{
            while(dados.moveToNext()){
                String nome = dados.getString(dados.getColumnIndex("usuario_nome"));
                long numero = Integer.parseInt(dados.getString(dados.getColumnIndex("usuario_numero")));
                String data = dados.getString(dados.getColumnIndex("usuario_data_reg"));
                String cargo = dados.getString(dados.getColumnIndex("cargo_nome"));
                UserModel userModel = new UserModel(data, nome, cargo, numero);
                userModelList.add(userModel);
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
            startActivity(new Intent(ReportUsuariosRegistadosActivity.this, MainScreenActivity.class));
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

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "E CONTA - Usuarios Registados");

                startActivity(Intent.createChooser(intentShareFile, "Enviar Ficheiro"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void imprimeVendas() {
        String timeStamp = new java.text.SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Econta_Relatorios");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            path = folder + "/Usuarios Registados" + timeStamp + ".pdf";
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

            Paragraph p0 = new Paragraph("Relatorio dos Usuarios Registados", chapterFont);
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

            PdfPCell dataVenda = new PdfPCell(new Phrase("Data de Registo", head));
            dataVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataVenda.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataVenda);

            PdfPCell contat = new PdfPCell(new Phrase("Nome do Usuario", head));
            contat.setHorizontalAlignment(Element.ALIGN_CENTER);
            contat.setBackgroundColor(BaseColor.GRAY);
            table.addCell(contat);

            PdfPCell nome = new PdfPCell(new Phrase("Cargo", head));
            nome.setHorizontalAlignment(Element.ALIGN_CENTER);
            nome.setBackgroundColor(BaseColor.GRAY);
            table.addCell(nome);

            PdfPCell age = new PdfPCell(new Phrase("Contacto", head));
            age.setHorizontalAlignment(Element.ALIGN_CENTER);
            age.setBackgroundColor(BaseColor.GRAY);
            table.addCell(age);

            table.setHeaderRows(1);

            for (int i = 0; i < userModelList.size(); i++) {
                table.addCell(userModelList.get(i).getUsuario_data_reg());
                table.addCell(userModelList.get(i).getUsuario_nome());
                table.addCell(userModelList.get(i).getCargo_nome() + "");
                table.addCell("+258 "+userModelList.get(i).getUsuario_numero());
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
