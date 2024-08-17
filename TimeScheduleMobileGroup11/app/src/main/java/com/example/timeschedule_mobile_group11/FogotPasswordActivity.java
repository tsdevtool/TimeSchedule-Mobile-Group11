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
import com.example.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FogotPasswordActivity extends AppCompatActivity {

    AloadingDialog loading;


    private String email;
    ActivityFogotPasswordBinding binding;
    private int notificationString;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFogotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading =new AloadingDialog(this);
        addEvents();

    }

    private void addEvents() {
        binding.btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.edtEmail.getText().toString().trim();
                if(email.isEmpty()){
                    notifcation(R.string.toastStringEmpty);
//                    Toast.makeText(FogotPasswordActivity.this, "Vui lòng không để trống thông tin!!", Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent = new Intent(FogotPasswordActivity.this, LoginActivity.class);

                    loading.show();
                    Handler handler= new Handler();
                    Runnable runnable= new Runnable() {
                        @Override
                        public void run() {
                            resetPassword();
                            intent.putExtra(Util.NOTIFICATION, 1);
                            intent.putExtra(Util.EMAIL, email);
                            loading.cancel();

                            startActivity(intent);
                            finish();
                        }
                    };
                    handler.postDelayed(runnable,2000);

                }
            }
        });
    }

    private void resetPassword() {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notificationString = R.string.toastCheckPasswordInEmail;
                        }else{
                            notificationString = R.string.toastNotFoundEmail;
                        }
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
        notificationBody.setText(notification);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}