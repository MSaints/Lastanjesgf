package com.afrofx.code.anjesgf.models;

import java.io.File;

/**
 * Created by Afro FX on 3/28/2018.
 */

public class FicheirosModel {

    public FicheirosModel() {
    }

    public String getNome_ficheiro() {
        return nome_ficheiro;
    }

    public void setNome_ficheiro(String nome_ficheiro) {
        this.nome_ficheiro = nome_ficheiro;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    private String nome_ficheiro, data_criacao;
}
