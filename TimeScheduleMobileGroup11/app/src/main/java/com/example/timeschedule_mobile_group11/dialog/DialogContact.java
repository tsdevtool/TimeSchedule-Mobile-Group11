package com.example.timeschedule_mobile_group11.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timeschedule_mobile_group11.R;

public class DialogContact extends Dialog {

    public DialogContact(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams params= getWindow().getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setWindowAnimations(R.style.Dialog_translate);
        setTitle(null);
        setCancelable(true);
        setOnCancelListener(null);
        View view= LayoutInflater.from(context).inflate(R.layout.activity_dialog_contact, null);
        setContentView(view);
    }
}