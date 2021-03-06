package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/6/2018.
 */

public class StockModel {

    private long id_produto, id_categoria, id_unidade, id_fornecedor, id_conta,id_registo_operacao, operacao_estado;
    private double produto_quantidade, produto_preco_compra, produto_quanti_minima,
            valor_operacao, produto_preco_venda;
    private String produto_nome, produto_data_registo, produto_validade;
    private String categoria;

    public long getId_registo_operacao() {
        return id_registo_operacao;
    }

    public void setId_registo_operacao(long id_registo_operacao) {
        this.id_registo_operacao = id_registo_operacao;
    }

    public StockModel(double produto_quantidade, double produto_preco_venda, String produto_nome, String categoria) {
        this.produto_quantidade = produto_quantidade;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
        this.categoria = categoria;
    }

    public StockModel(double produto_quantidade, String produto_nome, String categoria) {
        this.produto_quantidade = produto_quantidade;
        this.produto_nome = produto_nome;
        this.categoria = categoria;
    }
 public StockModel(double produto_quantidade, String produto_nome) {
        this.produto_quantidade = produto_quantidade;
        this.produto_nome = produto_nome;
    }

    public StockModel(double produto_quantidade, double produto_preco_venda, String produto_nome, String produto_data_registo, long id_produto, long
            id_registo_operacao) {
        this.produto_quantidade = produto_quantidade;
        this.id_produto = id_produto;
        this.id_registo_operacao = id_registo_operacao;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
        this.produto_data_registo = produto_data_registo;
    }
    public StockModel(double produto_quantidade, double produto_preco_venda, String produto_nome) {
        this.produto_quantidade = produto_quantidade;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
    }

    public long getId_conta() {
        return id_conta;
    }

    public void setId_conta(long id_conta) {
        this.id_conta = id_conta;
    }

    public long getOperacao_estado() {
        return operacao_estado;
    }

    public void setOperacao_estado(long operacao_estado) {
        this.operacao_estado = operacao_estado;
    }

    public double getValor_operacao() {
        return valor_operacao;
    }

    public void setValor_operacao(double valor_operacao) {
        this.valor_operacao = valor_operacao;
    }

    public StockModel(long id_conta, long operacao_estado, double valor_operacao) {
        this.id_conta = id_conta;
        this.operacao_estado = operacao_estado;
        this.valor_operacao = valor_operacao;
    }

    public StockModel() {
    }

    public StockModel(long id_produto, double quantide_produto, double preco_venda, String nome_produto) {
        this.id_produto = id_produto;
        this.produto_quantidade = quantide_produto;
        this.produto_preco_venda = preco_venda;
        this.produto_nome = nome_produto;
    }

    public StockModel(long id_categoria, long id_unidade, long id_fornecedor, double produto_quantidade, double produto_preco_compra, double produto_quanti_minima, double produto_preco_venda, String produto_nome, String produto_validade) {
        this.id_categoria = id_categoria;
        this.id_unidade = id_unidade;
        this.id_fornecedor = id_fornecedor;
        this.produto_quantidade = produto_quantidade;
        this.produto_preco_compra = produto_preco_compra;
        this.produto_quanti_minima = produto_quanti_minima;
        this.produto_preco_venda = produto_preco_venda;
        this.produto_nome = produto_nome;
        this.produto_validade = produto_validade;
    }

    public StockModel(long id_produto, String produto_nome, double produto_quantidade, double produto_preco_venda, String categoria) {
        this.id_produto = id_produto;
        this.produto_nome = produto_nome;
        this.id_categoria = id_categoria;
        this.produto_quantidade = produto_quantidade;
        this.produto_preco_venda = produto_preco_venda;
        this.categoria = categoria;
    }

    public long getId_produto() {
        return id_produto;
    }

    public void setId_produto(long id_produto) {
        this.id_produto = id_produto;
    }

    public long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(long id_categoria) {
        this.id_categoria = id_categoria;
    }

    public long getId_unidade() {
        return id_unidade;
    }

    public void setId_unidade(long id_unidade) {
        this.id_unidade = id_unidade;
    }

    public long getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(long id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public double getProduto_quantidade() {
        return produto_quantidade;
    }

    public void setProduto_quantidade(double produto_quantidade) {
        this.produto_quantidade = produto_quantidade;
    }

    public double getProduto_preco_compra() {
        return produto_preco_compra;
    }

    public void setProduto_preco_compra(double produto_preco_compra) {
        this.produto_preco_compra = produto_preco_compra;
    }

    public double getProduto_quanti_minima() {
        return produto_quanti_minima;
    }

    public void setProduto_quanti_minima(double produto_quanti_minima) {
        this.produto_quanti_minima = produto_quanti_minima;
    }

    public double getProduto_preco_venda() {
        return produto_preco_venda;
    }

    public void setProduto_preco_venda(double produto_preco_venda) {
        this.produto_preco_venda = produto_preco_venda;
    }

    public String getProduto_nome() {
        return produto_nome;
    }

    public void setProduto_nome(String produto_nome) {
        this.produto_nome = produto_nome;
    }

    public String getProduto_data_registo() {
        return produto_data_registo;
    }

    public void setProduto_data_registo(String produto_data_registo) {
        this.produto_data_registo = produto_data_registo;
    }

    public String getProduto_validade() {
        return produto_validade;
    }

    public void setProduto_validade(String produto_validade) {
        this.produto_validade = produto_validade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
