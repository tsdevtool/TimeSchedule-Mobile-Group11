package com.example.timeschedule_mobile_group11;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timeschedule_mobile_group11.databinding.ActivityRegisterBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    Calendar calendar;
    AloadingDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new AloadingDialog(this);
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
        binding.edtDate.setOnClickListener(view->{
            new DatePickerDialog(RegisterActivity.this,date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        binding.btnRegisterSucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =  new Intent(RegisterActivity.this, LoginActivity.class);
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
                handler.postDelayed(runnable,2500);
            }
        });
    }

    private void updatelabel() {

        String myFormat= "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat( myFormat, Locale.US);
        binding.edtDate.setText(dateFormat.format(calendar.getTime()));
    }
}