package com.afrofx.code.anjesgf.Activities.drawer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afrofx.code.anjesgf.Activities.root.MainScreenActivity;
import com.afrofx.code.anjesgf.DatabaseHelper;
import com.afrofx.code.anjesgf.R;
import com.afrofx.code.anjesgf.ThemeSettings.ThemeColor;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.DispesasModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class AnaliseActivity extends AppCompatActivity {

    private PieChart grafico1;
    private List<DispesasModel> dispesasModelList;
    private DatabaseHelper db;
    private List<StockModel> stockModelList;
    private List<StockModel> stockModelList2;

    private double saldo_caixa, saldo_bancos, saldo2, saldo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            new ThemeColor(this);
        }

        setContentView(R.layout.activity_analise);

        db = new DatabaseHelper(this);

        saldoTotal();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAnalises);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaMercadoriaVendida();

        ListaDespesa();

        pieChartConfig();
        double pago = 0, pendente = 0;

        ImageView estadoNegocioEmoji = findViewById(R.id.estadoNegocioEmoji);


        TextView estadoNegocio = findViewById(R.id.estadoNegocio);
        TextView saldoTotal = findViewById(R.id.saldoTotal);
        TextView saldoBancos = findViewById(R.id.saldoBancos);
        TextView saldoCaixa = findViewById(R.id.saldoCaixa);
        TextView contaAReceber = findViewById(R.id.contaAReceber);


        for (int i = 0; i < dispesasModelList.size(); i++) {
            if (dispesasModelList.get(i).getTipo_operacao() == 0) {
                pago = pago + dispesasModelList.get(i).getCusto_dispesa();
            }
        }
        double p = 0, p2 = 0, p3 = 0;

        for(int i = 0; i<stockModelList.size(); i++){
            p = p + (stockModelList.get(i).getProduto_preco_venda()* stockModelList.get(i).getProduto_quantidade());
        }

        for(int i = 0; i<stockModelList2.size(); i++){
            if (stockModelList2.get(i).getId_produto()==0) {
                p2 = p2 + (stockModelList2.get(i).getProduto_preco_venda() * stockModelList2.get(i).getProduto_quantidade());
            }else{
                p3 = p3 + (stockModelList2.get(i).getProduto_preco_venda() * stockModelList2.get(i).getProduto_quantidade());
            }
        }


        for (int i = 0; i < dispesasModelList.size(); i++) {
            if (dispesasModelList.get(i).getTipo_operacao() == 2) {
                pendente = pendente + dispesasModelList.get(i).getCusto_dispesa();
            }
            if (dispesasModelList.get(i).getTipo_operacao() == 0) {
                pago = pago + dispesasModelList.get(i).getCusto_dispesa();
            }
        }

        saldoTotal.setText(format("%,.2f",saldo_bancos+saldo_caixa+p2));
        saldoBancos.setText(format("%,.2f",saldo_bancos));
        saldoCaixa.setText(format("%,.2f",saldo_caixa));
        contaAReceber.setText(format("%,.2f",p2));


        if((pago+pendente)>(p2+p3)){
            estadoNegocioEmoji.setImageResource(R.drawable.ic_sad);
            estadoNegocio.setText("Péssimo");
        }if((pago+pendente)==(p2+p3)){
            estadoNegocioEmoji.setImageResource(R.drawable.ic_confused);
            estadoNegocio.setText("Alerta");
        }if((pago+pendente)<(p2+p3)){
            estadoNegocioEmoji.setImageResource(R.drawable.ic_smiling);
            estadoNegocio.setText("Excelente");
        }


    }
    public void listaMercadoriaVendida() {

        stockModelList2 = new ArrayList<>();

        Cursor dados = db.listaVendas();

        if (dados.getCount() == 0) {
        } else {
            while (dados.moveToNext()) {

                String dataVenda = dados.getString(dados.getColumnIndex("venda_data"));
                long numero = Long.parseLong(dados.getString(dados.getColumnIndex("venda_quantidade")));
                long idUsuario = Long.parseLong(dados.getString(dados.getColumnIndex("venda_estado")));
                String idCliente = dados.getString(dados.getColumnIndex("id_cliente"));

                long id_registo_operacao = Long.parseLong(dados.getString(dados.getColumnIndex("id_registo_venda")));
                double data = Double.parseDouble(dados.getString(dados.getColumnIndex("produto_preco_venda")));

                String nome = db.nomeCliente(Long.parseLong(idCliente));

                StockModel stockModel = new StockModel(numero, data, nome, dataVenda, idUsuario, id_registo_operacao);
                stockModelList2.add(stockModel);


            }
        }
    }



    public void pieChartConfig(){
        final int[] MY_COLORS = {Color.rgb(192,0,0), Color.rgb(255,0,0), Color.rgb(255,192,0),
                Color.rgb(127,127,127), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        grafico1 = (PieChart) findViewById(R.id.grafico1);

        listaMaisVendida();

        List<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i < stockModelList.size(); i++){
            entries.add(new PieEntry((float) stockModelList.get(i).getProduto_quantidade(),  stockModelList.get(i).getProduto_nome()));
        }
       /* entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));*/

        PieDataSet set = new PieDataSet(entries,"");
        for(int c: MY_COLORS) colors.add(c);
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(set);

        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        grafico1.getDescription().setEnabled(false);

        grafico1.setData(data);
        grafico1.invalidate(); // refresh
        grafico1.animateY(5000);

        grafico1.setDrawHoleEnabled(true);

        grafico1.setCenterText("Performance de Vendas");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(AnaliseActivity.this, MainScreenActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void saldoTotal(){

        db = new DatabaseHelper(this);

        Cursor cursor = db.listOperacoes();

        TextView sal = (TextView)findViewById(R.id.saldoCaixa);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nao Contas", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor != null && cursor.getString(1) != null){
                    int id_conta = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_conta")));
                    double cont_saldo = Double.parseDouble(cursor.getString(2));
                    String nome_conta= cursor.getString(1);

                    if(nome_conta.equals("Caixa")){
                        saldo_caixa = cont_saldo;
                    }else{
                        saldo_bancos = cont_saldo + saldo_bancos;
                    }
                }
            }
        }
    }

    public void ListaDespesa() {
        db = new DatabaseHelper(this);

        dispesasModelList = new ArrayList<>();

        Cursor cursor = db.listaDispesas();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Sem Despesas Registadas", Toast.LENGTH_SHORT);
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

                DispesasModel dispesasModel = new DispesasModel(id_despesa, estado, valor, descricao, categoria, data, nomeConta, id_op);

                dispesasModelList.add(dispesasModel);
            }
        }
    }
}
