package com.example.randomchat.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomchat.MainActivity;
import com.example.randomchat.R;
import com.example.randomchat.Utente;
import com.example.randomchat.tasks.RetrieveImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageprofile;
    EditText usernameTxt , passwordTxt , confermapassTxt;
    Button showPass , confirmBtn , logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        final Utente logged_user = intent.getParcelableExtra("utente");
        TextView username_text = (TextView) findViewById(R.id.textUsername);
        imageprofile = findViewById(R.id.imageProfile);
        RetrieveImageTask task = new RetrieveImageTask();
        try {
            imageprofile.setImageBitmap(Bitmap.createScaledBitmap(task.execute(logged_user.getProfile_image()).get() , 200,200,false));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        usernameTxt = findViewById(R.id.textUsername);
        usernameTxt.setText(logged_user.getUsername());
        passwordTxt = findViewById(R.id.textPassword);
        passwordTxt.setText(logged_user.getPassword());
        showPass = findViewById(R.id.showPass);
        showPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        passwordTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordTxt.setSelection(passwordTxt.length());
                        //pressed
                        return true;
                    case MotionEvent.ACTION_UP:
                        passwordTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordTxt.setSelection(passwordTxt.length());
                        //released
                        return true;
                }
                return false;
            }
        });
        confermapassTxt = findViewById(R.id.textConfermaPassword);
        passwordTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                confermapassTxt.setVisibility(View.VISIBLE);
                return false;
            }
        });
        confirmBtn = findViewById(R.id.confirm_mod);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordTxt.getText().toString().matches(logged_user.getPassword()) && usernameTxt.getText().toString().matches(logged_user.getUsername())){
                    Toast toast = Toast.makeText(ProfileActivity.this, "Credenziali non modificate", LENGTH_SHORT);
                    toast.show();
                }else{
                    if(!passwordTxt.getText().toString().matches(logged_user.getPassword()) && !passwordTxt.getText().toString().matches(confermapassTxt.getText().toString())){
                        Toast toast = Toast.makeText(ProfileActivity.this, "Credenziali non modificate", LENGTH_SHORT);
                        toast.show();
                    }else{
                        if(passwordTxt.getText().toString().contains(" ") || passwordTxt.getText().toString().matches("") || usernameTxt.getText().toString().matches("") ||usernameTxt.getText().toString().contains(" ") ){
                            Toast toast = Toast.makeText(ProfileActivity.this, "Credenziali non valide", LENGTH_SHORT);
                            toast.show();
                        }else{
                            //SCRIPT DI CONFERMA MODIFICHE
                            Utente user = new Utente();
                            user.setUsername(usernameTxt.getText().toString());
                            user.setPassword(passwordTxt.getText().toString());
                            user.setEmail(logged_user.getEmail());
                            user.setProfile_image(logged_user.getProfile_image());//da cambiare
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            intent.putExtra("utente",user);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });


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