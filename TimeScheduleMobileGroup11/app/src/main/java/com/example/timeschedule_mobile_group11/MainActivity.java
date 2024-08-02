package com.example.timeschedule_mobile_group11;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.timeschedule_mobile_group11.databinding.ActivityMainBinding;
import com.example.timeschedule_mobile_group11.fragment.ExamScheduleFragment;
import com.example.timeschedule_mobile_group11.fragment.HomeFragment;
import com.example.timeschedule_mobile_group11.fragment.OtherFragment;
import com.example.timeschedule_mobile_group11.fragment.ScheduleFragment;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());

                return true;
            } else if (item.getItemId() == R.id.schedule) {
                replaceFragment(new ScheduleFragment());
                return true;
            } else if (item.getItemId() == R.id.examSchedule) {
                replaceFragment(new ExamScheduleFragment());
                return true;
            } else if (item.getItemId() == R.id.other) {
                replaceFragment(new OtherFragment());
                return true;
            } else {
                return false;
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}