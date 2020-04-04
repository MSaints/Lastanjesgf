package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/20/2018.
 */

public class QuebrasModel {

    private long  quebraId, quebraIdProduto;
    private String quebraDescricao, quebraDataRegisto, quebraNomeProduto;
    private double quebraQuantidade, quebraPreco;

    public QuebrasModel(long quebraIdProduto, String quebraDescricao, String quebraDataRegisto, double quebraQuantidade, double quebraPreco) {
        this.quebraIdProduto = quebraIdProduto;
        this.quebraDescricao = quebraDescricao;
        this.quebraDataRegisto = quebraDataRegisto;
        this.quebraQuantidade = quebraQuantidade;
        this.quebraPreco = quebraPreco;
    }

    public QuebrasModel(String quebraDescricao,String quebraDataRegisto, String quebraNomeProduto, double quebraQuantidade, double quebraPreco) {
        this.quebraDescricao = quebraDescricao;
        this.quebraDataRegisto = quebraDataRegisto;
        this.quebraNomeProduto = quebraNomeProduto;
        this.quebraQuantidade = quebraQuantidade;
        this.quebraPreco = quebraPreco;
    }

    public String getQuebraNomeProduto() {
        return quebraNomeProduto;
    }

    public void setQuebraNomeProduto(String quebraNomeProduto) {
        this.quebraNomeProduto = quebraNomeProduto;
    }

    public long getQuebraId() {
        return quebraId;
    }

    public void setQuebraId(long quebraId) {
        this.quebraId = quebraId;
    }

    public long getQuebraIdProduto() {
        return quebraIdProduto;
    }

    public void setQuebraIdProduto(long quebraIdProduto) {
        this.quebraIdProduto = quebraIdProduto;
    }

    public String getQuebraDescricao() {
        return quebraDescricao;
    }

    public void setQuebraDescricao(String quebraDescricao) {
        this.quebraDescricao = quebraDescricao;
    }

    public String getQuebraDataRegisto() {
        return quebraDataRegisto;
    }

    public void setQuebraDataRegisto(String quebraDataRegisto) {
        this.quebraDataRegisto = quebraDataRegisto;
    }

    public double getQuebraQuantidade() {
        return quebraQuantidade;
    }

    public void setQuebraQuantidade(double quebraQuantidade) {
        this.quebraQuantidade = quebraQuantidade;
    }

    public double getQuebraPreco() {
        return quebraPreco;
    }

    public void setQuebraPreco(double quebraPreco) {
        this.quebraPreco = quebraPreco;
    }
}
