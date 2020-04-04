package com.afrofx.code.anjesgf.models;

public class NotificationModel {

    private String data_notificacao, msg_notificacao,tipo_notificacao, hora_notificacao;


    public NotificationModel(String tipo_notificacao,String msg_notificacao, String hora_notificacao, String data_notificacao) {
        this.data_notificacao = data_notificacao;
        this.msg_notificacao = msg_notificacao;
        this.tipo_notificacao = tipo_notificacao;
        this.hora_notificacao = hora_notificacao;
    }

    public String getHora_notificacao() {
        return hora_notificacao;
    }

    public void setHora_notificacao(String hora_notificacao) {
        this.hora_notificacao = hora_notificacao;
    }

    public String getData_notificacao() {
        return data_notificacao;
    }

    public void setData_notificacao(String data_notificacao) {
        this.data_notificacao = data_notificacao;
    }

    public String getMsg_notificacao() {
        return msg_notificacao;
    }

    public void setMsg_notificacao(String msg_notificacao) {
        this.msg_notificacao = msg_notificacao;
    }

    public String getTipo_notificacao() {
        return tipo_notificacao;
    }

    public void setTipo_notificacao(String tipo_notificacao) {
        this.tipo_notificacao = tipo_notificacao;
    }
}
