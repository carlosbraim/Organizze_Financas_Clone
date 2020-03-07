package com.example.idene.organizze.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.idene.organizze.R;
import com.example.idene.organizze.config.ConfiguracaoFirebase;
import com.example.idene.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail,campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){

                        //Logar o usuario
                        usuario = new Usuario();
                        //usuario.setNome(textoNome);
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this,"Preencha o campo senha", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"Preencha o campo email", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
              usuario.getEmail(),
              usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    abrirTelaPrincipal();
                    //Toast.makeText(LoginActivity.this,"Sucesso ao fazer login", Toast.LENGTH_SHORT).show();

                }else {

                    String excecao ="";
                    try {
                        //lançar a exceção
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuario não esta cadastrado.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email e senha não correspondem a um usuário cadastrado.";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar o usuario:" + e.getMessage();
                        e.printStackTrace();//printar a excecao no log
                    }

                    Toast.makeText(LoginActivity.this,excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this,PrincipalActivity.class));
    }
}
