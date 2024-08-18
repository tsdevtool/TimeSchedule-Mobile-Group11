package com.example.timeschedule_mobile_group11.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.timeschedule_mobile_group11.EventActivity;
import com.example.timeschedule_mobile_group11.ProfileActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.ActivityLoginBinding;
import com.example.timeschedule_mobile_group11.databinding.FragmentOtherBinding;
import com.example.utils.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherFragment extends Fragment {

    //Khai báo biến toàn cục
    FragmentOtherBinding binding;
    private HomeFragment.OnFragmentInteractionListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OtherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (HomeFragment.OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherFragment newInstance(String param1, String param2) {
        OtherFragment fragment = new OtherFragment();
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


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = FragmentOtherBinding.inflate(getLayoutInflater());
//        addEvents();
//        // Inflate the layout for this fragment
//        return binding.getRoot();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtherBinding.inflate(inflater, container, false);

        // Lấy userId của người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Truy vấn thông tin người dùng từ Firebase Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy dữ liệu người dùng từ Firebase
                String fullName = dataSnapshot.child("fullName").getValue(String.class);
                String userCode = dataSnapshot.child("userCode").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String photoUrl = dataSnapshot.child("photoUrl").getValue(String.class);
                // Kiểm tra và hiển thị ảnh đại diện nếu có
                if (photoUrl != null) {
                    Glide.with(OtherFragment.this)
                            .load(photoUrl)
                            .into(binding.imvAvatar);
                } else {
                    // Bạn có thể đặt một ảnh đại diện mặc định ở đây nếu không có ảnh nào được lưu
                    binding.imvAvatar.setImageResource(R.drawable.logo_app);
                }
                // Cập nhật giao diện với dữ liệu người dùng
                binding.txtFullName.setText(fullName);
                binding.txtUserCode.setText(userCode);
                binding.txtEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });

        addEvents(); // Đảm bảo rằng các sự kiện khác vẫn hoạt động

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    private void addEvents() {
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel: "+ Contact.PHONE_NUMBER);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        binding.btnKhaibao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnThuctap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserFragment.class);
                startActivity(intent);
            }
        });

        //Chuyển hướng fragment to fragment bằng cách kế thừa các hàm chuyển chuyển hướng bên homefragment
        binding.btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClicked();
                }
            }
        });

        binding.btnSaveEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClickedToEventFragment();
                }
            }
        });

        //Chuyển hướng fargment to activity
        binding.btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

}}