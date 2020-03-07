package com.example.idene.organizze.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.idene.organizze.R;
import com.example.idene.organizze.activity.CadastroActivity;
import com.example.idene.organizze.activity.LoginActivity;
import com.example.idene.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();
    }

    //verificar se o usuario esta logado
    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }


    public  void btEntrar(View view){
        startActivity(new Intent(this,LoginActivity.class));
    }

    public  void btCadastrar(View view){
        startActivity(new Intent(this,CadastroActivity.class));
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }

    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this,PrincipalActivity.class));
    }


}
