package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 3/12/2018.
 */

public class RegistoVendaModel {
    private int id_user, id_registo_operacao, id_cliente, id_registo_venda, venda_estado;
    private String venda_data_expiracao;
    private double venda_desconto;

    public RegistoVendaModel(int id_user, int id_registo_operacao, int venda_estado) {
        this.id_user = id_user;
        this.id_registo_operacao = id_registo_operacao;
        this.venda_estado = venda_estado;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_registo_operacao() {
        return id_registo_operacao;
    }

    public void setId_registo_operacao(int id_registo_operacao) {
        this.id_registo_operacao = id_registo_operacao;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_registo_venda() {
        return id_registo_venda;
    }

    public void setId_registo_venda(int id_registo_venda) {
        this.id_registo_venda = id_registo_venda;
    }

    public int getVenda_estado() {
        return venda_estado;
    }

    public void setVenda_estado(int venda_estado) {
        this.venda_estado = venda_estado;
    }

    public String getVenda_data_expiracao() {
        return venda_data_expiracao;
    }

    public void setVenda_data_expiracao(String venda_data_expiracao) {
        this.venda_data_expiracao = venda_data_expiracao;
    }

    public double getVenda_desconto() {
        return venda_desconto;
    }

    public void setVenda_desconto(double venda_desconto) {
        this.venda_desconto = venda_desconto;
    }
}
