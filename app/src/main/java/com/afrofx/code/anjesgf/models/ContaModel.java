package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/18/2018.
 */

public class ContaModel {

    private String nomeConta;
    private double saldoConta;
    private int id_conta, id_operacao;

    public ContaModel(String nomeConta, double saldoConta, int id_conta) {
        this.nomeConta = nomeConta;
        this.saldoConta = saldoConta;
        this.id_conta = id_conta;
    }

    public ContaModel(double saldoConta, int id_conta, int id_operacao) {
        this.saldoConta = saldoConta;
        this.id_conta = id_conta;
        this.id_operacao = id_operacao;
    }

    public ContaModel(String nomeConta) {
        this.nomeConta = nomeConta;
    }

    public int getId_operacao() {
        return id_operacao;
    }

    public void setId_operacao(int id_operacao) {
        this.id_operacao = id_operacao;
    }

    public double getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(double saldoConta) {
        this.saldoConta = saldoConta;
    }

    public String getNomeConta() {
        return nomeConta;
    }

    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
    }

    public int getId_conta() {
        return id_conta;
    }

    public void setId_conta(int id_conta) {
        this.id_conta = id_conta;
    }
}
