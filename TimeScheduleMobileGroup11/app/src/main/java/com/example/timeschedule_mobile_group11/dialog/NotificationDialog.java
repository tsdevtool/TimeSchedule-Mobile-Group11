package com.example.timeschedule_mobile_group11.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.ActivityNotificationDialogBinding;

public class NotificationDialog extends AppCompatActivity {

    ActivityNotificationDialogBinding binding;
    public static String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
    }

    private void addEvents() {
        binding.txtNotification.setText(content);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}