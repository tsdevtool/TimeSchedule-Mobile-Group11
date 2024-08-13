package com.example.timeschedule_mobile_group11;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.models.Event;
import com.example.models.User;
import com.example.timeschedule_mobile_group11.databinding.ActivityMainBinding;
import com.example.timeschedule_mobile_group11.dialog.DialogContact;
import com.example.timeschedule_mobile_group11.fragment.ExamScheduleFragment;
import com.example.timeschedule_mobile_group11.fragment.HomeFragment;
import com.example.timeschedule_mobile_group11.fragment.OtherFragment;
import com.example.timeschedule_mobile_group11.fragment.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    ActivityMainBinding binding;
//    String DB_PATH_SUFFIX = "/databases/";
//    String DATABASE_NAME = "db_timeschedule.db";

    //Khai bao cac bien lien quan toi database
    private FirebaseDatabase database;
    private DatabaseReference myDef;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Dữ liệu test event
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference().child("events"); // Reference to "events" node

//        // Create sample events
//        Event event1 = new Event(
//                1,
//                1,
//                "E1.03.02",
//                "2024-08-15T09:00:00",
//                "image1.png",
//                "A description for event 1.",
//                "Event Title 1",
//                "Location 1"
//        );
//
//        Event event2 = new Event(
//                2,
//                2,
//                "E2.04.03",
//                "2024-08-16T11:00:00",
//                "image2.png",
//                "A description for event 2.",
//                "Event Title 2",
//                "Location 2"
//        );
//
//        Event event3 = new Event(
//                3,
//                1,
//                "E3.05.04",
//                "2024-08-17T14:00:00",
//                "image3.png",
//                "A description for event 3.",
//                "Event Title 3",
//                "Location 3"
//        );
//        eventsRef.child("event1").setValue(event1);
//        eventsRef.child("event2").setValue(event2);
//        eventsRef.child("event3").setValue(event3);






        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = binding.bottomNavigationView;

        // Set initial fragment
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }
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
        //FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
        //
    }

    @Override
    public void onImageClicked() {
        replaceFragment(new ScheduleFragment());
        bottomNavigationView.setSelectedItemId(R.id.schedule);
    }

    @Override
    public void onImageClickedToEventFragment() {
        replaceFragment(new OtherFragment());
        bottomNavigationView.setSelectedItemId(R.id.examSchedule);
    }


}