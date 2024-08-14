package com.example.timeschedule_mobile_group11;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.timeschedule_mobile_group11.databinding.ActivityLoginBinding;
import com.example.timeschedule_mobile_group11.dialog.AloadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    AloadingDialog loading;

    boolean isPasswordVisible = false;
    //Khai bao cac bien lien quan toi database
    private FirebaseDatabase database;
    private DatabaseReference myDef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Khởi tạo Firebase
//        database = FirebaseDatabase.getInstance();
//        myDef = database.getReference().child("user");

//        // Tạo đối tượng User mới
//        String id = myDef.push().getKey();
//        String username = "newuser";
//        String fullname = "New User";
//        String password = "newpassword123";
//        String avatar = "avatar_url";
//        String email = "newuser@example.com";
//        Date dayAdmission = new Date();
//        int classId = 102;
//        int facultyId = 11;
//        int roleId = 2;
//
//        User newUser = new User(id, username, fullname, password, avatar, email, dayAdmission, classId, facultyId, roleId);
//        myDef.child(id).setValue(newUser);




        // Hiển thị mật khẩu

        //Cách 1 ẩn hiển password bằng checkbox có note bên activity_login
        /*binding.chkShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Hide password
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Move cursor to end of text
                binding.edtPassword.setSelection(binding.edtPassword.getText().length());
            }
        });*/

        //Cách 2: Ẩn hiện icon bằng password
        binding.edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtPassword.getRight() - binding.edtPassword.getCompoundDrawables()[2].getBounds().width())) {
                        // Toggle password visibility
                        if (isPasswordVisible) {
                            binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_26, 0, R.drawable.eye_26, 0);
                        } else {
                            binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_26, 0, R.drawable.invisible_26, 0);
                        }
                        isPasswordVisible = !isPasswordVisible;
                        binding.edtPassword.postInvalidate();
                        binding.edtPassword.setSelection(binding.edtPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        loading = new AloadingDialog(this);



        //Xu ly firebase
//        Firebase.loadFirebase();

//                loading.show();
//                Handler handler= new Handler();
//                Runnable runnable= new Runnable() {
//                    @Override
//                    public void run() {
//                        loading.cancel();
//                        startActivity(myIntent);
//                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

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
        email = binding.edtUser.getText().toString().trim();
        password = binding.edtPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email) ){
            Toast.makeText(LoginActivity.this, "Vui lòng nhập email hoặc mã số của bạn!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password) ){
            Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu!!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                            finish();
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