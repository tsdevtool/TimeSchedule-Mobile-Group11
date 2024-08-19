package com.example.timeschedule_mobile_group11.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;

import android.app.ProgressDialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.models.Event;
import com.example.models.User;
import com.example.timeschedule_mobile_group11.EventActivity;
import com.example.timeschedule_mobile_group11.MainActivity;
import com.example.timeschedule_mobile_group11.RegisterActivity;
import com.example.timeschedule_mobile_group11.adapter.EventAdapter;

import com.bumptech.glide.Glide;
import com.example.timeschedule_mobile_group11.LoginActivity;

import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.FragmentHomeBinding;
import com.example.timeschedule_mobile_group11.dialog.AloadingDialog;
import com.example.timeschedule_mobile_group11.dialog.DialogContact;

import com.example.utils.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    private OnFragmentInteractionListener listener;
    private DatabaseReference eventsRef;
    private EventAdapter eventAdapter;
    private CustomEventAdapter customEventAdapter;

    String role;
    ProgressDialog progressDialog;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor


    }
    public interface OnFragmentInteractionListener {
        void onImageClicked();
        void onImageClickedToEventFragment();

        void OnClickedSchedule();

        void OnClickedSaveEvents();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
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

        binding.layoutTKB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClicked();
                }
            }
        });
        binding.layoutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClickedToEventFragment();
                }
            }
        });
        binding.tvMoreEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });
        showAvatar();
        logout();
    }

    DatabaseReference userRef;
    FirebaseUser user;
    AloadingDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // sử dụng requireContext() để lấy Context từ Fragment và truyền nó vào DialogContact.

        contact = new DialogContact(requireContext());
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference(Firebase.USERS);

        addEvents();
        loadNewEvent();
        return binding.getRoot();


    }

    private void loadNewEvent() {
        userRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    role = snapshot.child(Firebase.USERS_ROLE_ID).getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Event event = snapshot1.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                if (!eventList.isEmpty()) {
                    Event latestEvent = getLatestEvent(eventList);
                    updateUI(latestEvent);

                    // Hiển thị các sự kiện còn lại vào ListView
                    showEventsExceptLatest(eventList, latestEvent);
                } else {
                    Toast.makeText(getContext(), "No events found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Event getLatestEvent(List<Event> events) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Event latestEvent = null;
        Date latestDate = null;

        for (Event event : events) {
            try {
                Date eventDate = sdf.parse(event.getTime());
                if (latestDate == null || eventDate.after(latestDate)) {
                    latestDate = eventDate;
                    latestEvent = event;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return latestEvent;
    }

    private void updateUI(Event event) {
        // Cập nhật tiêu đề sự kiện
        binding.tvTitleEvent.setText(event.getTitle());

        // Lấy ID của tài nguyên drawable từ tên hình ảnh trong dữ liệu
        int imageResId = getDrawableResourceByName(getContext(), event.getImage());

        // Nếu tài nguyên tồn tại, thì cập nhật ImageView
        if (imageResId != 0) {
            binding.imvPhotoEvent.setImageResource(imageResId);
        } else {
            binding.imvPhotoEvent.setImageResource(R.drawable.img); // Hình ảnh mặc định nếu không tìm thấy
        }
    }

    private void showEventsExceptLatest(List<Event> eventList, Event latestEvent) {
        // Loại bỏ sự kiện mới nhất từ danh sách
        eventList.remove(latestEvent);

        // Sắp xếp danh sách sự kiện từ mới nhất đến cũ nhất
        Collections.sort(eventList, (e1, e2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date d1 = sdf.parse(e1.getTime());
                Date d2 = sdf.parse(e2.getTime());
                return d2.compareTo(d1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });

        // Cập nhật adapter với danh sách sự kiện còn lại
        if (customEventAdapter == null) {
            customEventAdapter = new CustomEventAdapter(requireContext(), eventList);
            binding.lvEvents.setAdapter(customEventAdapter);
        } else {
            // Nếu adapter đã tồn tại, chỉ cần cập nhật lại dữ liệu
            customEventAdapter.notifyDataSetChanged();
        }
    }


    private int getDrawableResourceByName(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());


    }

    private static final int REQUEST_CODE_CREATE_EMPLOYEE = 2;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_EMPLOYEE && resultCode == RESULT_OK) {
            if (data != null) {
                String employeeId = data.getStringExtra("EMPLOYEE_ID");
                if (employeeId != null) {
                    Toast.makeText(getContext(), "ID nhân viên mới: " + employeeId, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void logout() {
//        Firebase.loadFirebase();

        binding.imvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role.equals("1")){

//                    loading.show();
//                    Handler handler= new Handler();
//                    Runnable runnable= new Runnable() {
//                        @Override
//                        public void run() {
//                            loading.cancel();

                            getActivity().finish();
                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_CREATE_EMPLOYEE);
//
//                        }
//                    };
//                    handler.postDelayed(runnable,2000);
                }
                // Kết thúc MainActivity
                else{
                    getActivity().finish();
                    FirebaseAuth.getInstance().signOut();
                    // Khởi động lại LoginActivity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

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