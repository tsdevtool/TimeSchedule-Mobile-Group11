package com.example.timeschedule_mobile_group11;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.User;
import com.example.timeschedule_mobile_group11.databinding.ActivityRegisterBinding;
import com.example.utils.Password;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    Calendar calendar;
    AloadingDialog loading;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new AloadingDialog(this);

//        Xu ly thoi gian
        TimeHandling();

//        Xu ly dang ky tai khoan
        addEvents();
    }

    private void addEvents() {
//        Xu ly dang ky tai khoan
        binding.btnRegisterSucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri imageUri;
                User user = new User();
                user.setUserCode(binding.edtUserCode.getText().toString());
                user.setEmail(binding.edtEmail.getText().toString());
                user.setFullName(binding.edtFullname.getText().toString());
//        user.setAvatar(R.drawable.user_icon.);
                user.setPassword(binding.edtPassword.getText().toString());
                user.setDayOfBirth(binding.edtDayOfBirth.getText().toString());
                if(binding.chkMale.isChecked()){
                    user.setSex(true);
                }else{
                    user.setSex(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    user.setDayAdmission(String.valueOf(LocalDate.now()));
                }

                registerUser(user.getEmail().toLowerCase().trim(), user.getPassword().trim(), user);
            }
        });
    }

    private void registerUser(String email, String password, User user) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //Lay userId khi dang ky thanh cong
                        String userId = mAuth.getCurrentUser().getUid();
                        //Luu thong tin vao Firebase Database
                        saveUserData(userId, user);
                    }else{
                        // Xử lý lỗi
                        Log.w("Đăng ký thất bại", "Đăng ký tài khoản không thành công", task.getException());
                    }
                });
    }

    private void saveUserData(String userId, User user) {
        usersRef.child(userId).setValue(user).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               //Thong bao thanh cong
               Intent myIntent =  new Intent(RegisterActivity.this, MainActivity.class);
               loading.show();
               Handler handler= new Handler();
               Runnable runnable= new Runnable() {
                   @Override
                   public void run() {
                       loading.cancel();
                       startActivity(myIntent);
                       Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                   }
               };
               handler.postDelayed(runnable,2000);
               finish();
           }else{
               Toast.makeText(this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
           }
        });
    }

//    private void addEvents() {
//        binding.btnRegisterSucess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String email, password;
//                email = binding.edtUsername.getText().toString().trim();
////                password = Password.generatePassword(12).trim();
//                password = binding.edtFullname.getText().toString().trim();
//
//                if(TextUtils.isEmpty(email) ){
//                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập email hoặc mã số của bạn!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(TextUtils.isEmpty(password) ){
//                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập mật khẩu!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Intent myIntent =  new Intent(RegisterActivity.this, LoginActivity.class);
//                            loading.show();
//                            Handler handler= new Handler();
//                            Runnable runnable= new Runnable() {
//                                @Override
//                                public void run() {
//                                    loading.cancel();
//                                    startActivity(myIntent);
//                                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
//
//                                }
//                            };
//                            handler.postDelayed(runnable,2000);
//                            finishAffinity();
//                        }else{
//
//                            Toast.makeText(RegisterActivity.this, "Tạo tài khoản không thành công!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//
//            }
//        });
//    }

    private void TimeHandling() {
        calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, dayOfMonth);

                updatelabel();


            }
        };
        binding.edtDayOfBirth.setOnClickListener(view->{
            new DatePickerDialog(RegisterActivity.this,date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updatelabel() {

        String myFormat= "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat( myFormat, Locale.US);
        binding.edtDayOfBirth.setText(dateFormat.format(calendar.getTime()));
    }

}