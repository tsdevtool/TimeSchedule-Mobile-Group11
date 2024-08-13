package com.example.timeschedule_mobile_group11;


import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.timeschedule_mobile_group11.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    private void setUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

//        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.user_icon).into(binding.imvAvatar);
        binding.edtFullname.setText(user.getDisplayName());
        binding.edtEmail.setText(user.getEmail());
    }
}