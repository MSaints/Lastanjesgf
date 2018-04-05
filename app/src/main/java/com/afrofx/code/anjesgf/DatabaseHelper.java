package com.afrofx.code.anjesgf;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.afrofx.code.anjesgf.models.CategoriaModel;
import com.afrofx.code.anjesgf.models.ClienteModel;
import com.afrofx.code.anjesgf.models.CompanyModel;
import com.afrofx.code.anjesgf.models.ContaModel;
import com.afrofx.code.anjesgf.models.DispesasModel;
import com.afrofx.code.anjesgf.models.FornecedorModel;
import com.afrofx.code.anjesgf.models.RedimentosModel;
import com.afrofx.code.anjesgf.models.RegistoVendaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UnidadeModel;
import com.afrofx.code.anjesgf.models.UserModel;
import com.afrofx.code.anjesgf.models.VendasModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    /*================Nome da Base de Dados============================*/
    private static final String DB_NAME = "moze.db";

    private Cursor cursor;

    /*==================Tabelas a Serem Criadas======================*/
    private static final String ENDERECO_TABLE = "moze_endereco";
    private static final String USUARIO_TABLE = "moze_usuario";
    private static final String CARGO_TABLE = "moze_cargo";
    private static final String VENDA_TABLE = "moze_venda";
    private static final String REGISTO_VENDA_TABLE = "moze_registo_venda";
    private static final String PRODUTO_TABLE = "moze_produto";
    private static final String PRODUTO_CATEGORIA_TABLE = "moze_produto_categoria";
    private static final String FORNECEDOR_TABLE = "moze_fornecedor";
    private static final String PRODUTO_UNIDADE_TABLE = "moze_produto_unidade";
    private static final String REGISTO_CONTA_TABLE = "moze_registo_conta";
    private static final String REGISTO_OPERACOES_TABLE = "moze_registo_operacao";
    private static final String CLIENTE_TABLE = "moze_cliente";
    private static final String DESPESAS_TABLE = "moze_despesas";
    private static final String DESPESAS_CATEGORIA_TABLE = "moze_despesas_categoria";
    private static final String RENDIMENTOS_TABLE = "moze_rendimentos";
    private static final String EMPRESA_TABLE = "moze_empresa";


    /*==================COLUNAS DAS TABELAS DA BD MOZE======================*/
    //==========COLUNAS USUARIO_TABLE
    private static final String US_COL_1 = "id_usuario";
    private static final String US_COL_2 = "id_cargo";
    private static final String US_COL_3 = "id_endereco";
    private static final String US_COL_4 = "usuario_nome";
    private static final String US_COL_5 = "usuario_numero";
    private static final String US_COL_6 = "usuario_email";
    private static final String US_COL_7 = "usuario_password";
    private static final String US_COL_8 = "usuario_data_reg";
    private static final String US_COL_9 = "usuario_imagem";

    //==========COLUNAS CARGO_TABLE
    private static final String CA_COL_1 = "id_cargo";
    private static final String CA_COL_2 = "cargo_nome";

    //==========COLUNAS ENDERECO_TABLE
    private static final String EN_COL_1 = "id_endereco";
    private static final String EN_COL_2 = "endereco_cidade";
    private static final String EN_COL_3 = "endereco_provincia";
    private static final String EN_COL_4 = "endereco_bairro";

    //==========COLUNAS PRODUTOS_TABLE
    private static final String PR_COL_1 = "id_produto";
    private static final String PR_COL_2 = "id_categoria";
    private static final String PR_COL_3 = "id_unidade";
    private static final String PR_COL_4 = "id_fornecedor";
    private static final String PR_COL_5 = "produto_nome";
    private static final String PR_COL_6 = "produto_quantidade";
    private static final String PR_COL_7 = "produto_quanti_minima";
    private static final String PR_COL_8 = "id_registo_operacao";
    private static final String PR_COL_9 = "produto_preco_venda";
    private static final String PR_COL_10 = "produto_data_registo";
    private static final String PR_COL_11 = "produto_validade";

    //==========COLUNAS PRODUTO_CATEGORIA_TABLE
    private static final String PC_COL_1 = "id_categoria";
    private static final String PC_COL_2 = "categoria_nome";

    //==========COLUNAS PRODUTO_UNIDADE_TABLE
    private static final String PU_COL_1 = "id_unidade";
    private static final String PU_COL_2 = "unidade_nome";

    //==========COLUNAS REGISTO_VENDAS_TABLE
    private static final String RV_COL_1 = "id_registo_venda";
    private static final String RV_COL_2 = "id_cliente";
    private static final String RV_COL_3 = "id_usuario";
    private static final String RV_COL_4 = "id_registo_operacao";
    private static final String RV_COL_5 = "venda_desconto";
    private static final String RV_COL_6 = "venda_estado";
    private static final String RV_COL_7 = "venda_data";
    private static final String RV_COL_8 = "venda_data_expiracao";

    //==========COLUNAS REGISTO_CONTA_TABLE
    private static final String RC_COL_1 = "id_conta";
    private static final String RC_COL_2 = "conta_nome";

    //==========COLUNAS REGISTO_OPERACOES_TABLE
    private static final String RO_COL_1 = "id_registo_operacao";
    private static final String RO_COL_2 = "id_conta";
    private static final String RO_COL_3 = "tipo_operacao";
    private static final String RO_COL_4 = "operacao_valor";
    private static final String RO_COL_5 = "operacao_data";


    //==========COLUNAS VENDAS_TABLE
    private static final String VE_COL_1 = "id_venda";
    private static final String VE_COL_2 = "id_produto";
    private static final String VE_COL_3 = "id_registo_venda";
    private static final String VE_COL_4 = "venda_quantidade";
    private static final String VE_COL_5 = "venda_preco";

    //==========COLUNAS FORNECEDOR_TABLE
    private static final String FO_COL_1 = "id_fornecedor";
    private static final String FO_COL_2 = "id_endereco";
    private static final String FO_COL_3 = "fornecedor_nome";
    private static final String FO_COL_4 = "fornecedor_contacto";
    private static final String FO_COL_5 = "fornecedor_email";
    private static final String FO_COL_6 = "fornecedor_tipo";
    private static final String FO_COL_7 = "fornecedor_registo";

    //==========COLUNAS CLIENTES_TABLE=====================*/
    private static final String CL_COL_1 = "id_cliente";
    private static final String CL_COL_2 = "id_endereco";
    private static final String CL_COL_3 = "cliente_nome";
    private static final String CL_COL_4 = "cliente_cell";
    private static final String CL_COL_5 = "cliente_email";
    private static final String CL_COL_6 = "cliente_data_registo";


    //==========COLUNAS CLIENTES_TABLE=====================*/
    private static final String EI_COL_1 = "id_empresa";
    private static final String EI_COL_2 = "id_usuario";
    private static final String EI_COL_3 = "nome_empresa";
    private static final String EI_COL_4 = "localidade_empresa";
    private static final String EI_COL_5 = "nuit_empresa";
    private static final String EI_COL_6 = "email_empresa";
    private static final String EI_COL_7 = "dona_empresa";
    private static final String EI_COL_8 = "numero_empresa";

    //==========COLUNAS DESPESA_TABLE=====================*/
    private static final String DE_COL_1 = "id_despesa";
    private static final String DE_COL_2 = "id_categoria_despesa";
    private static final String DE_COL_3 = "id_registo_operacao";
    private static final String DE_COL_4 = "despesa_descricao";
    private static final String DE_COL_5 = "despesa_data";
    private static final String DE_COL_6 = "despesa_data_registo";

    //==========COLUNAS CATEGORIA_DESPESA_TABLE=====================*/
    private static final String CD_COL_1 = "id_categoria_despesa";
    private static final String CD_COL_2 = "despesa_categoria_nome";

    //==========COLUNAS RENDIMENTOS_TABLE=====================*/
    private static final String RE_COL_1 = "id_rendimento";
    private static final String RE_COL_2 = "id_registo_operacao";
    private static final String RE_COL_3 = "rendimento_descricao";
    private static final String RE_COL_4 = "rendimento_data";
    private static final String RE_COL_5 = "rendimento_data_registo";


    /*==================Criacao da tabela Rendimentos na bd=====================*/
    private static final String query_redimentos = "CREATE TABLE "
            + RENDIMENTOS_TABLE + "(" + RE_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + RE_COL_2 + " INTEGER REFERENCES " + REGISTO_OPERACOES_TABLE + "(" + RO_COL_1 + ") NOT NULL," +
            RE_COL_3 + " VARCHAR NOT NULL, " + RE_COL_4 + " DATE NOT NULL, " + RE_COL_5 + " DATE DEFAULT (CURRENT_TIMESTAMP))";


    /*==================Criacao da tabela Despesas na bd=====================*/
    private static final String query_despesas = "CREATE TABLE "
            + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + CD_COL_2 + " VARCHAR NOT NULL)";

    /*==================Criacao da tabela Categoria despesa na bd=====================*/
    private static final String query_categoria_despesa = "CREATE TABLE "
            + DESPESAS_TABLE + "(" + DE_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            DE_COL_2 + " INTEGER REFERENCES " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_1 + ") NOT NULL," +
            DE_COL_3 + " INTEGER REFERENCES " + REGISTO_OPERACOES_TABLE + "(" + RO_COL_1 + ") NOT NULL," +
            DE_COL_4 + " VARCHAR NOT NULL, " + DE_COL_5 + " DATE NOT NULL, " +
            DE_COL_6 + " DATE DEFAULT (CURRENT_TIMESTAMP))";

    /*==================Criacao da tabela cargos na bd=====================*/
    private static final String query_cargos = "CREATE TABLE "
            + CARGO_TABLE + "(" + CA_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + CA_COL_2 + " VARCHAR NOT NULL DEFAULT Administrador)";

    /*==================Criacao da tabela localidade na bd=====================*/
    private static final String query_endereco = "CREATE TABLE "
            + ENDERECO_TABLE + "(" + EN_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + EN_COL_2 +
            " VARCHAR ," + EN_COL_3 + " VARCHAR ," + EN_COL_4 + " VARCHAR)";

    /*==================Criacao da tabela Usuarios na bd=====================*/
    private static final String query_usuario = "CREATE TABLE " + USUARIO_TABLE + "("
            + US_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + US_COL_2 + " INTEGER REFERENCES " + CARGO_TABLE +
            "(" + CA_COL_1 + ") DEFAULT (1) NOT NULL," + US_COL_3 + " INTEGER REFERENCES " + ENDERECO_TABLE +
            "(" + EN_COL_1 + ") DEFAULT (0) NOT NULL," + US_COL_4 + " VARCHAR NOT NULL," + US_COL_5 + " INTEGER NOT NULL UNIQUE,"
            + US_COL_6 + "  VARCHAR," + US_COL_7 + " INTEGER NOT NULL, " + US_COL_8 + " DATE DEFAULT (CURRENT_TIMESTAMP)," + US_COL_9 + " BLOB)";

    /*==================Criacao da tabela Cliente na bd=====================*/
    private static final String query_cliente = "CREATE TABLE " + CLIENTE_TABLE + "("
            + CL_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + CL_COL_2 + " INTEGER REFERENCES " + ENDERECO_TABLE
            + "(" + EN_COL_1 + ")," + CL_COL_3 + " VARCHAR NOT NULL," + CL_COL_4 + " INTEGER," + CL_COL_5 + " VARCHAR,"
            + CL_COL_6 + " DATE DEFAULT (CURRENT_TIMESTAMP))";

    /*==================Criacao da tabela produto categoria na bd=====================*/
    private static final String query_produto_categoria = "CREATE TABLE " + PRODUTO_CATEGORIA_TABLE + "("
            + PC_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + PC_COL_2 + " VARCHAR NOT NULL)";

    /*==================Criacao da tabela produto unidade na bd=====================*/
    private static final String query_produto_unidade = "CREATE TABLE " + PRODUTO_UNIDADE_TABLE + "("
            + PU_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + PU_COL_2 + " VARCHAR NOT NULL)";

    /*==================Criacao da tabela fornecedor na bd=====================*/
    private static final String query_fornecedor = "CREATE TABLE " + FORNECEDOR_TABLE + "("
            + FO_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + FO_COL_2 + " INTEGER REFERENCES " + ENDERECO_TABLE
            + "(" + EN_COL_1 + ")," + FO_COL_3 + " VARCHAR NOT NULL," + FO_COL_4 + " INTEGER," + FO_COL_5 + " VARCHAR," +
            FO_COL_6 + " VARCHAR," + FO_COL_7 + " DATE DEFAULT (CURRENT_TIMESTAMP))";

    /*==================Criacao da tabela PRODUTOS na bd=====================*/
    private static final String query_produto = "CREATE TABLE " + PRODUTO_TABLE + "("
            + PR_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            PR_COL_2 + " INTEGER REFERENCES " + PRODUTO_CATEGORIA_TABLE + "(" + PC_COL_1 + ")DEFAULT 0," +
            PR_COL_3 + " INTEGER REFERENCES " + PRODUTO_UNIDADE_TABLE + "(" + PU_COL_1 + ")DEFAULT 0," +
            PR_COL_4 + " INTEGER REFERENCES " + FORNECEDOR_TABLE + "(" + FO_COL_1 + ")DEFAULT 0," +
            PR_COL_5 + " VARCHAR NOT NULL," + PR_COL_6 + " REAL," + PR_COL_7 + " REAL," +
            PR_COL_8 + " INTEGER REFERENCES " + REGISTO_OPERACOES_TABLE + "(" + RO_COL_1 + ")," + PR_COL_9 + " REAL ," + PR_COL_10 + " DATE DEFAULT (CURRENT_TIMESTAMP) ," + PR_COL_11 + " DATE)";

    /*==================Criacao da tabela REGISTO VENDA na bd=====================*/
    private static final String query_registo_venda = "CREATE TABLE " + REGISTO_VENDA_TABLE + "("
            + RV_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            RV_COL_2 + " INTEGER REFERENCES " + CLIENTE_TABLE + "(" + CL_COL_1 + ") DEFAULT Anonimo," +
            RV_COL_3 + " INTEGER REFERENCES " + USUARIO_TABLE + "(" + US_COL_1 + ") NOT NULL," +
            RV_COL_4 + " INTEGER REFERENCES " + REGISTO_OPERACOES_TABLE + "(" + RO_COL_1 + ")," +
            RV_COL_5 + " REAL," + RV_COL_6 + " INTEGER NOT NULL," + RV_COL_7 + " DATE DEFAULT (CURRENT_TIMESTAMP)," +
            RV_COL_8 + " DATE)";

    /*==================Criacao da tabela VENDA na bd=====================*/
    private static final String query_venda = "CREATE TABLE " + VENDA_TABLE + "("
            + VE_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            VE_COL_2 + " INTEGER REFERENCES " + PRODUTO_TABLE + "(" + PR_COL_1 + ") NOT NULL," +
            VE_COL_3 + " INTEGER REFERENCES " + REGISTO_VENDA_TABLE + "(" + RV_COL_1 + ") NOT NULL," +
            VE_COL_4 + " REAL NOT NULL," + VE_COL_5 + " REAL NOT NULL)";

    /*==================Criacao da tabela registo operacoes na bd=====================*/
    private static final String query_registo_operecaoes = "CREATE TABLE " + REGISTO_OPERACOES_TABLE + "("
            + RO_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            RO_COL_2 + " INTEGER REFERENCES " + REGISTO_CONTA_TABLE + "(" + RC_COL_1 + ") NOT NULL," +
            RO_COL_3 + " INTEGER NOT NULL," + RO_COL_4 + " REAL NOT NULL," + RO_COL_5 + " DATE DEFAULT (CURRENT_TIMESTAMP))";

    /*==================Criacao da tabela registo operacoes na bd=====================*/
    private static final String query_empresa = "CREATE TABLE " + EMPRESA_TABLE + "("
            + EI_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            EI_COL_2 + " INTEGER REFERENCES " + USUARIO_TABLE + "(" + US_COL_1 + ") NOT NULL," +
            EI_COL_3 + " VARCHAR," + EI_COL_4 + " VARCHAR," + EI_COL_5 + " INTEGER, " +
            EI_COL_6 + " VARCHAR," + EI_COL_7 + " VARCHAR," + EI_COL_8 + " INTEGER)";


    /*==================Criacao da tabela registo venda na bd=====================*/
    private static final String query_registo_conta = "CREATE TABLE " + REGISTO_CONTA_TABLE + "("
            + RC_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            RC_COL_2 + " VARCHAR NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_cargos);
        /*==================Criacao da tabela endereco na bd=====================*/
        db.execSQL(query_endereco);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_usuario);
        /*==================Criacao da tabela Cliente na bd=====================*/
        db.execSQL(query_cliente);
        /*==================Criacao da tabela Produto Categoria na bd=====================*/
        db.execSQL(query_produto_categoria);
        /*==================Criacao da tabela Produto na bd=====================*/
        db.execSQL(query_produto);
        /*==================Criacao da tabela Produto Unidade na bd=====================*/
        db.execSQL(query_produto_unidade);
        /*==================Criacao da tabela Venda na bd=====================*/
        db.execSQL(query_venda);
        /*==================Criacao da tabela Registo de Venda na bd=====================*/
        db.execSQL(query_registo_venda);
        /*==================Criacao da tabela Conta na bd=====================*/
        db.execSQL(query_registo_conta);
        /*==================Criacao da tabela Fornecedor na bd=====================*/
        db.execSQL(query_fornecedor);
        /*==================Criacao da tabela Operacoes na bd=====================*/
        db.execSQL(query_registo_operecaoes);
         /*==================Criacao da tabela Categorias de Despesas na bd=====================*/
        db.execSQL(query_categoria_despesa);
        /*==================Criacao da tabela Despesas na bd=====================*/
        db.execSQL(query_despesas);
        /*==================Criacao da tabela Rendimentos na bd=====================*/
        db.execSQL(query_redimentos);
        /*==================Criacao da tabela Rendimentos na bd=====================*/
        db.execSQL(query_empresa);

        /*================Cargos de Usuarios=============================*/
        db.execSQL("INSERT INTO " + CARGO_TABLE + "(" + CA_COL_2 + ") VALUES('Administrador')");
        db.execSQL("INSERT INTO " + CARGO_TABLE + "(" + CA_COL_2 + ") VALUES('Colaborador')");


        /*================Contas=============================*/
        db.execSQL("INSERT INTO " + REGISTO_CONTA_TABLE + "(" + RC_COL_2 + ") VALUES('BCI')");
        db.execSQL("INSERT INTO " + REGISTO_CONTA_TABLE + "(" + RC_COL_2 + ") VALUES('Caixa')");
        db.execSQL("INSERT INTO " + REGISTO_CONTA_TABLE + "(" + RC_COL_2 + ") VALUES('M-PESA')");
        db.execSQL("INSERT INTO " + REGISTO_CONTA_TABLE + "(" + RC_COL_2 + ") VALUES('M-KESH')");
        db.execSQL("INSERT INTO " + REGISTO_CONTA_TABLE + "(" + RC_COL_2 + ") VALUES('Moza Banco')");
        db.execSQL("INSERT INTO " + REGISTO_CONTA_TABLE + "(" + RC_COL_2 + ") VALUES('MIllenium BIM')");


        /*================Categorias de Despesas=============================*/
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('CREDELEC')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('TRANSPORTE')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('ALIMENTOS')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('SALARIOS')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('AGUA')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('RENDA')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('SAUDE')");
        db.execSQL("INSERT INTO " + DESPESAS_CATEGORIA_TABLE + "(" + CD_COL_2 + ") VALUES('OUTRA')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CARGO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ENDERECO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USUARIO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENTE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUTO_CATEGORIA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUTO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTO_VENDA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + VENDA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTO_CONTA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTO_OPERACOES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUTO_UNIDADE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FORNECEDOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DESPESAS_CATEGORIA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DESPESAS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RENDIMENTOS_TABLE);
        onCreate(db);
    }

    /*===========================================INICIO DOS METODOS DO USUARIO(REGISTO/LOGIN)======================================*/
    /* ======================Regista um novo usuario================================*/
    public void inserirUser(UserModel userModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(US_COL_4, userModel.getUsuario_nome());
        contentValues.put(US_COL_5, userModel.getUsuario_numero());
        contentValues.put(US_COL_6, userModel.getUsuario_email());
        contentValues.put(US_COL_7, userModel.getUsuario_password());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(USUARIO_TABLE, null, contentValues);

        db.close();
    }

    public String nomeUsuario(int x) {
        String sqlQuery = "SELECT " + US_COL_4 + " FROM " + USUARIO_TABLE + " WHERE " + US_COL_1 + " = " + x;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        String n = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            n = cursor.getString(cursor.getColumnIndex(US_COL_4));
            cursor.close();
        }
        db.close();
        return n;
    }

    /* ======================procura o usuario na hora do registo Usuario================================*/
    public UserModel procurar(int userNumber) {
        String sqlQuery = "SELECT * FROM " + USUARIO_TABLE + " WHERE " + US_COL_5 + " = " + userNumber;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        UserModel userModel = new UserModel();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            userModel.setId_usuario(Integer.parseInt(cursor.getString(cursor.getColumnIndex(US_COL_1))));
            cursor.close();
        } else {
            userModel = null;
        }

        db.close();
        return userModel;
    }

    /* ======================Valida a existencia de dados na BD para acessar o sistema================================*/
    public UserModel userLogin(int userNumber, int userPin) {
        String sqlQuery = "SELECT * FROM " + USUARIO_TABLE + " WHERE " + US_COL_5 + " = " + userNumber + " AND " + US_COL_7 + " = " + userPin + "";

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        UserModel userModel = new UserModel();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            userModel.setId_usuario(Integer.parseInt(cursor.getString(cursor.getColumnIndex(US_COL_1))));
            userModel.setId_cargo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(US_COL_2))));
            userModel.setId_endereco(Integer.parseInt(cursor.getString(cursor.getColumnIndex(US_COL_3))));
            userModel.setUsuario_nome((cursor.getString(cursor.getColumnIndex(US_COL_4))));
            userModel.setUsuario_numero(Integer.parseInt(cursor.getString(cursor.getColumnIndex(US_COL_5))));
            userModel.setUsuario_email(cursor.getString(cursor.getColumnIndex(US_COL_6)));
            userModel.setUsuario_password(Integer.parseInt(cursor.getString(cursor.getColumnIndex(US_COL_7))));
            userModel.setUsuario_data_reg((cursor.getString(cursor.getColumnIndex(US_COL_8))));
            userModel.setUsuario_imagem((cursor.getBlob(cursor.getColumnIndex(US_COL_9))));

            cursor.close();
        } else {
            userModel = null;
        }

        db.close();

        return userModel;
    }

    /* ======================Verifica a existencia do produto no sistema================================*/
    public StockModel procuraProduto(String nome) {
        String sqlQuery = "SELECT * FROM " + PRODUTO_TABLE + " WHERE " + PR_COL_5 + " = '" + nome + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        StockModel stockModel = new StockModel();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stockModel.setId_produto(Integer.parseInt(cursor.getString(0)));
            cursor.close();
        } else {
            stockModel = null;
        }

        db.close();

        return stockModel;
    }

    /*    =============Lista Todos Os Produtos====================*/
    public Cursor listProduto() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_TABLE + " NATURAL JOIN " + PRODUTO_CATEGORIA_TABLE + " ORDER BY " + PR_COL_5;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    =============Lista Todos Os Produtos====================*/
    public Cursor listProdutoRelatorio(String d1, String d2) {
        String sqlQuery = "SELECT * FROM "+PRODUTO_CATEGORIA_TABLE+" NATURAL JOIN "+ PRODUTO_TABLE + " WHERE " + PR_COL_10 +
                " BETWEEN '" + d1 + " 00:00:00 '" + " AND '" + d2 + " 24:00:00 '";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    =============Lista Fornecedores====================*/
    public Cursor listFornecedor() {
        String sqlQuery = "SELECT * FROM " + FORNECEDOR_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    =============Lista Categorias de Produtos====================*/
    public Cursor listCategoria() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_CATEGORIA_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    ==============Lista Unidades de Produtos====================*/
    public Cursor listUnidade() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_UNIDADE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    ==============Actualiza Quantidade do Produto====================*/
    public boolean updateQuatidade(String nome, double quantidade) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PR_COL_6, quantidade);

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(PRODUTO_TABLE, contentValues, PR_COL_5 + " = '" + nome + "'", null);

        db.close();

        return true;
    }

    /*    ==============Regista Produtos====================*/
    public void inserirProduto(StockModel stockModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PR_COL_2, stockModel.getId_categoria());
        contentValues.put(PR_COL_3, stockModel.getId_unidade());
        contentValues.put(PR_COL_4, stockModel.getId_fornecedor());
        contentValues.put(PR_COL_5, stockModel.getProduto_nome());
        contentValues.put(PR_COL_6, stockModel.getProduto_quantidade());
        contentValues.put(PR_COL_7, stockModel.getProduto_quanti_minima());
        contentValues.put(PR_COL_8, stockModel.getId_registo_operacao());
        contentValues.put(PR_COL_9, stockModel.getProduto_preco_venda());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(PRODUTO_TABLE, null, contentValues);

        db.close();
    }

    /*    ==============ID da categoria de produtos apartir do nome====================*/
    public int procuraCategoria(String categoria) {
        String sqlQuery = "SELECT " + PC_COL_1 + " FROM " + PRODUTO_CATEGORIA_TABLE + " WHERE " + PC_COL_2 + " = '" + categoria + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        int id_categoria = 0;

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_categoria = cursor.getInt(0);
        }

        return id_categoria;
    }

    /*    ==============Verifica a Existencia do Fornecedor====================*/
    public FornecedorModel procurarForndecedor(String fornecedor) {
        String sqlQuery = "SELECT " + FO_COL_1 + " FROM " + FORNECEDOR_TABLE + " WHERE " + FO_COL_3 + " = '" + fornecedor + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        FornecedorModel fornecedorModel = new FornecedorModel();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            fornecedorModel.setId_fornecedor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FO_COL_1))));

            cursor.close();
        } else {
            fornecedorModel = null;
        }

        db.close();

        return fornecedorModel;
    }

    /*    ==============Procura Categoria====================*/
    public CategoriaModel procurarCategoria(String nome) {
        String sqlQuery = "SELECT * FROM " + PRODUTO_CATEGORIA_TABLE + " WHERE " + PC_COL_2 + " = '" + nome + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        CategoriaModel categoriaModel = new CategoriaModel();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            categoriaModel.setCategoria(cursor.getString(cursor.getColumnIndex(PC_COL_2)));
            categoriaModel.setCategoria_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PC_COL_1))));

            cursor.close();
        } else {
            categoriaModel = null;
        }

        db.close();

        return categoriaModel;
    }

    /*    ==============Regista Categoria de Produto====================*/
    public void registarCategoria(CategoriaModel categoriaModel) {
        ContentValues cv = new ContentValues();

        cv.put(PC_COL_2, categoriaModel.getCategoria());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(PRODUTO_CATEGORIA_TABLE, null, cv);

        db.close();
    }

    /*    ==============Seleciona Unidade de Produto====================*/
    public UnidadeModel procurarUnidade(String nome) {
        String sqlQuery = "SELECT * FROM " + PRODUTO_UNIDADE_TABLE + " WHERE " + PU_COL_2 + " = '" + nome + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        UnidadeModel unidadeModel = new UnidadeModel();

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();

            unidadeModel.setUnidadee_nome(cursor.getString(cursor.getColumnIndex(PU_COL_2)));
            unidadeModel.setId_unidade(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PU_COL_1))));

            cursor.close();
        } else {
            unidadeModel = null;
        }

        db.close();

        return unidadeModel;
    }

    /*    ==============Regista Unidade de Produto====================*/
    public void registarUnidade(UnidadeModel unidadeModel) {
        ContentValues cv = new ContentValues();

        cv.put(PU_COL_2, unidadeModel.getUnidadee_nome());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(PRODUTO_UNIDADE_TABLE, null, cv);

        db.close();
    }

    /*    ==============tras ID Unidade de Produto====================*/
    public int procuraUnidade(String unidade) {
        String sqlQuery = "SELECT " + PU_COL_1 + " FROM " + PRODUTO_UNIDADE_TABLE + " WHERE " + PU_COL_2 + " = '" + unidade + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        int id_unidade = 0;

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_unidade = cursor.getInt(0);
        }
        return id_unidade;
    }

    /*    =============Procura ID do fornecedor apartir do NOme====================*/
    public int procuraFornecedor(String fornecedor) {
        String sqlQuery = "SELECT " + FO_COL_1 + " FROM " + FORNECEDOR_TABLE + " WHERE " + FO_COL_3 + " = '" + fornecedor + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        int id_fornecedor = 0;

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_fornecedor = cursor.getInt(0);
        }
        return id_fornecedor;
    }

    /*    =============Elimina Produto====================*/
    public boolean eliminarProduto(String nome) {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();

        int query = db.delete(PRODUTO_TABLE, PR_COL_5 + " = '" + nome + "'", null);

        if (query > 0) {
            result = true;
        }

        db.close();

        return result;
    }

    /*    =============Actualiza Produto====================*/
    public boolean updateProduto(String nome_conf, String nome, double preco_compra, double preco_venda) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PR_COL_5, nome);
        contentValues.put(PR_COL_8, preco_compra);
        contentValues.put(PR_COL_9, preco_venda);

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(PRODUTO_TABLE, contentValues, PR_COL_5 + " = '" + nome_conf + "'", null);

        db.close();

        return true;
    }

    /*    =============Regista Fornecedor====================*/
    public void registarFornecedor(FornecedorModel fornecedorModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FO_COL_3, fornecedorModel.getFornecedor_nome());
        contentValues.put(FO_COL_4, fornecedorModel.getFornecedor_contacto());
        contentValues.put(FO_COL_5, fornecedorModel.getFornecedor_email());
        contentValues.put(FO_COL_6, fornecedorModel.getFornecedor_tipo());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(FORNECEDOR_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Regista Cliente====================*/
    public void registarCliente(ClienteModel clienteModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CL_COL_3, clienteModel.getNomeCliente());
        contentValues.put(CL_COL_4, clienteModel.getNumeroCliente());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(CLIENTE_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Tras a Lista de Todos os Clientes====================*/
    public Cursor listCliente() {
        String sqlQuery = "SELECT * FROM " + CLIENTE_TABLE + " ORDER BY " + CL_COL_3;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    =============Regista Valor na Conta====================*/
    public void registarValor(ContaModel contaModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(RO_COL_2, contaModel.getId_conta());
        contentValues.put(RO_COL_3, contaModel.getId_operacao());
        contentValues.put(RO_COL_4, contaModel.getSaldoConta());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(REGISTO_OPERACOES_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Tras o saldo total de uma determinada conta====================*/
    public double SaldoTotal(String nome) {
        double saldo = 0;

        String sqlQuery = "SELECT SUM(CASE WHEN " + RO_COL_3 + " = '" + 1 + "'" +
                "THEN " + REGISTO_OPERACOES_TABLE + "." + RO_COL_4 + " ELSE 0 END)- SUM(CASE WHEN " + RO_COL_3 + "='" + 0 + "'" +
                "THEN " + REGISTO_OPERACOES_TABLE + "." + RO_COL_4 + " ELSE 0 END) FROM " + REGISTO_CONTA_TABLE + " NATURAL JOIN " +
                REGISTO_OPERACOES_TABLE + " WHERE " + REGISTO_CONTA_TABLE + "." + RC_COL_2 + " = '" + nome +
                "' GROUP BY " + REGISTO_CONTA_TABLE + "." + RC_COL_2;

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            saldo = cursor.getInt(0);
        }

        return saldo;
    }

    /*    =============Lista as contas e Seus Saldos====================*/
    public Cursor listOperacoes() {
        String sqlQuery = "SELECT " + REGISTO_CONTA_TABLE + "." + RC_COL_1 + ", " + REGISTO_CONTA_TABLE + "." + RC_COL_2 +
                ", SUM(CASE WHEN " + RO_COL_3 + " = '" + 1 + "'" +
                "THEN " + REGISTO_OPERACOES_TABLE + "." + RO_COL_4 + " ELSE 0 END)- SUM(CASE WHEN " + RO_COL_3 + "='" + 0 + "'" +
                "THEN " + REGISTO_OPERACOES_TABLE + "." + RO_COL_4 + " ELSE 0 END) FROM " + REGISTO_CONTA_TABLE + " NATURAL JOIN " +
                REGISTO_OPERACOES_TABLE + " GROUP BY " + REGISTO_CONTA_TABLE + "." + RC_COL_2;

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;

    }

    /*    =============Tras o ID da Conta Apartir do Nome====================*/
    public int idConta(String Nome) {
        String sqlQuery = "SELECT " + RC_COL_1 + " FROM " + REGISTO_CONTA_TABLE + " WHERE " + RC_COL_2 + " = '" + Nome + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        int id_conta = 0;

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_conta = cursor.getInt(0);
        }

        return id_conta;
    }

    /*    =============Tras o ID do produto Apartir do Nome====================*/
    public int idProduto(String Nome) {
        String sqlQuery = "SELECT " + PR_COL_1 + " FROM " + PRODUTO_TABLE + " WHERE " + PR_COL_5 + " = '" + Nome + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        int id_produto = 0;

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_produto = cursor.getInt(0);
        }
        return id_produto;
    }

    /*    =============Tras o nome do produto Apartir do ID====================*/
    public String nomeProduto(int idPro) {
        String sqlQuery = "SELECT " + PR_COL_5 + " FROM " + PRODUTO_TABLE + " WHERE " + PR_COL_1 + " = '" + idPro + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        String nomeProduto = "";

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            nomeProduto = cursor.getString(4);
        }

        return nomeProduto;
    }

    /*    =============Lista de Todas as Contas====================*/
    public List<String> listContas() {
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + REGISTO_CONTA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return labels;
    }

    /*    =============Lista de Todas as Contas Que ja foram operadas====================*/
    public List<String> listContas2() {
        List<String> labels = new ArrayList<>();

        String selectQuery = "SELECT " + REGISTO_CONTA_TABLE + "." + RC_COL_1 + ", " + REGISTO_CONTA_TABLE +
                "." + RC_COL_2 + ", SUM(CASE WHEN " + RO_COL_3 + " = '" + 1 + "'" + "THEN " + REGISTO_OPERACOES_TABLE +
                "." + RO_COL_4 + " ELSE 0 END)- SUM(CASE WHEN " + RO_COL_3 + "='" + 0 + "'" + "THEN " + REGISTO_OPERACOES_TABLE +
                "." + RO_COL_4 + " ELSE 0 END) FROM " + REGISTO_CONTA_TABLE + " NATURAL JOIN " + REGISTO_OPERACOES_TABLE +
                " GROUP BY " + REGISTO_CONTA_TABLE + "." + RC_COL_2;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return labels;
    }

    /*==================Lista de Despesas e Categorias=====================*/
    public List<String> listaDespesaCategoria() {
        List<String> itens = new ArrayList<>();

        String query = "SELECT * FROM " + DESPESAS_CATEGORIA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                itens.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return itens;
    }


    /*    =============Tras o ID da Categoria da Despesa====================*/
    public int idCategoriaDispesa(String categoria) {
        int id_categoria = 0;

        String query = "SELECT " + CD_COL_1 + " FROM " + DESPESAS_CATEGORIA_TABLE + " WHERE " + CD_COL_2 + " = '" + categoria + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_categoria = cursor.getInt(0);
        }
        return id_categoria;
    }

    /*    =============Regista a Despesa====================*/
    public void apenasRegistaDespesa(DispesasModel dispesasModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DE_COL_2, dispesasModel.getId_categoria());
        contentValues.put(DE_COL_3, dispesasModel.getId_registo_operacao());
        contentValues.put(DE_COL_4, dispesasModel.getDescricao_dispesa());
        contentValues.put(DE_COL_5, dispesasModel.getData_pagamento());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(DESPESAS_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Mostra as Despesas e suas categorias====================*/
    public Cursor listaDispesas() {
        String sqlQuery = "SELECT * FROM " + DESPESAS_CATEGORIA_TABLE + " NATURAL JOIN " + DESPESAS_TABLE + " NATURAL JOIN " +
                REGISTO_OPERACOES_TABLE + " NATURAL JOIN " + REGISTO_CONTA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    =============Mostra as Despesas e suas categorias====================*/
    public Cursor listaRendimentos() {
        String sqlQuery = "SELECT * FROM " + RENDIMENTOS_TABLE + " NATURAL JOIN " + REGISTO_OPERACOES_TABLE +
                " NATURAL JOIN " + REGISTO_CONTA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    /*    =============Tras o ultimo ID da operaco====================*/
    public int idOperacao() {
        String query = "SELECT " + RO_COL_1 + " FROM " + REGISTO_OPERACOES_TABLE + " order by " + RO_COL_1 + " DESC limit 1";

        int id_operacao = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_operacao = cursor.getInt(0);
        }
        return id_operacao;
    }

    /*    =============Tras o ultimo ID do registo de venda====================*/
    public int idRegistoVenda() {
        String query = "SELECT " + RV_COL_1 + " FROM " + REGISTO_VENDA_TABLE + " order by " + RV_COL_1 + " DESC limit 1";

        int id_registo_venda = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_registo_venda = cursor.getInt(0);
        }

        return id_registo_venda;
    }


    /*    =============Tras o ultimo ID do registo de venda====================*/
    public int idCliente() {
        String query = "SELECT " + CL_COL_1 + " FROM " + CLIENTE_TABLE + " order by " + CL_COL_1 + " DESC limit 1";

        int id_cliente = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            id_cliente = cursor.getInt(0);
        }

        return id_cliente;
    }

    /*    =============Regista Rendimentos====================*/
    public void registoRendimento(RedimentosModel redimentosModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(RE_COL_2, redimentosModel.getId_registo_operacao());
        contentValues.put(RE_COL_3, redimentosModel.getRedimentoDescricao());
        contentValues.put(RE_COL_4, redimentosModel.getRendimentoData());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(RENDIMENTOS_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Regista Venda====================*/
    public void registoVenda(RegistoVendaModel registoVendaModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(RV_COL_3, registoVendaModel.getId_user());
        contentValues.put(RV_COL_4, registoVendaModel.getId_registo_operacao());
        contentValues.put(RV_COL_6, registoVendaModel.getVenda_estado());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(REGISTO_VENDA_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Regista produtos a Serem Vendidos====================*/
    public void registoVendas(VendasModel vendasModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(VE_COL_2, vendasModel.getId_produto());
        contentValues.put(VE_COL_3, vendasModel.getId_registo_venda());
        contentValues.put(VE_COL_4, vendasModel.getVenda_quantidade());
        contentValues.put(VE_COL_5, vendasModel.getVenda_preco());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(VENDA_TABLE, null, contentValues);

        db.close();
    }

    /*    =============Tras o ID da Categoria da Despesa====================*/
    public double quantidadePro(String nome) {
        double quantidade = 0;

        String query = "SELECT " + PR_COL_6 + " FROM " + PRODUTO_TABLE + " WHERE " + PR_COL_5 + " = '" + nome + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            quantidade = cursor.getDouble(cursor.getColumnIndex(PR_COL_6));
        }
        return quantidade;
    }

    public boolean deleteDespesa(int id_Despesa, int id_Operacao) {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();

        int query = db.delete(REGISTO_OPERACOES_TABLE, RO_COL_1 + " = " + id_Operacao, null);
        int query2 = db.delete(DESPESAS_TABLE, DE_COL_1 + " = " + id_Despesa, null);

        if (query > 0 || query2 > 0) {
            result = true;
        }

        db.close();
        return result;
    }

    public boolean deleteRedimento(int id_Redimento, int id_Operacao) {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();

        int query = db.delete(REGISTO_OPERACOES_TABLE, RO_COL_1 + " = " + id_Operacao, null);
        int query2 = db.delete(RENDIMENTOS_TABLE, RE_COL_1 + " = " + id_Redimento, null);

        if (query > 0 || query2 > 0) {
            result = true;
        }

        db.close();
        return result;
    }

    public boolean pagaRendi(int sss, int opca, int idop) {

        boolean ss = false;
        ContentValues contentValues = new ContentValues();

        contentValues.put(RO_COL_3, opca);
        contentValues.put(RO_COL_2, sss);

        SQLiteDatabase db = this.getWritableDatabase();

        int f = db.update(REGISTO_OPERACOES_TABLE, contentValues, RO_COL_1 + " = '" + idop + "'", null);

        if (f > 0) {
            ss = true;
        }

        db.close();

        return ss;
    }

    public Cursor listaVendas() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_TABLE + " INNER JOIN " + VENDA_TABLE + " INNER JOIN " +
                REGISTO_VENDA_TABLE + " WHERE " + PRODUTO_TABLE + "." + PR_COL_1 + " == " + VENDA_TABLE + "." + VE_COL_2 +
                " AND " + VENDA_TABLE + "." + VE_COL_3 + " == " + REGISTO_VENDA_TABLE + "." + RV_COL_1 + " ORDER BY " + VE_COL_1 + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    public Cursor listaMaisVendidos() {
        String sqlQuery = "SELECT " + PR_COL_5 + "," + PC_COL_2 + ",sum(" + VENDA_TABLE + "." + VE_COL_4 + ") FROM " + PRODUTO_CATEGORIA_TABLE + " INNER JOIN " +
                PRODUTO_TABLE + " INNER JOIN " + VENDA_TABLE + " INNER JOIN " + REGISTO_VENDA_TABLE + " WHERE " +
                PRODUTO_TABLE + "." + PR_COL_2 + " == " + PRODUTO_CATEGORIA_TABLE + "." + PC_COL_1 + " AND " +
                PRODUTO_TABLE + "." + PR_COL_1 + " == " + VENDA_TABLE + "." + VE_COL_2 + " AND " +
                VENDA_TABLE + "." + VE_COL_3 + " == " + REGISTO_VENDA_TABLE + "." + RV_COL_1 + " GROUP BY " +
                PR_COL_5 + " ORDER BY SUM(" + VENDA_TABLE + "." + VE_COL_4 + ") DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }


    public void registaEmpresa(CompanyModel companyModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(EI_COL_2, companyModel.getId_user());
        contentValues.put(EI_COL_3, companyModel.getNome_empresa());
        contentValues.put(EI_COL_4, companyModel.getEndereco());
        contentValues.put(EI_COL_5, companyModel.getNuit());
        contentValues.put(EI_COL_6, companyModel.getEmail());
        contentValues.put(EI_COL_7, companyModel.getNome_proprietario());
        contentValues.put(EI_COL_8, companyModel.getContacto());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(EMPRESA_TABLE, null, contentValues);

        db.close();
    }

    public Cursor empresaDetalhes() {
        String sqlQuery = "SELECT * FROM " + EMPRESA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

}
