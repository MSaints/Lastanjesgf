package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/18/2018.
 */

public class ContaModel {

    private String nomeConta;
    private double saldoConta;
    private long id_conta, id_operacao;

    public ContaModel() {
    }


    public ContaModel(String nomeConta, double saldoConta, long id_conta) {
        this.nomeConta = nomeConta;
        this.saldoConta = saldoConta;
        this.id_conta = id_conta;
    }

    public ContaModel(double saldoConta, long id_conta, long id_operacao) {
        this.saldoConta = saldoConta;
        this.id_conta = id_conta;
        this.id_operacao = id_operacao;
    }

    public ContaModel(String nomeConta) {
        this.nomeConta = nomeConta;
    }

    public long getId_operacao() {
        return id_operacao;
    }

    public void setId_operacao(long id_operacao) {
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

    public long getId_conta() {
        return id_conta;
    }

    public void setId_conta(long id_conta) {
        this.id_conta = id_conta;
    }
}
