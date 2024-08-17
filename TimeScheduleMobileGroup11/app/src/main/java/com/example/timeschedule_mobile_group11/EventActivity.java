package com.example.timeschedule_mobile_group11;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.adapter.EventAdapter;
import com.example.timeschedule_mobile_group11.databinding.ActivityEventBinding;
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

public class EventActivity extends AppCompatActivity {

    ActivityEventBinding binding;
    private DatabaseReference eventsRef;
    private EventAdapter eventAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        LoadEvents();
        addEvents();
    }

    private void addEvents() {
        binding.imvPhotoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadEvents() {
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
                    // Hiển thị các sự kiện còn lại vào ListView
                    showEventsExceptLatest(eventList, latestEvent);
                } else {
                    Toast.makeText(EventActivity.this, "No events found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
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



    private void showEventsExceptLatest(List<Event> eventList, Event latestEvent) {
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
        if (eventAdapter == null) {
            eventAdapter = new EventAdapter(EventActivity.this, eventList);
            binding.lvMoreEvents.setAdapter(eventAdapter);
        } else {
            eventAdapter.clear();
            eventAdapter.addAll(eventList);
            eventAdapter.notifyDataSetChanged();
        }
    }

    private int getDrawableResourceByName(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());


    }
}