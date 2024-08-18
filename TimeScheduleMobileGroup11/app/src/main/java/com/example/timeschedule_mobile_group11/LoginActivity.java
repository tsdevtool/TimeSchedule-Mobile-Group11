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
import com.example.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    AloadingDialog loading;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        init();

        addEvents();
    }
    private void init(){
        Intent intent = getIntent();
        int notificationId = intent.getIntExtra(Util.NOTIFICATION, 0);
        if(notificationId == 1){
            notifcation(R.string.toastCheckPasswordInEmail);
            binding.edtUser.setText(intent.getStringExtra(Util.EMAIL));
        }
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
                Intent myIntent =  new Intent(LoginActivity.this, FogotPasswordActivity.class);
                startActivity(myIntent);
            }
        });
        binding.imvLogo.setOnClickListener(new View.OnClickListener() {
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
                            updateInfomation(email, password);
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

    private void updateInfomation(String email, String password) {
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    String userId = snap.getKey();

                    usersRef.child(userId).child("password").setValue(password)
                            .addOnCompleteListener(task -> {

                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                notifcation(R.string.error);
            }
        });
    }

    private void notifcation(int notification){
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.activity_notification_dialog);
        Button back = dialog.findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView notificationBody = dialog.findViewById(R.id.txtNotification);
        notificationBody.setText(notification);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}