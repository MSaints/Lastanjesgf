package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 3/5/2018.
 */

public class RedimentosModel {

    private String redimentoDescricao, rendimentoData, rendimentoDataRegisto, conta;
    private int id_rendimento, id_registo_operacao, tipoOpe;
    private double rendimento_valor;

    public RedimentosModel() {
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public int getTipoOpe() {
        return tipoOpe;
    }

    public void setTipoOpe(int tipoOpe) {
        this.tipoOpe = tipoOpe;
    }

    public RedimentosModel(String redimentoDescricao, String rendimentoData, String conta, int id_rendimento, double rendimento_valor, int tipoOpe, int id_registo_operacao) {
        this.redimentoDescricao = redimentoDescricao;
        this.rendimentoData = rendimentoData;
        this.conta = conta;
        this.id_rendimento = id_rendimento;
        this.rendimento_valor = rendimento_valor;
        this.tipoOpe = tipoOpe;
        this.id_registo_operacao = id_registo_operacao;
    }

    public RedimentosModel(String redimentoDescricao, String rendimentoData, int id_registo_operacao) {
        this.redimentoDescricao = redimentoDescricao;
        this.rendimentoData = rendimentoData;
        this.id_registo_operacao = id_registo_operacao;
    }

    public String getRedimentoDescricao() {
        return redimentoDescricao;
    }

    public void setRedimentoDescricao(String redimentoDescricao) {
        this.redimentoDescricao = redimentoDescricao;
    }

    public String getRendimentoData() {
        return rendimentoData;
    }

    public void setRendimentoData(String rendimentoData) {
        this.rendimentoData = rendimentoData;
    }

    public String getRendimentoDataRegisto() {
        return rendimentoDataRegisto;
    }

    public void setRendimentoDataRegisto(String rendimentoDataRegisto) {
        this.rendimentoDataRegisto = rendimentoDataRegisto;
    }

    public int getId_rendimento() {
        return id_rendimento;
    }

    public void setId_rendimento(int id_rendimento) {
        this.id_rendimento = id_rendimento;
    }

    public int getId_registo_operacao() {
        return id_registo_operacao;
    }

    public void setId_registo_operacao(int id_registo_operacao) {
        this.id_registo_operacao = id_registo_operacao;
    }

    public double getRendimento_valor() {
        return rendimento_valor;
    }

    public void setRendimento_valor(double rendimento_valor) {
        this.rendimento_valor = rendimento_valor;
    }
}
