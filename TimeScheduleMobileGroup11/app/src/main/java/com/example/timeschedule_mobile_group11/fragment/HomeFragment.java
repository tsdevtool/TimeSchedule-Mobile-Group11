package com.example.timeschedule_mobile_group11.fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.MainActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.FragmentHomeBinding;
import com.example.timeschedule_mobile_group11.dialog.DialogContact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor


    }
    public interface OnFragmentInteractionListener {
        void onImageClicked();
        void onImageClickedToEventFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // sử dụng requireContext() để lấy Context từ Fragment và truyền nó vào DialogContact.
        contact = new DialogContact(requireContext());
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        addEvents();
        loadEvent();
        return binding.getRoot();


    }

    private void loadEvent() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> eventList= new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Event event= snapshot1.getValue(Event.class);
                    if(event!= null){
                        eventList.add(event);
                    }
                }
                if(!eventList.isEmpty()){
                    Event latestEvent = getLatestEvent(eventList);
                    updateUI(latestEvent);
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

    private Event getLatestEvent(List<Event> events){
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
    // Hàm này lấy ID của tài nguyên drawable dựa trên tên hình ảnh
    private int getDrawableResourceByName(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}