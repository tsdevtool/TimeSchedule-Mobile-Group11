package com.example.timeschedule_mobile_group11;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firebase.Firebase;
import com.example.timeschedule_mobile_group11.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    AloadingDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new AloadingDialog(this);


        //Xu ly firebase
        Firebase.loadFirebase();

        //Xu ly dang nhap
        addEvents();
    }

    private void addEvents() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Xu ly dang nhap
                login();

            }
        });
        binding.btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent =  new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void login() {
        String email, password;
        email = binding.edtUserCode.getText().toString();
        password = binding.edtPassword.getText().toString();

        if(TextUtils.isEmpty(email) ){
            Toast.makeText(this, "Vui lòng nhập email hoặc mã số của bạn!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password) ){
            Toast.makeText(this, "Vui lòng nhập mật khẩu!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Firebase.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //Hiển thị loading khi chuyển trang
                    loading.show();
                    Handler handler= new Handler();
                    Runnable runnable= new Runnable() {
                        @Override
                        public void run() {
                            loading.cancel();
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        }
                    };
                    handler.postDelayed(runnable,2000);
                }else{
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}