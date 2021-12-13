package com.example.randomchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.randomchat.MainActivity;
import com.example.randomchat.R;
import com.example.randomchat.Utente;
import com.example.randomchat.tasks.RetrieveImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        final Utente logged_user = intent.getParcelableExtra("utente");
        TextView username_text = (TextView) findViewById(R.id.textUsername);
        username_text.setText(logged_user.getUsername());
        imageprofile = findViewById(R.id.imageProfile);
        RetrieveImageTask task = new RetrieveImageTask();
        try {
            imageprofile.setImageBitmap(Bitmap.createScaledBitmap(task.execute(logged_user.getProfile_image()).get() , 200,200,false));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                    case R.id.profile:
                        return true;
                    case R.id.addRoom:
                        Intent intent1 = new Intent(getApplicationContext(), AddRoomActivity.class);
                        intent1.putExtra("utente",logged_user);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }


}