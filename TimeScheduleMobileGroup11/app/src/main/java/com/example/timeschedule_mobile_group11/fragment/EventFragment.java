package com.example.timeschedule_mobile_group11.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.EventDetailActivity;
import com.example.timeschedule_mobile_group11.adapter.EventAdapter;
import com.example.timeschedule_mobile_group11.databinding.ActivityEventBinding;
import com.example.timeschedule_mobile_group11.databinding.FragmentEventScheduleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import com.example.timeschedule_mobile_group11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    FragmentEventScheduleBinding binding;

    private DatabaseReference databaseReference;
    private List<Event> eventList = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> eventTitles = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
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
        // Khởi tạo kết nối tới Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("events");
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_event_schedule, container, false);
//
//    }
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_event_schedule, container, false);

    // Thiết lập ListView
    listView = view.findViewById(R.id.lvEvents);
    arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, eventTitles);
    listView.setAdapter(arrayAdapter);

    // Lấy dữ liệu từ Firebase
    loadDataFromFirebase();
    // click event
    // Thiết lập sự kiện click cho ListView
    listView.setOnItemClickListener((parent, view1, position, id) -> {
        // Lấy sự kiện được click
        Event selectedEvent = eventList.get(position);

        // Chuyển đến EventDetailActivity với thông tin chi tiết về sự kiện
        Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra("event", selectedEvent);
        startActivity(intent);
    });


    return view;
}

    private void loadDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear(); // Xóa dữ liệu cũ

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                // Sử dụng CustomEventAdapter để hiển thị tất cả thông tin của sự kiện
                CustomEventAdapter adapter = new CustomEventAdapter(getContext(), eventList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}