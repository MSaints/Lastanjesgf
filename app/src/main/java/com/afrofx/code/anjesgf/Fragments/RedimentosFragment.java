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
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.MyFooter;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.adpters.PageAdapters;
import com.afrofx.code.anjesgf.adpters.RendimentosRecyclerAdapter;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.RedimentosModel;
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

public class RedimentosFragment extends Fragment {

    private TextView rendimentoRecebidos, rendimentoPendentes;
    private List<RedimentosModel> redimentosModels;
    private DatabaseHelper db;
    private FloatingActionButton addRendime;
    private ViewPager viewPager;
    private PageAdapters pageAdapters;
    private CompanyModel companyModel;
    private String path;

    public RedimentosFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_redimentos, container, false);
        db = new DatabaseHelper(getContext());
        setHasOptionsMenu(true);
        rendimentoPendentes = (TextView) v.findViewById(R.id.rendimentoPendente);
        rendimentoRecebidos = (TextView) v.findViewById(R.id.rendimentoTotal);

        addRendime = (FloatingActionButton) v.findViewById(R.id.rendimento_adicionar);

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

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewRendimentos);
        recyclerView.setHasFixedSize(true);
        RendimentosRecyclerAdapter rendimentosRecyclerAdapter = new RendimentosRecyclerAdapter(getContext(), redimentosModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(rendimentosRecyclerAdapter);

        return v;
    }


    public void addRendimento() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptRendimento = li.inflate(R.layout.prompt_add_rendimento, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptRendimento);

        db = new DatabaseHelper(getContext());

        List<String> lables = db.listContas();

        final Spinner spinnerRendimentoConta = (Spinner) promptRendimento.findViewById(R.id.spinnerContaRendimento);
        final EditText rendimentoData = (EditText) promptRendimento.findViewById(R.id.txtdataRendimento);
        final EditText add_valor = (EditText) promptRendimento.findViewById(R.id.valorRendimento);
        final EditText rendimentoDescricao = (EditText) promptRendimento.findViewById(R.id.txtDescricaoRendimento);

        final CheckBox estadoRendimento = (CheckBox) promptRendimento.findViewById(R.id.recebidoRendimento);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lables);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRendimentoConta.setAdapter(adapter2);

        final Calendar myCalendar = Calendar.getInstance();
        final String[] data = {""};

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                rendimentoData.setText(sdf.format(myCalendar.getTime()));
                data[0] = sdf.format(myCalendar.getTime());
            }
        };

        int id_conta = 0;

        rendimentoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String val = add_valor.getText().toString().trim();
                final double valorRend = !val.equals("") ? Double.parseDouble(val) : 0.0;

                String descri = rendimentoDescricao.getText().toString().trim();

                final int idConta = db.idConta(spinnerRendimentoConta.getSelectedItem().toString());

                if (estadoRendimento.isChecked()) {
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 1);
                    db.registarValor(contaModel);

                    RedimentosModel redimentosModel = new RedimentosModel(descri, data[0], db.idOperacao());
                    db.registoRendimento(redimentosModel);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Rendimento Recebido")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    viewPager = (ViewPager) getActivity().findViewById(R.id.pageViewerDespesas);
                                    pageAdapters = new PageAdapters(getFragmentManager());
                                    pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
                                    pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
                                    viewPager.setAdapter(pageAdapters);

                                    viewPager.setCurrentItem(1);
                                    sDialog.dismiss();
                                    sDialog.setCancelable(false);
                                }
                            })
                            .show();
                } else {
                    ContaModel contaModel = new ContaModel(valorRend, idConta, 2);
                    db.registarValor(contaModel);
                    final RedimentosModel redimentosModel = new RedimentosModel(descri, data[0], db.idOperacao());
                    db.registoRendimento(redimentosModel);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Rendimento Adicionado")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    viewPager = (ViewPager) getActivity().findViewById(R.id.pageViewerDespesas);
                                    pageAdapters = new PageAdapters(getFragmentManager());
                                    pageAdapters.AddFragment(new DespesasFragment(), "Despesas");
                                    pageAdapters.AddFragment(new RedimentosFragment(), "Rendimentos");
                                    viewPager.setAdapter(pageAdapters);

                                    viewPager.setCurrentItem(1);
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
        db = new DatabaseHelper(getContext());

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
        if(id == R.id.partilhaVendas){
            imprimeVendas();
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(path);

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Mozê - Lista de Rendimentos");

                startActivity(Intent.createChooser(intentShareFile, "Enviar Ficheiro"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void imprimeVendas() {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy  HH_mm_ss").format(Calendar.getInstance().getTime());
        path = Environment.getExternalStorageDirectory() + "/Lista de Rendimentos " + timeStamp + ".pdf";

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

            Chunk chunk = new Chunk("Lista de Rendimentos", chapterFont);
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
                if (redimentosModels.get(i).getTipoOpe()== 2) {
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
}
