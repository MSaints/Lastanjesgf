package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/16/2018.
 */

public class UnidadeModel {
    private int id_unidade;
    private String unidade_nome;


    public UnidadeModel(int id_unidade, String unidade_nome) {
        this.id_unidade = id_unidade;
        this.unidade_nome = unidade_nome;
    }

    public UnidadeModel(String unidade_nome) {
        this.unidade_nome = unidade_nome;
    }

    public UnidadeModel() {
    }

    public int getId_unidade() {
        return id_unidade;
    }

    public void setId_unidade(int id_unidade) {
        this.id_unidade = id_unidade;
    }

    public String getUnidadee_nome() {
        return unidade_nome;
    }

    public void setUnidadee_nome(String unidadee_nome) {
        this.unidade_nome = unidadee_nome;
    }
}
