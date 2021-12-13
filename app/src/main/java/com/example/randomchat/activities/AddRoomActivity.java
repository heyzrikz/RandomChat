package com.example.randomchat.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.randomchat.MainActivity;
import com.example.randomchat.R;
import com.example.randomchat.Room;
import com.example.randomchat.Utente;
import com.example.randomchat.api.ApiClient;
import com.example.randomchat.api.ApiInterface;
import com.example.randomchat.api.Note;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoomActivity extends AppCompatActivity {

    Button creaBtn;
    EditText nomeRoom,descrizioneRoom;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        Intent intent = getIntent();
        final Utente logged_user = intent.getParcelableExtra("utente");
        nomeRoom = findViewById(R.id.roomName);
        descrizioneRoom = findViewById(R.id.descriptionRoom);
        creaBtn = findViewById(R.id.creaBtn);
        creaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nomeRoom.getText().toString().matches("") || descrizioneRoom.getText().toString().matches("")){
                    Toast toast = Toast.makeText(AddRoomActivity.this, "Attenzione ci sono dei campi vuoti", LENGTH_SHORT);
                    toast.show();
                }else{
                    Room room = new Room();
                    room.setNome(nomeRoom.getText().toString());
                    room.setDescription(descrizioneRoom.getText().toString());
                    room.setHost_name(logged_user.getUsername());
                    verifica_room(room , logged_user);







                }
            }
        });



        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.addRoom);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                switch(menuitem.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("utente",logged_user);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.addRoom:
                        return true;
                    case R.id.profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent1.putExtra("utente",logged_user);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    public void showConfirmDialog(Room stanza ,Utente logged){

        AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomActivity.this);
        builder.setMessage("Sei sicuro di voler creare la stanza "+nomeRoom.getText().toString()+"?")
                .setCancelable(false)
                .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        stanza.setNome(stanza.getNome().toLowerCase(Locale.ROOT));
                       inserisci_Room(stanza);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("utente",logged);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    public void verifica_room(Room stanza, Utente logged){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.searchRoomName(stanza.getNome());
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getMessage().matches("false")) {
                        showConfirmDialog(stanza , logged);
                    } else {
                        Toast toast = Toast.makeText(AddRoomActivity.this, "Nome della stanza già in uso", LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {

            }
        });
    }

    public void inserisci_Room(Room stanza){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Room> call = apiInterface.crea_stanza(stanza.getNome(),stanza.getDescription(),stanza.getHost_name());
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {

            }
        });


    }



}