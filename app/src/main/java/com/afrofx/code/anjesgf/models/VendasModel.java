package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/22/2018.
 */

public class VendasModel {
    private int id_venda, id_produto, id_registo_venda;
    private double venda_quantidade, venda_preco;
    private String nome;

    public VendasModel(double venda_quantidade, double venda_preco, String nome) {
        this.venda_quantidade = venda_quantidade;
        this.venda_preco = venda_preco;
        this.nome = nome;
    }

    public double getVenda_quantidade() {
        return venda_quantidade;
    }

    public void setVenda_quantidade(double venda_quantidade) {
        this.venda_quantidade = venda_quantidade;
    }

    public double getVenda_preco() {
        return venda_preco;
    }

    public void setVenda_preco(double venda_preco) {
        this.venda_preco = venda_preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId_venda() {
        return id_venda;
    }

    public void setId_venda(int id_venda) {
        this.id_venda = id_venda;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getId_registo_venda() {
        return id_registo_venda;
    }

    public void setId_registo_venda(int id_registo_venda) {
        this.id_registo_venda = id_registo_venda;
    }

}
