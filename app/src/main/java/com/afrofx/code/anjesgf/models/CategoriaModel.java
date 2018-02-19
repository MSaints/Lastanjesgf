package com.afrofx.code.anjesgf.models;

/**
 * Created by Afro FX on 2/12/2018.
 */

public class CategoriaModel {

    private String categoria;
    private int categoria_id;

    public CategoriaModel() {
    }

    public CategoriaModel( int categoria_id, String categoria) {
        this.categoria = categoria;
        this.categoria_id = categoria_id;
    }

    public CategoriaModel(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }
}
