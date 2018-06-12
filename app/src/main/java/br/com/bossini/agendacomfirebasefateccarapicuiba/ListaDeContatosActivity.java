package br.com.bossini.agendacomfirebasefateccarapicuiba;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class ListaDeContatosActivity extends AppCompatActivity {
    private ListView contatosListView;
    private ArrayAdapter <Contato> contatoArrayAdapter;
    private List <Contato> contatos;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contatosReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configuraDatabase();
        contatos = new LinkedList <Contato> ();
        contatoArrayAdapter =
                new ArrayAdapter <Contato> (this, android.R.layout.simple_list_item_1, contatos);
        contatosListView =
                findViewById(R.id.contatosListView);
        contatosListView.setAdapter(contatoArrayAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDeContatosActivity.this, AdicionaContatoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configuraDatabase (){
        firebaseDatabase = FirebaseDatabase.getInstance();
        contatosReference = firebaseDatabase.getReference("contatos");
    }

    @Override
    protected void onStart() {
        super.onStart();
        contatosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contatos.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Contato contato = filho.getValue(Contato.class);
                    contato.setId(filho.getKey());
                    contatos.add(contato);
                }
                contatoArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListaDeContatosActivity.this,
                        getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
