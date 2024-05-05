package com.example.logindanregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.logindanregister.api.ApiClient;
import com.example.logindanregister.api.ApiInterface;
import com.example.logindanregister.model.login.Login;
import com.example.logindanregister.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnlogin;

    String Username, Password;

    TextView tvregister;

    ApiInterface apiInterface;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager;

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnlogin = findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(this);

        tvregister = findViewById(R.id.tvCreateAccount);
        tvregister.setOnClickListener(this);









        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                Username = etUsername.getText().toString();
                Password = etPassword.getText().toString();
                login(Username,Password);
                break;
            case R.id.tvCreateAccount:
                Intent intent = new Intent(this, register_activity.class);
                startActivity(intent);
                break;


        }
    }
    private void login(String username, String password){


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.LoginResponse(username, password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                sessionManager = new SessionManager(login.this);
                LoginData loginData = response.body().getData();
                    sessionManager.createLoginSession(loginData);






                    Toast.makeText(login.this, response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(login.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }
}