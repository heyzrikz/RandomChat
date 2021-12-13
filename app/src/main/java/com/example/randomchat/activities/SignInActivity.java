package com.example.randomchat.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomchat.MainActivity;
import com.example.randomchat.R;
import com.example.randomchat.Utente;
import com.example.randomchat.api.ApiClient;
import com.example.randomchat.api.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signup;
    private EditText editUsername;
    private EditText editPassword;
    ApiInterface apiInterface;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        View someView = findViewById(R.id.create_new_account);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(R.color.white));
        progress = findViewById(R.id.progress_bar);
        editUsername = (EditText) findViewById(R.id.username_input);
        editPassword = (EditText) findViewById(R.id.password_input);
        signup = (TextView) findViewById(R.id.create_new_account);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginButton = (Button)findViewById(R.id.signIn_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.INVISIBLE);
                if(editUsername.getText().toString().matches("") || editPassword.getText().toString().matches("")){
                    loginButton.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(SignInActivity.this, "Attenzione ci sono dei campi vuoti", LENGTH_SHORT);
                    toast.show();
                }else{

                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<List<Utente>> call = apiInterface.login(editUsername.getText().toString(),editPassword.getText().toString());
                    call.enqueue(new Callback<List<Utente>>() {
                        @Override
                        public void onResponse(Call<List<Utente>> call, Response<List<Utente>> response) {
                            if(response.isSuccessful() && response.body() != null){
                                Utente user = new Utente();
                                user.setUsername(response.body().get(0).getUsername());
                                //user.setPassword(response.body().get(0).getPassword());
                                user.setEmail(response.body().get(0).getEmail());
                                user.setProfile_image(response.body().get(0).getProfile_image());
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                intent.putExtra("utente",user);
                                startActivity(intent);
                                loginButton.setVisibility(View.VISIBLE);
                            }else{
                                loginButton.setVisibility(View.VISIBLE);
                                Toast toast = Toast.makeText(SignInActivity.this, "Utente non trovato", LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Utente>> call, Throwable t) {
                            loginButton.setVisibility(View.VISIBLE);
                            t.printStackTrace();
                            Toast toast = Toast.makeText(SignInActivity.this, "Utente non trovato", LENGTH_SHORT);
                            toast.show();

                        }
                    });

                }
            }
        });

    }
}