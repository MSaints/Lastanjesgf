package com.afrofx.code.anjesgf;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.afrofx.code.anjesgf.models.StockModel;
import com.afrofx.code.anjesgf.models.UserModel;


/**
 * Created by Afro FX on 2/2/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "moze.db";

    /*==================Tabelas a Serem Criadas======================*/
    private static final String USER_TABLE = "moze_usuario";
    private static final String POSITION_TABLE = "moze_cargo";
    private static final String VENDAS_TABLE = "moze_venda";
    private static final String REGISTO_VENDAS_TABLE = "moze_registo_venda";
    private static final String PRODUTOS_TABLE = "moze_produtos";
    private static final String PRODUTO_CATEGORIA_TABLE = "moze_produto_categoria";
    private static final String LOCALIDADE_TABLE = "moze_localidade";
    private static final String CLIENTES_TABLE = "moze_cliente";


    /*==================COLUNAS DAS TABELAS DA BD MOZE======================*/
    //==========COLUNAS USER_TABLE
    private static final String U_COL_1 = "user_id";
    private static final String U_COL_2 = "id_cargo";
    private static final String U_COL_3 = "user_name";
    private static final String U_COL_4 = "user_number";
    private static final String U_COL_5 = "user_email";
    private static final String U_COL_6 = "user_password";
    private static final String U_COL_7 = "user_reg_date";
    private static final String U_COL_8 = "user_login";
    private static final String U_COL_9 = "id_localidade";


    //==========COLUNAS POSITION_TABLE
    private static final String PO_COL_1 = "id_cargo";
    private static final String PO_COL_2 = "cargo_nome";
    private static final String PO_COL_3 = "cargo_posicao";

    //==========COLUNAS VENDAS_TABLE
    private static final String V_COL_1 = "id_venda";
    private static final String V_COL_2 = "id_produto";
    private static final String V_COL_3 = "venda_quantidade";
    private static final String V_COL_4 = "venda_preco";

    //==========COLUNAS PRODUTOS_TABLE
    private static final String PR_COL_1 = "id_produto";
    private static final String PR_COL_2 = "id_categoria";
    private static final String PR_COL_3 = "produto_nome";
    private static final String PR_COL_4 = "produto_quantidade";
    private static final String PR_COL_5 = "produto_preco_compra";
    private static final String PR_COL_7 = "produto_preco_venda";

    //==========COLUNAS REGISTO_VENDAS_TABLE
    private static final String R_V_COL_1 = "id_registo_venda";
    private static final String R_V_COL_2 = "id_cliente";
    private static final String R_V_COL_3 = "user_id";
    private static final String R_V_COL_4 = "valor_total";
    private static final String R_V_COL_5 = "venda_desconto";
    private static final String R_V_COL_6 = "venda_estado";
    private static final String R_V_COL_7 = "venda_pagamento";
    private static final String R_V_COL_8 = "venda_data";
    private static final String R_V_COL_9 = "venda_data_expiracao";

    //==========COLUNAS PRODUTO_CATEGORIA_TABLE
    private static final String P_C_COL_1 = "id_categoria";
    private static final String P_C_COL_2 = "categoria_nome";

    //==========COLUNAS LOCALIDADE_TABLE
    private static final String L_COL_1 = "id_localidade";
    private static final String L_COL_2 = "localidade_cidade";
    private static final String L_COL_3 = "localidade_provincia";
    private static final String L_COL_4 = "localidade_bairro";

    //==========COLUNAS CLIENTES_TABLE=====================*/
    private static final String C_COL_1 = "id_cliente";
    private static final String C_COL_2 = "id_localidade";
    private static final String C_COL_3 = "cliente_nome";
    private static final String C_COL_4 = "cliente_cell";
    private static final String C_COL_5 = "cliente_email";
    private static final String C_COL_6 = "cliente_data_registo";

    /*==================Criacao da tabela cargos na bd=====================*/
    private static final String query_cargos = "CREATE TABLE "
    +POSITION_TABLE+ "("+PO_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
    +PO_COL_2+" VARCHAR NOT NULL DEFAULT Administrador,"+ PO_COL_3+" BOOLEAN DEFAULT (1) NOT NULL)";

    /*==================Criacao da tabela localidade na bd=====================*/
    private static final String query_localidade = "CREATE TABLE "
    +LOCALIDADE_TABLE+"("+L_COL_1+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+L_COL_2+
    " VARCHAR NOT NULL,"+L_COL_3+" VARCHAR NOT NULL,"+L_COL_4+" VARCHAR)";

    /*==================Criacao da tabela Usuarios na bd=====================*/
    private static final String query_usuario = "CREATE TABLE "+USER_TABLE+"("
    +U_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+U_COL_2+" INTEGER REFERENCES "+POSITION_TABLE+
    "("+PO_COL_1+") DEFAULT (1) NOT NULL,"+U_COL_3+" VARCHAR NOT NULL,"+U_COL_4+" INTEGER NOT NULL UNIQUE,"+U_COL_5+
    " VARCHAR,"+U_COL_6+" INTEGER NOT NULL,"+U_COL_7+" DATETIME, "+U_COL_8+" DATETIME,"+U_COL_9+
    " INTEGER REFERENCES "+LOCALIDADE_TABLE+"("+L_COL_1+"))";

    /*==================Criacao da tabela Cliente na bd=====================*/
    private static final String query_cliente = "CREATE TABLE "+CLIENTES_TABLE+"("
    +C_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+C_COL_3+" VARCHAR NOT NULL,"+C_COL_4+
    " INTEGER,"+C_COL_5+" VARCHAR,"+C_COL_2+" INTEGER REFERENCES "+LOCALIDADE_TABLE+"("+L_COL_1+"),"+C_COL_6+" DATE NOT NULL)";

    /*==================Criacao da tabela produto categoria na bd=====================*/
    private static final String query_produto_categoria = "CREATE TABLE "+PRODUTO_CATEGORIA_TABLE+"("
    +P_C_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+P_C_COL_2+" VARCHAR NOT NULL)";

    /*==================Criacao da tabela PRODUTOS na bd=====================*/
    private static final String query_produto = "CREATE TABLE "+PRODUTOS_TABLE+"("
    +PR_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+PR_COL_2+" INTEGER REFERENCES "+PRODUTO_CATEGORIA_TABLE+
    "("+P_C_COL_1+")DEFAULT (0),"+PR_COL_3+" VARCHAR NOT NULL,"+PR_COL_4+" INTEGER,"+PR_COL_5+" REAL NOT NULL,"+PR_COL_7+
    " REAL NOT NULL, FOREIGN KEY ("+PR_COL_2+") REFERENCES "+PRODUTO_CATEGORIA_TABLE+"("+P_C_COL_1+"))";

    /*==================Criacao da tabela REGISTO VENDA na bd=====================*/
    private static final String query_registo_venda = "CREATE TABLE "+REGISTO_VENDAS_TABLE+"("
    +R_V_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+R_V_COL_2+" INTEGER REFERENCES "
    +CLIENTES_TABLE+" ("+C_COL_1+") DEFAULT Anonimo,"+R_V_COL_3+" INTEGER REFERENCES "+USER_TABLE+
    "("+U_COL_1+") NOT NULL,"+R_V_COL_4+" REAL NOT NULL,"+R_V_COL_5+" REAL,"+R_V_COL_6+
    " VARCHAR NOT NULL,"+R_V_COL_7+" VARCHAR NOT NULL,"+R_V_COL_8+" DATE NOT NULL,"+R_V_COL_9+" DATE)";

    /*==================Criacao da tabela VENDA na bd=====================*/
    private static final String query_venda = "CREATE TABLE "+VENDAS_TABLE+"("
    +V_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+V_COL_2+" INTEGER REFERENCES "
    +PRODUTOS_TABLE+"("+PR_COL_1+") NOT NULL,"+V_COL_3+" REAL NOT NULL,"+V_COL_4+" REAL NOT NULL);";




    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_cargos);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_localidade);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_usuario);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_cliente);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_produto_categoria);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_produto);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_registo_venda);
        /*==================Criacao da tabela cargos na bd=====================*/
        db.execSQL(query_venda);
        db.execSQL("INSERT INTO "+POSITION_TABLE+"("+PO_COL_2+","+PO_COL_3+") VALUES('Administrador', 1)");
        db.execSQL("INSERT INTO "+POSITION_TABLE+"("+PO_COL_2+","+PO_COL_3+") VALUES('Gestor', 0)");
        db.execSQL("");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+POSITION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+LOCALIDADE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CLIENTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PRODUTO_CATEGORIA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PRODUTOS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+REGISTO_VENDAS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+VENDAS_TABLE);
        onCreate(db);
    }


    public void inserirUser(UserModel userModel){

        ContentValues contentValues = new ContentValues();

        contentValues.put(U_COL_7, userModel.getUser_reg_date());
        contentValues.put(U_COL_6, userModel.getUser_pin());
        contentValues.put(U_COL_3, userModel.getUser_nome());
        contentValues.put(U_COL_4, userModel.getUser_numero());
        contentValues.put(U_COL_5, userModel.getUser_email());

        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(USER_TABLE, null, contentValues);

        Log.w(TAG,"myApp");

        db.close();
    }


    public StockModel procuraProduto(String nome){
        String sqlQuery = "SELECT * FROM "+PRODUTOS_TABLE+" WHERE "+PR_COL_3+" = '"+nome+"'";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery,null);

        StockModel stockModel = new StockModel();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            stockModel.setProduto_id(Integer.parseInt(cursor.getString(0)));
            stockModel.setProduto_id_categoria(Integer.parseInt(cursor.getString(1)));
            stockModel.setProduto_nome(cursor.getString(2));
            stockModel.setProduto_quantidade(Integer.parseInt(cursor.getString(3)));
            stockModel.setProduto_preco_compra(Double.parseDouble(cursor.getString(4)));
            stockModel.setProduto_preco_venda(Double.parseDouble(cursor.getString(5)));
            cursor.close();
        }else{
            stockModel = null;
        }

        db.close();
        return stockModel;
    }


    public Cursor listProduto(){
        String sqlQuery = "SELECT * FROM "+PRODUTOS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery,null);

        return cursor;
    }


    public void inserirProduto(StockModel stockModel){

        ContentValues contentValues = new ContentValues();

        contentValues.put(PR_COL_3, stockModel.getProduto_nome());
        contentValues.put(PR_COL_4, stockModel.getProduto_quantidade());
        contentValues.put(PR_COL_5, stockModel.getProduto_preco_compra());
        contentValues.put(PR_COL_7, stockModel.getProduto_preco_venda());

        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(PRODUTOS_TABLE, null, contentValues);

        Log.w(TAG,"myApp");

        db.close();
    }


    public Cursor saldoCaixa(){
        String sqlQuery = "SELECT SUM("+PR_COL_7+"*"+PR_COL_4+") FROM "+PRODUTOS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        Cursor c = saldoCaixa();

        double total_saldo = cursor.getInt(cursor.getColumnIndex("total"));

        return cursor;
    }







    public UserModel userLogin(int userNumber, int userPin){
        String sqlQuery = "SELECT * FROM "+USER_TABLE+" WHERE "+U_COL_4+" = "+userNumber+ " AND " +U_COL_6+" = "+userPin+"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery,null);

        UserModel userModel = new UserModel();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            userModel.setUser_id(Integer.parseInt(cursor.getString(0)));
            userModel.setUser_cargo_id(Integer.parseInt(cursor.getString(1)));
            userModel.setUser_nome(cursor.getString(2));
            userModel.setUser_numero(Integer.parseInt(cursor.getString(3)));
            userModel.setUser_email(cursor.getString(4));
            userModel.setUser_pin(Integer.parseInt(cursor.getString(5)));
            userModel.setUser_reg_date(cursor.getString(6));
            userModel.setUser_login_date(cursor.getString(7));
            userModel.setUser_localidade_id(cursor.getInt(8));
            cursor.close();
        }else{
            userModel = null;
        }

        db.close();
        return userModel;
    }

    public UserModel procurar(int userNumber){
        String sqlQuery = "SELECT * FROM "+USER_TABLE+" WHERE "+U_COL_4+" = "+userNumber;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery,null);

        UserModel userModel = new UserModel();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            userModel.setUser_id(Integer.parseInt(cursor.getString(0)));
            userModel.setUser_cargo_id(Integer.parseInt(cursor.getString(1)));
            userModel.setUser_nome(cursor.getString(2));
            userModel.setUser_numero(Integer.parseInt(cursor.getString(3)));
            userModel.setUser_email(cursor.getString(4));
            userModel.setUser_pin(Integer.parseInt(cursor.getString(5)));
            userModel.setUser_reg_date(cursor.getString(6));
            userModel.setUser_login_date(cursor.getString(7));
            userModel.setUser_localidade_id((cursor.getInt(8)));
            cursor.close();
        }else{
            userModel = null;
        }

        db.close();
        return userModel;
    }
}
