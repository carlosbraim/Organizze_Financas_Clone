package com.example.idene.organizze.model;

import com.example.idene.organizze.config.ConfiguracaoFirebase;
import com.example.idene.organizze.helper.Base64Custom;
import com.example.idene.organizze.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;
    private String key;

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();//objeto para recuperar o email
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesanoDataEcolhida(dataEscolhida);
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()//criar id firebase
                .setValue(this);

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
