package com.example.timeschedule_mobile_group11;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Class;
import com.example.models.Role;
import com.example.models.User;
import com.example.timeschedule_mobile_group11.databinding.ActivityRegisterBinding;
import com.example.timeschedule_mobile_group11.dialog.AloadingDialog;
import com.example.timeschedule_mobile_group11.fragment.HomeFragment;
import com.example.utils.Firebase;
import com.example.utils.JavaMailAPI;
import com.example.utils.Password;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    DatabaseReference classRef, roleRef;
    List<Class> classList;
    List<Role> roleList;

    private String subject;
    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new AloadingDialog(this);
        classList = new ArrayList<>();
        classRef = FirebaseDatabase.getInstance().getReference(Firebase.CLASS);

        roleList = new ArrayList<>();
        roleRef = FirebaseDatabase.getInstance().getReference(Firebase.ROLE);
        loadClass();

//        Xu ly thoi gian
        TimeHandling();

//        Xu ly dang ky tai khoan
        addEvents();
    }

    private void loadClass() {
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    Class cl = new Class();
                    String className = classSnapshot.child(Firebase.COL_CLASS_CODE).getValue(String.class);
                    if (className != null) {
                        cl.setId(classSnapshot.getKey());
                        cl.setCode(className);
                        cl.setFacultyId("faculty1");
                        classList.add(cl);
                    }
                }
                // Tạo adapter cho spinner
                ArrayAdapter<Class> adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_item, classList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Dropdown view layout (optional)
                binding.spinnerClasses.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        roleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roleList.clear();
                for (DataSnapshot roleSnapshot : snapshot.getChildren()) {
                    Role cl = new Role();
                    String roleName = roleSnapshot.child(Firebase.COL_ROLE_NAME).getValue(String.class);
                    if (roleName != null) {
                        cl.setId(roleSnapshot.getKey());
                        cl.setName(roleName);
                        roleList.add(cl);
                    }
                }
                // Tạo adapter cho spinner
                ArrayAdapter<Role> adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_item, roleList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Dropdown view layout (optional)
                binding.spinnerRoles.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                user.setPassword(Password.generatePassword(12));
                user.setDayOfBirth(binding.edtDayOfBirth.getText().toString());
                user.setFacultyId("faculty1");
                binding.spinnerClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Class selectedClass = (Class) parent.getItemAtPosition(position);
                        String selectedClassId = selectedClass.getId();
                        user.setClassId(selectedClassId);


                        // Sử dụng selectedClassId và selectedClassName
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Xử lý khi không có gì được chọn
                    }
                });
                binding.spinnerRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Role selectedRole = (Role) parent.getItemAtPosition(position);
                        String selectedRoleId = selectedRole.getId();
                        user.setRoleId(selectedRoleId);


                        // Sử dụng selectedClassId và selectedClassName
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Xử lý khi không có gì được chọn
                    }
                });

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
//                        mAuth.signOut();
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

               loading.show();
               Handler handler= new Handler();
               Runnable runnable= new Runnable() {
                   @Override
                   public void run() {
                       loading.cancel();
                       sendRegisterAccount(user.getEmail(), user.getPassword());

                       Intent myIntent =  new Intent(RegisterActivity.this, HomeFragment.class);
                       startActivity(myIntent);
                        finish();
                       Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                   }
               };
               handler.postDelayed(runnable,3000);
               finish();
           }else{
               Toast.makeText(this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
           }
        });
    }


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