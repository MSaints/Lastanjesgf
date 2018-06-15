package com.afrofx.code.anjesgf.models;

import android.content.Context;
import android.database.Cursor;

import com.afrofx.code.anjesgf.DatabaseHelper;

/**
 * Created by Afro FX on 3/21/2018.
 */

public class CompanyModel {

    Context context;

    DatabaseHelper db;

    private String nome_proprietario, endereco, nome_empresa, email;

    private long contacto, nuit, id_user;

    public CompanyModel(long id_user, long contacto, long nuit, String nome_empresa, String nome_proprietario, String endereco) {
        this.nome_proprietario = nome_proprietario;
        this.endereco = endereco;
        this.nome_empresa = nome_empresa;
        this.contacto = contacto;
        this.nuit = nuit;
        this.id_user = id_user;
    }

    public String getNome_proprietario() {
        return nome_proprietario;
    }

    public void setNome_proprietario(String nome_proprietario) {
        this.nome_proprietario = nome_proprietario;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome_empresa() {
        return nome_empresa;
    }

    public void setNome_empresa(String nome_empresa) {
        this.nome_empresa = nome_empresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getContacto() {
        return contacto;
    }

    public void setContacto(long contacto) {
        this.contacto = contacto;
    }

    public long getNuit() {
        return nuit;
    }

    public void setNuit(long nuit) {
        this.nuit = nuit;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public CompanyModel(Context context) {
        this.context=context;
        db = new DatabaseHelper(context);

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

                    new CompanyModel(id_user, contacto, nuit, empresa, nome, local);

                }
            }
        }
    }
}
