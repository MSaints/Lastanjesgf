package com.afrofx.code.anjesgf.models;

public class UserModel {

    private long id_usuario, id_endereco, id_cargo, usuario_numero, usuario_password;
    private String usuario_nome, usuario_email, usuario_data_reg, cargo_nome;
    byte[] usuario_imagem;




    public UserModel() {

    }

    public UserModel(long usuario_numero, long usuario_password, String usuario_nome, byte[] usuario_imagem) {
        this.usuario_numero = usuario_numero;
        this.usuario_password = usuario_password;
        this.usuario_nome = usuario_nome;
        this.usuario_imagem = usuario_imagem;
    }


    public UserModel(String usuario_data_reg, String usuario_nome, String cargo_nome,long usuario_numero) {
        this.usuario_data_reg = usuario_data_reg;
        this.usuario_nome = usuario_nome;
        this.cargo_nome = cargo_nome;
        this.usuario_numero = usuario_numero;
    }





    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public long getId_endereco() {
        return id_endereco;
    }

    public void setId_endereco(long id_endereco) {
        this.id_endereco = id_endereco;
    }

    public long getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(long id_cargo) {
        this.id_cargo = id_cargo;
    }

    public long getUsuario_numero() {
        return usuario_numero;
    }

    public void setUsuario_numero(long usuario_numero) {
        this.usuario_numero = usuario_numero;
    }

    public long getUsuario_password() {
        return usuario_password;
    }

    public void setUsuario_password(long usuario_password) {
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


    public String getCargo_nome() {
        return cargo_nome;
    }

    public void setCargo_nome(String cargo_nome) {
        this.cargo_nome = cargo_nome;
    }
}


