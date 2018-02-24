package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/19/2018.
 */

public class FornecedorModel {

    private int id_fornecedor, id_endereco, fornecedor_contacto;
    private String fornecedor_nome, fornecedor_email, fornecedor_tipo;

    public FornecedorModel() {
    }

    public FornecedorModel(int id_fornecedor, String fornecedor_nome) {
        this.id_fornecedor = id_fornecedor;
        this.fornecedor_nome = fornecedor_nome;
    }

    public FornecedorModel(String fornecedor_nome, String fornecedor_tipo, String fornecedor_email, int fornecedor_contacto) {
        this.fornecedor_contacto = fornecedor_contacto;
        this.fornecedor_nome = fornecedor_nome;
        this.fornecedor_email = fornecedor_email;
        this.fornecedor_tipo = fornecedor_tipo;
    }

    public FornecedorModel(int id_fornecedor, int fornecedor_contacto, String fornecedor_nome, String fornecedor_email, String fornecedor_tipo) {
        this.id_fornecedor = id_fornecedor;
        this.fornecedor_contacto = fornecedor_contacto;
        this.fornecedor_nome = fornecedor_nome;
        this.fornecedor_email = fornecedor_email;
        this.fornecedor_tipo = fornecedor_tipo;
    }

    public int getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(int id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public int getId_endereco() {
        return id_endereco;
    }

    public void setId_endereco(int id_endereco) {
        this.id_endereco = id_endereco;
    }

    public int getFornecedor_contacto() {
        return fornecedor_contacto;
    }

    public void setFornecedor_contacto(int fornecedor_contacto) {
        this.fornecedor_contacto = fornecedor_contacto;
    }

    public String getFornecedor_nome() {
        return fornecedor_nome;
    }

    public void setFornecedor_nome(String fornecedor_nome) {
        this.fornecedor_nome = fornecedor_nome;
    }

    public String getFornecedor_email() {
        return fornecedor_email;
    }

    public void setFornecedor_email(String fornecedor_email) {
        this.fornecedor_email = fornecedor_email;
    }

    public String getFornecedor_tipo() {
        return fornecedor_tipo;
    }

    public void setFornecedor_tipo(String fornecedor_tipo) {
        this.fornecedor_tipo = fornecedor_tipo;
    }
}
