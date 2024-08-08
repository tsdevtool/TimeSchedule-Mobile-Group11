package com.example.timeschedule_mobile_group11.fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.timeschedule_mobile_group11.LoginActivity;
import com.example.timeschedule_mobile_group11.MainActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.FragmentHomeBinding;
import com.example.timeschedule_mobile_group11.dialog.DialogContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentHomeBinding binding;
    private DialogContact contact ;
    ProgressDialog progressDialog;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor


    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // sử dụng requireContext() để lấy Context từ Fragment và truyền nó vào DialogContact.
        contact = new DialogContact(requireContext());
        addEvents();
        return binding.getRoot();

    }

    private void addEvents() {
//        binding.btnContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        binding.imvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.show();
            }
        });
        showAvatar();
        logout();

    }

    private void logout() {
//        Firebase.loadFirebase();
        binding.imvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kết thúc MainActivity
                getActivity().finish();
                FirebaseAuth.getInstance().signOut();
                // Khởi động lại LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    private void showAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        Uri urlAvatar = user.getPhotoUrl();
        Glide.with(this).load(urlAvatar).error(R.drawable.user_icon).into(binding.imvProfile);
    }
}