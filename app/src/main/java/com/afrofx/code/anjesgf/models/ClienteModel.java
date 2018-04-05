package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/20/2018.
 */

public class ClienteModel {

    private String nomeCliente, emailCliente, dataRegistoCliente;
    private int idCliente, numeroCliente;

    public ClienteModel(String nomeCliente, int numeroCliente) {
        this.nomeCliente = nomeCliente;
        this.numeroCliente = numeroCliente;
    }

    public ClienteModel(String nomeCliente, int idCliente, int numeroCliente) {
        this.nomeCliente = nomeCliente;
        this.idCliente = idCliente;
        this.numeroCliente = numeroCliente;
    }

    public ClienteModel(){

    }

    public ClienteModel(String nomeCliente, String emailCliente, String dataRegistoCliente, int idCliente, int numeroCliente) {
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.dataRegistoCliente = dataRegistoCliente;
        this.idCliente = idCliente;
        this.numeroCliente = numeroCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getDataRegistoCliente() {
        return dataRegistoCliente;
    }

    public void setDataRegistoCliente(String dataRegistoCliente) {
        this.dataRegistoCliente = dataRegistoCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(int numeroCliente) {
        this.numeroCliente = numeroCliente;
    }
}
