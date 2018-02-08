package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/6/2018.
 */

public class StockModel {

    private int produto_id_categoria;
    private int produto_quantidade;
    private int produto_id;
    private double produto_preco_compra;
    private double produto_preco_venda;
    private String produto_nome;
    private String produto_categoria;

    public int getProduto_id_categoria() {
        return produto_id_categoria;
    }

    public int getProduto_quantidade() {
        return produto_quantidade;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public double getProduto_preco_compra() {
        return produto_preco_compra;
    }

    public double getProduto_preco_venda() {
        return produto_preco_venda;
    }

    public String getProduto_nome() {
        return produto_nome;
    }

    public String getProduto_categoria() {
        return produto_categoria;
    }

    public void setProduto_id_categoria(int produto_id_categoria) {
        this.produto_id_categoria = produto_id_categoria;
    }

    public void setProduto_quantidade(int produto_quantidade) {
        this.produto_quantidade = produto_quantidade;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public void setProduto_preco_compra(double produto_preco_compra) {
        this.produto_preco_compra = produto_preco_compra;
    }

    public void setProduto_preco_venda(double produto_preco_venda) {
        this.produto_preco_venda = produto_preco_venda;
    }

    public void setProduto_nome(String produto_nome) {
        this.produto_nome = produto_nome;
    }

    public void setProduto_categoria(String produto_categoria) {
        this.produto_categoria = produto_categoria;
    }

    public StockModel(int produto_id_categoria, int produto_quantidade, int produto_id, double produto_preco_compra, double produto_preco_venda, String produto_nome, String produto_categoria) {
        this.produto_id_categoria = produto_id_categoria;
        this.produto_quantidade = produto_quantidade;
        this.produto_id = produto_id;
        this.produto_preco_compra = produto_preco_compra;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
        this.produto_categoria = produto_categoria;
    }

    public StockModel() {

    }

    public StockModel(int produto_quantidade, double produto_preco_venda, String produto_nome, String produto_categoria) {
        this.produto_quantidade = produto_quantidade;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
        this.produto_categoria = produto_categoria;
    }

    public StockModel(int produto_quantidade, double produto_preco_compra, double produto_preco_venda, String produto_nome) {
        this.produto_quantidade = produto_quantidade;
        this.produto_preco_compra = produto_preco_compra;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
    }
}
