package com.example.idene.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idene.organizze.R;
import com.example.idene.organizze.adapter.AdapterMovimentacao;
import com.example.idene.organizze.config.ConfiguracaoFirebase;
import com.example.idene.organizze.helper.Base64Custom;
import com.example.idene.organizze.model.Movimentacao;
import com.example.idene.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, texoSaldo;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;//uma unica movimentacao
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;//objeto para tratar e receber o ValueEventListener
    private ValueEventListener valueEventListenerMovimentacoes;//objeto para tratar e receber o ValueEventListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);


        texoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentos);
        configuraCalendarView();
        swipe();


        //Configurar adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);


        //Comfigurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);

    }

    //metodo responsalvel por fazer o elemento do RecyclerView ser apagado apos arrastar para direita ou esquerda
    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;//ACTION_STATE_IDLE deixar o movimento de arrastar desativado
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;//arrastar do inicio ao fim
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 //Log.i("Item","Item arrastado");
                excluirMovimentacao(viewHolder);//viewHolder é ultilizada para recuperar a posicao do item da lista
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //configurando AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);//recupera movimentacao atraves da posicao do RecyclerView

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                movimentacaoRef = firebaseRef.child("movimentacao")//criar a movimentacaoRef usando a refenrencia firebaseRef que ja implementa o database
                        .child(idUsuario)
                        .child( mesAnoSelecionado );

                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atulizarSaldo();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this,"Cancelado", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atulizarSaldo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        if (movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }

        if (movimentacao.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }
    }


    public void recuperarMovimentacoes(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("movimentacao")//criar a movimentacaoRef usando a refenrencia firebaseRef que ja implementa o database
                                     .child(idUsuario)
                                     .child( mesAnoSelecionado );
        //Log.i("Mes"," mes " + mesAnoSelecionado);

        //recuperar os dados de movimentacoes
        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                movimentacoes.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){//getValue recupera um objeto ja o getChildren recupera todos
                    //Log.i("dados","retorno:" + dados.toString());

                    Movimentacao movimentacao = dados.getValue( Movimentacao.class);//recupera uma movimentacao
                    //Log.i("dadosRetorno","dados:" + movimentacao.getCategoria());
                    movimentacao.setKey(dados.getKey());//recuperar a chave do firebase criada pelo firebase que referencia cada dado referente ao mes
                    movimentacoes.add(movimentacao);
                }

                adapterMovimentacao.notifyDataSetChanged();//avisar o adapter q os dados foram mudados

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void recuperarResumo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        //Log.i("Evento", "Evento foi adicionado" );

        //recuperar dados
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");//representa digitos mais sem o 0 #.## explo 800.00 os dois 00 n aparece
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                textoSaudacao.setText("Olá, " + usuario.getNome());
                texoSaldo.setText("R$ " + resultadoFormatado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //configurar o boao elevado
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);//vaiconverter o xml em uma view
        return super.onCreateOptionsMenu(menu);
    }
    //acao do botao elevado


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuSair :
                autenticacao.signOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(this,DespesasActivity.class));
    }
    public void adicionarReceita(View view){
        startActivity(new Intent(this,ReceitasActivity.class));
    }

    public void configuraCalendarView(){

        //valores para os meses
        CharSequence meses[] = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho",
                "Agosto","Setembro","Otubro","Novembro","Dezembro"};
        calendarView.setTitleMonths(meses);

        //recuperar o mes e ano para passar para o RecyclerView
        CalendarDay dataAtual =calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d",(dataAtual.getMonth()+1));//formatacao para ficar 01,02....0,9
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                  String mesSelecionado = String.format("%02d",(date.getMonth()+1));//formatacao para ficar 01,02....0,9
                  mesAnoSelecionado = String.valueOf( mesSelecionado + "" + date.getYear());
                //Log.i("Mes"," mes " + mesAnoSelecionado);
                //recuperar e listar as movimentacoes
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);//para n ter varias movimentacoes adicionadas removendo a anterior
                recuperarMovimentacoes();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.i("Evento", "Evento foi removido" );
        usuarioRef.removeEventListener( valueEventListenerUsuario ); // remover o evento sempre q o usuario não estiver usando o app
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}
