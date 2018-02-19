package com.afrofx.code.anjesgf;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.afrofx.code.anjesgf.models.CategoriaModel;
import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UnidadeModel;
import com.afrofx.code.anjesgf.models.UserModel;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

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
    private static final String OPERACOES_TABLE = "moze_operacoes";
    private static final String CLIENTE_TABLE = "moze_cliente";


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
    private static final String PR_COL_7 = "produto_quanti_unidade";
    private static final String PR_COL_8 = "produto_quanti_minima";
    private static final String PR_COL_9 = "produto_preco_compra";
    private static final String PR_COL_10 = "produto_preco_venda";
    private static final String PR_COL_11 = "produto_data_registo";
    private static final String PR_COL_12 = "produto_validade";

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
    private static final String RV_COL_4 = "id_conta";
    private static final String RV_COL_5 = "venda_desconto";
    private static final String RV_COL_6 = "venda_estado";
    private static final String RV_COL_7 = "valor_total";
    private static final String RV_COL_8 = "venda_data";
    private static final String RV_COL_9 = "venda_data_expiracao";

    //==========COLUNAS REGISTO_CONTA_TABLE
    private static final String RC_COL_1 = "id_conta";
    private static final String RC_COL_2 = "conta_nome";

    //==========COLUNAS REGISTO_OPERACOES_TABLE

    private static final String RO_COL_1 = "id_registo_opeacao";
    private static final String RO_COL_2 = "id_conta";
    private static final String RO_COL_3 = "id_operacao";
    private static final String RO_COL_4 = "operacao_valor";

    //==========COLUNAS OPERACOES_TABLE
    private static final String OP_COL_1 = "id_operacao";
    private static final String OP_COL_2 = "operacao_nome";


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

    //==========COLUNAS CLIENTES_TABLE=====================*/
    private static final String CL_COL_1 = "id_cliente";
    private static final String CL_COL_2 = "id_endereco";
    private static final String CL_COL_3 = "cliente_nome";
    private static final String CL_COL_4 = "cliente_cell";
    private static final String CL_COL_5 = "cliente_email";
    private static final String CL_COL_6 = "cliente_data_registo";

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

    /*==================Criacao da tabela produto categoria na bd=====================*/
    private static final String query_fornecedor = "CREATE TABLE " + FORNECEDOR_TABLE + "("
            + FO_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + FO_COL_2 + " INTEGER REFERENCES " + ENDERECO_TABLE
            + "(" + EN_COL_1 + ")," + FO_COL_3 + " VARCHAR NOT NULL," + FO_COL_4 + "INTEGER," + FO_COL_5 + " VARCHAR," +
            FO_COL_6 + " VARCHAR)";

    /*==================Criacao da tabela PRODUTOS na bd=====================*/
    private static final String query_produto = "CREATE TABLE " + PRODUTO_TABLE + "("
            + PR_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            PR_COL_2 + " INTEGER REFERENCES " + PRODUTO_CATEGORIA_TABLE + "(" + PC_COL_1 + ")DEFAULT 0," +
            PR_COL_3 + " INTEGER REFERENCES " + PRODUTO_UNIDADE_TABLE + "(" + PU_COL_1 + ")DEFAULT 0," +
            PR_COL_4 + " INTEGER REFERENCES " + FORNECEDOR_TABLE + "(" + FO_COL_1 + ")DEFAULT 0," +
            PR_COL_5 + " VARCHAR NOT NULL," + PR_COL_6 + " REAL," + PR_COL_7 + " REAL," +
            PR_COL_8 + " REAL, " + PR_COL_9 + " REAL ," + PR_COL_10 + " REAL ," + PR_COL_11 + " DATE DEFAULT (CURRENT_TIMESTAMP)," +
            PR_COL_12 + " DATE DEFAULT Nenhuma)";

    /*==================Criacao da tabela REGISTO VENDA na bd=====================*/
    private static final String query_registo_venda = "CREATE TABLE " + REGISTO_VENDA_TABLE + "("
            + RV_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            RV_COL_2 + " INTEGER REFERENCES " + CLIENTE_TABLE + "(" + CL_COL_1 + ") DEFAULT Anonimo," +
            RV_COL_3 + " INTEGER REFERENCES " + USUARIO_TABLE + "(" + US_COL_1 + ") NOT NULL," +
            RV_COL_4 + " INTEGER REFERENCES " + REGISTO_CONTA_TABLE + "(" + RC_COL_1 + ")," +
            RV_COL_5 + " REAL," + RV_COL_6 + " VARCHAR NOT NULL," + RV_COL_7 + " REAL NOT NULL," +
            RV_COL_8 + " DATE DEFAULT (CURRENT_TIMESTAMP)," + RV_COL_9 + " DATE)";

    /*==================Criacao da tabela VENDA na bd=====================*/
    private static final String query_venda = "CREATE TABLE " + VENDA_TABLE + "("
            + VE_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            VE_COL_2 + " INTEGER REFERENCES " + PRODUTO_TABLE + "(" + PR_COL_1 + ") NOT NULL," +
            VE_COL_3 + " INTEGER REFERENCES " + REGISTO_VENDA_TABLE + "(" + RV_COL_1 + ") NOT NULL," +
            VE_COL_4 + " REAL NOT NULL," + VE_COL_5 + " REAL NOT NULL)";

    /*==================Criacao da tabela VENDA na bd=====================*/
    private static final String query_registo_operecaoes = "CREATE TABLE " + REGISTO_OPERACOES_TABLE + "("
            + RO_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            RO_COL_2 + " INTEGER REFERENCES " + REGISTO_CONTA_TABLE + "(" + RC_COL_1 + ") NOT NULL," +
            RO_COL_3 + " INTEGER REFERENCES " + OPERACOES_TABLE + "(" + OP_COL_1 + ") NOT NULL," +
            RO_COL_4 + " REAL NOT NULL)";

    private static final String query_operecaoes = "CREATE TABLE " + OPERACOES_TABLE + "("
            + OP_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            OP_COL_2 + " VARCHAR NOT NULL)";

    /*==================Criacao da tabela VENDA na bd=====================*/
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
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_endereco);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_usuario);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_cliente);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_produto_categoria);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_produto);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_produto_unidade);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_venda);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_registo_venda);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_operecaoes);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_registo_conta);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_fornecedor);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_registo_operecaoes);

        db.execSQL("INSERT INTO " + CARGO_TABLE + "(" + CA_COL_2 + ") VALUES('Administrador')");
        db.execSQL("INSERT INTO " + CARGO_TABLE + "(" + CA_COL_2 + ") VALUES('Colaborador')");
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
        db.execSQL("DROP TABLE IF EXISTS " + OPERACOES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTO_CONTA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTO_OPERACOES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUTO_UNIDADE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FORNECEDOR_TABLE);
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
    /*===========================================FIM DOS METODOS DO USUARIO(REGISTO/LOGIN)======================================
    ===========================================================================================================================*/


    /*===========================================INICIO DOS METODOS DE PRODUTOS(REGISTO/EDITAR, etc)============================*/
     /* ======================Verifica a existencia do produto no sistema================================*/
    public StockModel procuraProduto(String nome) {
        String sqlQuery = "SELECT * FROM " + PRODUTO_TABLE + " WHERE " + PR_COL_5 + " = '" + nome + "'";

        SQLiteDatabase db = this.getWritableDatabase();

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


    public Cursor listProduto() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_TABLE + " NATURAL JOIN " + PRODUTO_CATEGORIA_TABLE + " ORDER BY " + PR_COL_5;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }


    public Cursor listCategoria() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_CATEGORIA_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    public Cursor listUnidade() {
        String sqlQuery = "SELECT * FROM " + PRODUTO_UNIDADE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        return cursor;
    }

    public boolean updateQuatidade(String nome, double quantidade) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PR_COL_6, quantidade);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PRODUTO_TABLE, contentValues, PR_COL_5 + " = '" + nome + "'", null);
        db.close();
        return true;
    }


    public void inserirProduto(StockModel stockModel) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PR_COL_2, stockModel.getId_categoria());
        contentValues.put(PR_COL_3, stockModel.getId_unidade());
        contentValues.put(PR_COL_4, stockModel.getId_fornecedor());
        contentValues.put(PR_COL_5, stockModel.getProduto_nome());
        contentValues.put(PR_COL_6, stockModel.getProduto_quantidade());
        contentValues.put(PR_COL_7, stockModel.getProduto_quanti_unidade());
        contentValues.put(PR_COL_8, stockModel.getProduto_quanti_minima());
        contentValues.put(PR_COL_9, stockModel.getProduto_preco_compra());
        contentValues.put(PR_COL_10, stockModel.getProduto_preco_venda());
        contentValues.put(PR_COL_12, stockModel.getProduto_validade());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(PRODUTO_TABLE, null, contentValues);

        db.close();
    }

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

    public void registarCategoria(CategoriaModel categoriaModel) {
        ContentValues cv = new ContentValues();

        cv.put(PC_COL_2, categoriaModel.getCategoria());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(PRODUTO_CATEGORIA_TABLE, null, cv);

        db.close();
    }


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

    public void registarUnidade(UnidadeModel unidadeModel) {
        ContentValues cv = new ContentValues();

        cv.put(PU_COL_2, unidadeModel.getUnidadee_nome());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(PRODUTO_UNIDADE_TABLE, null, cv);

        db.close();
    }

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

    public boolean updateProduto(String nome_conf, String nome, double preco_compra, double preco_venda) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(PR_COL_3, nome);
        contentValues.put(PR_COL_5, preco_compra);
        contentValues.put(PR_COL_7, preco_venda);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PRODUTO_TABLE, contentValues, PR_COL_3 + " = '" + nome_conf + "'", null);
        db.close();
        return true;
    }

    /*    =============Metodo Responsavel por abrir a Coneccao com a BD====================*/


    /*    =============Mostra o Valor total do Stock caso sejam vendidos todos os produtos====================*/
    public double saldoCaixa() {
        String sqlQuery = "SELECT SUM((" + PR_COL_6 + "*" + PR_COL_7 + ")*(" + PR_COL_10 + ")) FROM " + PRODUTO_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sqlQuery, null);

        double total = 0;

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            total = cursor.getInt(0);
        }
        return total;
    }

    /*    =============Mostra o Valor total do Stock caso sejam vendidos todos os produtos====================*/
    public void registarConta() {


    }

     /*    =============Mostra o Valor total do Stock caso sejam vendidos todos os produtos====================*/

    public void mostrarConta() {

    }


}
