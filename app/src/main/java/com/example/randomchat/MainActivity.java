package com.example.randomchat;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomchat.activities.AddRoomActivity;
import com.example.randomchat.activities.ChatActivity;
import com.example.randomchat.activities.ProfileActivity;
import com.example.randomchat.activities.SignInActivity;
import com.example.randomchat.activities.SignUpActivity;

import com.example.randomchat.adapters.RecyclerItemClickListener;
import com.example.randomchat.adapters.RoomAdapter;
import com.example.randomchat.api.ApiClient;
import com.example.randomchat.api.ApiInterface;
import com.example.randomchat.tasks.RetrieveImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    ImageView profile_pic;
    RecyclerView recycler;
    LinearLayoutManager linearLayoutManager;
    RoomAdapter adapter;
    List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final Utente logged_user = intent.getParcelableExtra("utente");
        TextView textUsername = (TextView) findViewById(R.id.textUsername);
        textUsername.setText(logged_user.getUsername());
        profile_pic = findViewById(R.id.profile_foto);
        RetrieveImageTask task = new RetrieveImageTask();
        try {
            profile_pic.setImageBitmap(Bitmap.createScaledBitmap(task.execute(logged_user.getProfile_image()).get() , 110,110,false));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recycler = findViewById(R.id.recyclerRooms);
        linearLayoutManager = new LinearLayoutManager(this);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);
        roomList = new ArrayList<>();

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setHintTextColor(getResources().getColor(R.color.text_color_hint));
        init(logged_user,"a");
        //searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.matches("") && !newText.matches("  ") && !newText.contains("  ")){
                    if(!roomList.isEmpty()){
                        roomList.clear(); }
                    final ArrayList<Room> listOfRooms = new ArrayList<Room>();
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<List<Room>> call = apiInterface.cerca_stanza(newText.toLowerCase(Locale.ROOT));
                    call.enqueue(new Callback<List<Room>>() {
                        @Override
                        public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                for (Room i : response.body()) {
                                    listOfRooms.add(i);

                            }
                            roomList.clear();
                            roomList.addAll(listOfRooms);
                            if (roomList.isEmpty()) {
                                Toast toast = Toast.makeText(MainActivity.this, "Nessun risultato trovato", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            adapter = new RoomAdapter(MainActivity.this, roomList);
                            recycler.setAdapter(adapter);
                            recycler.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recycler, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Room room_selezionata = new Room();
                                    room_selezionata.setNome(roomList.get(position).getNome());
                                    room_selezionata.setHost_name(roomList.get(position).getHost_name());
                                    room_selezionata.setDescription(roomList.get(position).getDescription());
                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    intent.putExtra("room", room_selezionata);
                                    intent.putExtra("utente", logged_user);
                                    startActivity(intent);
                                    //intent put extra nella sala d'attesa
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));
                        }
                    }
                        @Override
                        public void onFailure(Call<List<Room>> call, Throwable t) {
                           t.printStackTrace();
                        }
                    });


                }

                return false;
            }
        });












        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                switch(menuitem.getItemId()){
                    case R.id.addRoom:
                        Intent intent = new Intent(getApplicationContext(), AddRoomActivity.class);
                        intent.putExtra("utente",logged_user);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
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

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent setIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(setIntent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Clicca due volte per uscire", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void init(Utente logged_user , String newText){
        final ArrayList<Room> listOfRooms = new ArrayList<Room>();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Room>> call = apiInterface.cerca_stanza(newText.toLowerCase(Locale.ROOT));
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Room i : response.body()) {
                        listOfRooms.add(i);

                    }
                    roomList.clear();
                    roomList.addAll(listOfRooms);
                    if (roomList.isEmpty()) {
                        Toast toast = Toast.makeText(MainActivity.this, "Nessun risultato trovato", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    adapter = new RoomAdapter(MainActivity.this, roomList);
                    recycler.setAdapter(adapter);
                    recycler.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recycler, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Room room_selezionata = new Room();
                            room_selezionata.setNome(roomList.get(position).getNome());
                            room_selezionata.setHost_name(roomList.get(position).getHost_name());
                            room_selezionata.setDescription(roomList.get(position).getDescription());
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            intent.putExtra("room", room_selezionata);
                                    /*Toast toast = Toast.makeText(MainActivity.this, room_selezionata.getNome(), Toast.LENGTH_LONG);
                                    toast.show();*/
                            intent.putExtra("utente", logged_user);
                            startActivity(intent);
                            //intent put extra nella sala d'attesa
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
                }
            }
            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }



}