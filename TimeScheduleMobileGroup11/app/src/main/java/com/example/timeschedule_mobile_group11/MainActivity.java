package com.example.timeschedule_mobile_group11;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.timeschedule_mobile_group11.databinding.ActivityMainBinding;
import com.example.timeschedule_mobile_group11.dialog.DialogContact;
import com.example.timeschedule_mobile_group11.fragment.ExamScheduleFragment;
import com.example.timeschedule_mobile_group11.fragment.HomeFragment;
import com.example.timeschedule_mobile_group11.fragment.OtherFragment;
import com.example.timeschedule_mobile_group11.fragment.ScheduleFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String DB_PATH_SUFFIX = "/databases/";
    String DATABASE_NAME = "db_timeschedule.db";
    //Khai bao cac bien lien quan toi database
    SQLiteDatabase database = null;
    FirebaseDatabase database2;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Viết thông báo vào database
        database2 = FirebaseDatabase.getInstance();
        myRef= database2.getReference("message");
        myRef.setValue("Hello,Word!");


        //Setting fragments to MainActivity
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

    //Database

    public void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
// TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
// Path to the just created empty db
            String outFileName = getDatabasePath();
// if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
// Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
// transfer bytes from the inputfile to the outputfile
// Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
// Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}