package com.example.timeschedule_mobile_group11;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timeschedule_mobile_group11.databinding.ActivityFogotPasswordBinding;
import com.example.utils.JavaMailAPI;
import com.example.utils.Password;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FogotPasswordActivity extends AppCompatActivity {

    AloadingDialog aloadingDialog;

    private FirebaseAuth mAuth;
    private String email;
    ActivityFogotPasswordBinding binding;
    private DatabaseReference databaseReference;

    private String subject;
    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFogotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();

    }

    private void addEvents() {
        binding.btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.edtEmail.getText().toString().toLowerCase().trim();
                if(email.isEmpty()){
                    notifcation(R.string.toastStringEmpty);
//                    Toast.makeText(FogotPasswordActivity.this, "Vui lòng không để trống thông tin!!", Toast.LENGTH_SHORT).show();

                }else{


                    resetPassword(email);
                }

//                resetPasswordInDatabase();
            }
        });
    }


    private void resetPassword(String email) {
        mAuth  = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                if (emailExists) {
                    mAuth.signInAnonymously().addOnCompleteListener(signInTask -> {
                        if (signInTask.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.updatePassword("123456").addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        // Cập nhật mật khẩu thành công, cập nhật trong Realtime Database
                                        String uid = user.getUid();
                                        databaseReference.child(uid).child("password").setValue("123456")
                                                .addOnCompleteListener(databaseUpdateTask -> {
                                                    if (databaseUpdateTask.isSuccessful()) {
                                                        // Chuyển hướng tới trang đăng nhập
                                                        mAuth.signOut();
                                                        startActivity(new Intent(this, LoginActivity.class));
                                                        finish();
                                                    } else {
                                                        // Xử lý lỗi cập nhật database
                                                        Toast.makeText(this, "Cập nhật mật khẩu trong database thất bại.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // Xử lý lỗi cập nhật mật khẩu
                                        Toast.makeText(this, "Cập nhật mật khẩu thất bại.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            // Xử lý lỗi đăng nhập ẩn danh
                            Toast.makeText(this, "Đăng nhập ẩn danh thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Email không tồn tại
                    Toast.makeText(this, "Email không tồn tại trong hệ thống.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Xử lý lỗi khi kiểm tra email
                Toast.makeText(this, "Kiểm tra email thất bại.", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void notifcation(int notification){
        Dialog dialog = new Dialog(FogotPasswordActivity.this);
        dialog.setContentView(R.layout.activity_notification_dialog);
        Button back = dialog.findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView notificationBody = dialog.findViewById(R.id.txtNotification);
        notificationBody.setText(R.string.toastStringEmpty);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void sendRegisterAccount(String email, String password){
        subject = "TIMESCHEDULE - GỬI THÔNG TIN MẬT KHẨU";
        body = subject + "\n\nĐây là mật khẩu tài khoản được gửi từ TimeSchedule. Tuyệt đối không được gửi cho bất cứ ai. \n"
                + password.trim() + "\n\nThân ái";
//        javaMailAPI = new JavaMailAPI(RegisterActivity.this, email,subject, body);
//        javaMailAPI.execute();
//        String recipient = "recipient@example.com";
//        String subject = "Hello from Android";
//        String message = "This is a test email sent from an Android app.";

        JavaMailAPI sendMailTask = new JavaMailAPI(email, subject, body);
        sendMailTask.execute();
    }
}