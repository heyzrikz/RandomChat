package com.example.randomchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomchat.MainActivity;
import com.example.randomchat.R;
import com.example.randomchat.Utente;
import com.example.randomchat.api.ApiClient;
import com.example.randomchat.api.ApiInterface;
import com.example.randomchat.api.Note;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {
    private FrameLayout addImage;
    private ImageView imageView;
    String imageUrl;
    Dialog popUp;
    TextView addImgTxt;
    Button signUp_btn;
    EditText username_txt,password_txt,conferma_password_txt,email_txt;
    ApiInterface apiInterface;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        View someView = findViewById(R.id.signUp_button);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(R.color.white));
        progress = findViewById(R.id.progres_bar);
        imageView = (ImageView) findViewById(R.id.imageProfile);
        popUp = new Dialog(this);
        imageUrl = "";
        addImage = (FrameLayout) findViewById(R.id.imagelayout);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        username_txt = findViewById(R.id.username_input);
        password_txt = findViewById(R.id.password_input);
        conferma_password_txt = findViewById(R.id.confermaPassword_input);
        email_txt = findViewById(R.id.email_input);
    signUp_btn = findViewById(R.id.signUp_button);
    signUp_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signUp_btn.setVisibility(View.INVISIBLE);
            if(username_txt.getText().toString().matches("") || password_txt.getText().toString().matches("") || conferma_password_txt.getText().toString().matches("")
            || email_txt.getText().toString().matches("")){
                signUp_btn.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(SignUpActivity.this, "Attenzione campi vuoti", Toast.LENGTH_LONG);
                toast.show();
            }else{
                if(isEmailValid(email_txt.getText().toString())){
                   if(imageUrl.matches("")){
                       signUp_btn.setVisibility(View.VISIBLE);
                       Toast toast = Toast.makeText(SignUpActivity.this, "Scegli una foto profilo", Toast.LENGTH_LONG);
                       toast.show();
                   }else{
                       if(password_txt.getText().toString().matches(conferma_password_txt.getText().toString())){
                           Utente user = new Utente();
                           user.setUsername(username_txt.getText().toString());
                           user.setPassword(password_txt.getText().toString());
                           user.setEmail(email_txt.getText().toString());
                           user.setProfile_image(imageUrl);
                           verifica_mail(user);


                       }else{
                           signUp_btn.setVisibility(View.VISIBLE);
                           Toast toast = Toast.makeText(SignUpActivity.this, "Attenzione password diverse", Toast.LENGTH_LONG);
                           toast.show();
                       }


                   }
                }else{
                    signUp_btn.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(SignUpActivity.this, "Mail non valida", Toast.LENGTH_LONG);
                    toast.show();
                }


            }

        }
    });

    }
    public void ShowPopup(View v){
        ImageView img1;ImageView img2;ImageView img3;
        ImageView img4;ImageView img5;ImageView img6;
        ImageView img7;ImageView img8;ImageView img9;
        TextView textClose;
        popUp.setContentView(R.layout.popup);
        textClose =(TextView) popUp.findViewById(R.id.close);
        img1 = (ImageView) popUp.findViewById(R.id.img1);
        img2 = (ImageView) popUp.findViewById(R.id.img2);
        img3 = (ImageView) popUp.findViewById(R.id.img3);
        img4 = (ImageView) popUp.findViewById(R.id.img4);
        img5 = (ImageView) popUp.findViewById(R.id.img5);
        img6 = (ImageView) popUp.findViewById(R.id.img6);
        img7 = (ImageView) popUp.findViewById(R.id.img7);
        img8 = (ImageView) popUp.findViewById(R.id.img8);
        img9 = (ImageView) popUp.findViewById(R.id.img9);
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss(); }});
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_1.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_1));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_2.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_2));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_3.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_3));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_4.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_4));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_5.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_5));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_6.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_6));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_7.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_7));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_8.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_8));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl="http://3.135.246.169/profile_picture_9.png";
                imageView = (ImageView) findViewById(R.id.imageProfile);
                imageView.setImageBitmap(BitmapFactory.decodeResource(SignUpActivity.this.getResources(),
                        R.drawable.profile_picture_9));
                addImgTxt = (TextView) findViewById(R.id.addImgTxt);
                addImgTxt.setVisibility(View.INVISIBLE);
                popUp.dismiss();

            }
        });

        popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUp.show();

    }

    void verifica_mail(Utente utente){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.searchEmail(utente.getEmail());
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(response.isSuccessful() && response.body() != null){

                    if(response.body().getMessage().matches("false")){
                        verifica_username(utente);

                    }else{
                        signUp_btn.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(SignUpActivity.this, "Mail già in utilizzo", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
t.printStackTrace();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void verifica_username(Utente utente){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.searchUsername(utente.getUsername());
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getMessage().matches("false")){
                        inserisciUtente_db(utente);
                    }else{
                        signUp_btn.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(SignUpActivity.this, "Username già in uso", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                signUp_btn.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }

    public void inserisciUtente_db(Utente utente){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Utente> call = apiInterface.registrazione(utente.getUsername(),utente.getPassword(),utente.getEmail(),utente.getProfile_image());
        call.enqueue(new Callback<Utente>() {
            @Override
            public void onResponse(Call<Utente> call, Response<Utente> response) {


            }

            @Override
            public void onFailure(Call<Utente> call, Throwable t) {
//t.printStackTrace();
            }
        });
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.putExtra("utente", utente);
        startActivity(intent);
        signUp_btn.setVisibility(View.VISIBLE);


    }



}