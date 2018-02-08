package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/5/2018.
 */

public class UserModel {



    public String getUser_nome() {
        return user_nome;
    }

    public int getUser_numero() {
        return user_numero;
    }

    public int getUser_pin() {
        return user_pin;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_reg_date() {
        return user_reg_date;
    }

    public String getUser_login_date() {
        return user_login_date;
    }

    public int getUser_localidade_id() {
        return user_localidade_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getUser_cargo_id() {
        return user_cargo_id;
    }

    public void setUser_nome(String user_nome) {
        this.user_nome = user_nome;
    }

    public void setUser_numero(int user_numero) {
        this.user_numero = user_numero;
    }

    public void setUser_pin(int user_pin) {
        this.user_pin = user_pin;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_reg_date(String user_reg_date) {
        this.user_reg_date = user_reg_date;
    }

    public void setUser_login_date(String user_login_date) {
        this.user_login_date = user_login_date;
    }

    public void setUser_localidade_id(int user_localidade_id) {
        this.user_localidade_id = user_localidade_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_cargo_id(int user_cargo_id) {
        this.user_cargo_id = user_cargo_id;
    }

    public UserModel(String user_nome, int user_numero, int user_pin, String user_email, String user_reg_date) {
        this.user_nome = user_nome;
        this.user_numero = user_numero;
        this.user_pin = user_pin;
        this.user_email = user_email;
        this.user_reg_date = user_reg_date;
    }

    public UserModel() {
    }

    private String user_nome;
    private int user_numero;
    private int user_pin;
    private String user_email;
    private String user_reg_date;
    private int user_localidade_id;
    private int user_id;
    private int user_cargo_id;
    private String user_login_date;


}


