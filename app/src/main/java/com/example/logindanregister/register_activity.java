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
import com.example.logindanregister.model.register.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register_activity extends AppCompatActivity implements View.OnClickListener{

    EditText etUsername, etPassword, etName;
    Button btnRegister;

    TextView tvLogin;

    String Username, Password, Name;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRegisterUsername);
        etName = findViewById(R.id.etRegisterName);
        etPassword = findViewById(R.id.etRegisterPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);



        tvLogin = findViewById(R.id.tvLoginHere);
        tvLogin.setOnClickListener(this);





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                Username = etUsername.getText().toString();
                Name = etName.getText().toString();
                Password = etPassword.getText().toString();
                register(Username,Name,Password);
                break;
            case R.id.tvLoginHere:
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
                break;
        }
    }
    private void register(String username, String password, String name){
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<Register> call= apiInterface.RegisterResponse(username, password, name);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    Toast.makeText(register_activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(register_activity.this, login.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(register_activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(register_activity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}