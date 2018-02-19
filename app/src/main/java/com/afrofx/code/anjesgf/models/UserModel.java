package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/5/2018.
 */

public class UserModel {

    private int id_usuario, id_endereco, id_cargo, usuario_numero, usuario_password;
    private String usuario_nome, usuario_email, usuario_data_reg;
    byte[] usuario_imagem;

    public UserModel() {
    }

    public UserModel(int usuario_numero, int usuario_password, String usuario_nome, String usuario_email) {
        this.usuario_numero = usuario_numero;
        this.usuario_password = usuario_password;
        this.usuario_nome = usuario_nome;
        this.usuario_email = usuario_email;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_endereco() {
        return id_endereco;
    }

    public void setId_endereco(int id_endereco) {
        this.id_endereco = id_endereco;
    }

    public int getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(int id_cargo) {
        this.id_cargo = id_cargo;
    }

    public int getUsuario_numero() {
        return usuario_numero;
    }

    public void setUsuario_numero(int usuario_numero) {
        this.usuario_numero = usuario_numero;
    }

    public int getUsuario_password() {
        return usuario_password;
    }

    public void setUsuario_password(int usuario_password) {
        this.usuario_password = usuario_password;
    }

    public String getUsuario_nome() {
        return usuario_nome;
    }

    public void setUsuario_nome(String usuario_nome) {
        this.usuario_nome = usuario_nome;
    }

    public String getUsuario_email() {
        return usuario_email;
    }

    public void setUsuario_email(String usuario_email) {
        this.usuario_email = usuario_email;
    }

    public byte[] getUsuario_imagem() {
        return usuario_imagem;
    }

    public void setUsuario_imagem(byte[] usuario_imagem) {
        this.usuario_imagem = usuario_imagem;
    }

    public String getUsuario_data_reg() {
        return usuario_data_reg;
    }

    public void setUsuario_data_reg(String usuario_data_reg) {
        this.usuario_data_reg = usuario_data_reg;
    }
}


