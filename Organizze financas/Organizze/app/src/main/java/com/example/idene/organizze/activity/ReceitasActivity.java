package com.example.idene.organizze.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.idene.organizze.R;
import com.example.idene.organizze.config.ConfiguracaoFirebase;
import com.example.idene.organizze.helper.Base64Custom;
import com.example.idene.organizze.helper.DateCustom;
import com.example.idene.organizze.model.Movimentacao;
import com.example.idene.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData,campoCategoria,campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
        //recuper o valor da despesa antes de atribuir um novo evento
        recuperarReceitaTotal();
    }


    public void salvarReceita(View view){

        if ( validarCamposReceita() ){

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);//converter string em Double
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");


            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizaReceita( receitaAtualizada );

            movimentacao.salvar(data);

            finish();
        }



    }


    public Boolean validarCamposReceita(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()){
                if (!textoCategoria.isEmpty()){
                    if (!textoDescricao.isEmpty()){
                        return true;
                    }else {
                        Toast.makeText(ReceitasActivity.this,"Descricao não foi preenchido", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(ReceitasActivity.this,"Categoria não foi preenchido", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(ReceitasActivity.this,"Data não foi preenchido", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(ReceitasActivity.this,"Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public void recuperarReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //criando um ouvinte
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue( Usuario.class);//processo inverso pegando os dados da base e alocando na base classe usuario pois la tem os campos
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    public void atualizaReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}
