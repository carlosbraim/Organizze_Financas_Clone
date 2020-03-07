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
import com.example.idene.organizze.helper.Base64Custom;
import com.example.idene.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //alterar o titulo do ActionBar
        getSupportActionBar().setTitle("Cadastro");

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperar o que o usuario digitou para não deixar espaço em branco
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                //verificar campos preenchidos
                if (!textoNome.isEmpty()){//! se o texto não esta vazio
                    if (!textoEmail.isEmpty()){
                        if (!textoSenha.isEmpty()){

                            //cadastrar o usuario
                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            cadastrarUsuario();

                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha o campo senha", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CadastroActivity.this,"Preencha o campo email", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha o campo nome", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void cadastrarUsuario(){

        //Recuperar a instancia (objeto) do fire base que permite fazer autenticacao do usuario
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {//verificar se o cadastro fui um sucesso
                if (task.isSuccessful()){
                    //Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar o usuario", Toast.LENGTH_SHORT).show();

                    //salvar dados do usuario usando o firebase com o email em base 64
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    finish();
                }else {

                    String excecao ="";
                    try {
                        //lançar a exceção
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite uma email valido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar o usuario:" + e.getMessage();
                        e.printStackTrace();//printar a excecao no log
                    }

                    Toast.makeText(CadastroActivity.this,excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
