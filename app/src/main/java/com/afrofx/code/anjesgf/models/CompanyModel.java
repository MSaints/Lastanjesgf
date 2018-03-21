package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 3/21/2018.
 */

public class CompanyModel {

    private String nome_proprietario, endereco, nome_empresa, email;

    private int contacto, nuit, id_user;

    public CompanyModel(int id_user, int contacto, int nuit, String nome_empresa, String nome_proprietario, String email, String endereco) {
        this.nome_proprietario = nome_proprietario;
        this.endereco = endereco;
        this.nome_empresa = nome_empresa;
        this.email = email;
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

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
