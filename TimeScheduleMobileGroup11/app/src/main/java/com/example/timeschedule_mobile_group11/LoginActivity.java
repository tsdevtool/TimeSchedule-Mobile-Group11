package com.example.timeschedule_mobile_group11;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timeschedule_mobile_group11.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    AloadingDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new AloadingDialog(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(LoginActivity.this, MainActivity.class);

                loading.show();
                Handler handler= new Handler();
                Runnable runnable= new Runnable() {
                    @Override
                    public void run() {
                        loading.cancel();
                        startActivity(myIntent);
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    }
                };
                handler.postDelayed(runnable,2000);


            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });


    }
}