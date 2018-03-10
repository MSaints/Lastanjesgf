package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 3/1/2018.
 */

public class DispesasModel {

    private int id_dispesa, estado_dispesa, id_conta, id_categoria, id_registo_operacao,tipo_operacao;
    private double custo_dispesa;
    private String nome_dispesa, descricao_dispesa, categoria_dispesa, data_registo, data_pagamento, conta_dispesa;

    public DispesasModel(int id_conta, int tipo_operacao, double custo_dispesa) {
        this.id_conta = id_conta;
        this.tipo_operacao = tipo_operacao;
        this.custo_dispesa = custo_dispesa;
    }

    public DispesasModel(int id_categoria, int id_registo_operacao, String descricao_dispesa, String data_pagamento) {
        this.id_categoria = id_categoria;
        this.id_registo_operacao = id_registo_operacao;
        this.descricao_dispesa = descricao_dispesa;
        this.data_pagamento = data_pagamento;
    }

    public DispesasModel(int id_dispesa, int tipo_operacao, double custo_dispesa, String descricao_dispesa, String categoria_dispesa, String data_pagamento, String conta_dispesa) {
        this.id_dispesa = id_dispesa;
        this.conta_dispesa = conta_dispesa;
        this.tipo_operacao = tipo_operacao;
        this.custo_dispesa = custo_dispesa;
        this.descricao_dispesa = descricao_dispesa;
        this.categoria_dispesa = categoria_dispesa;
        this.data_pagamento = data_pagamento;
    }

    public int getTipo_operacao() {
        return tipo_operacao;
    }

    public void setTipo_operacao(int tipo_operacao) {
        this.tipo_operacao = tipo_operacao;
    }

    public int getId_registo_operacao() {
        return id_registo_operacao;
    }

    public void setId_registo_operacao(int id_registo_operacao) {
        this.id_registo_operacao = id_registo_operacao;
    }



    public DispesasModel() {
    }

    public int getId_conta() {
        return id_conta;
    }

    public void setId_conta(int id_conta) {
        this.id_conta = id_conta;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getConta_dispesa() {
        return conta_dispesa;
    }

    public void setConta_dispesa(String conta_dispesa) {
        this.conta_dispesa = conta_dispesa;
    }

    public int getId_dispesa() {
        return id_dispesa;
    }

    public void setId_dispesa(int id_dispesa) {
        this.id_dispesa = id_dispesa;
    }

    public int getEstado_dispesa() {
        return estado_dispesa;
    }

    public void setEstado_dispesa(int estado_dispesa) {
        this.estado_dispesa = estado_dispesa;
    }

    public double getCusto_dispesa() {
        return custo_dispesa;
    }

    public void setCusto_dispesa(double custo_dispesa) {
        this.custo_dispesa = custo_dispesa;
    }

    public String getNome_dispesa() {
        return nome_dispesa;
    }

    public void setNome_dispesa(String nome_dispesa) {
        this.nome_dispesa = nome_dispesa;
    }

    public String getDescricao_dispesa() {
        return descricao_dispesa;
    }

    public void setDescricao_dispesa(String descricao_dispesa) {
        this.descricao_dispesa = descricao_dispesa;
    }

    public String getCategoria_dispesa() {
        return categoria_dispesa;
    }

    public void setCategoria_dispesa(String categoria_dispesa) {
        this.categoria_dispesa = categoria_dispesa;
    }

    public String getData_registo() {
        return data_registo;
    }

    public void setData_registo(String data_registo) {
        this.data_registo = data_registo;
    }

    public String getData_pagamento() {
        return data_pagamento;
    }

    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = data_pagamento;
    }
}
