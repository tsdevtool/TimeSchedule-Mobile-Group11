package com.example.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.models.EventDetail;
import com.example.models.SubjectDetail;
import com.example.timeschedule_mobile_group11.R;
import com.example.utils.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.Date;
import java.util.List;

public class EventDetailAdapter extends ArrayAdapter<EventDetail> {
    private Context context;
    private List<EventDetail> eventDetailList;

    private DatabaseReference eventRef;

    public EventDetailAdapter(Context context, List<EventDetail> eventDetailList){
        super(context, 0, eventDetailList);
        this.context = context;
        this.eventDetailList = eventDetailList;
        this.eventRef = FirebaseDatabase.getInstance().getReference(Firebase.EVENT);

    }

    @Nullable
    @Override
    public EventDetail getItem(int position) {
        return eventDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_events_fragment, viewGroup, false);
        }

        holder = new ViewHolder();
        holder.txtTitle = view.findViewById(R.id.txtTitleEvent);
        holder.txtLocation = view.findViewById(R.id.txtLocationEvent);
        holder.txtTime = view.findViewById(R.id.txtTimeEvent);
        holder.imvEvent = view.findViewById(R.id.imvImageEvent);

        EventDetail eventDetail = eventDetailList.get(i);
        String eventId = eventDetail.getEventId();

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    eventRef.child(eventId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                holder.txtTitle.setText(snapshot.child(Firebase.COL_EVENT_TITLE).getValue(String.class));
                                holder.txtTime.setText(snapshot.child(Firebase.COL_EVENT_TIME).getValue(String.class));
                                holder.txtLocation.setText("Địa điểm: " + snapshot.child(Firebase.COL_EVENT_LOCATION).getValue(String.class));
                                String imageUrl = snapshot.child(Firebase.COL_EVENT_IMAGE).getValue(String.class);
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Picasso.get()
                                            .load(imageUrl)
                                            .into(holder.imvEvent);              // ImageView để hiển thị ảnh
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }

    private static class ViewHolder{
        TextView txtTitle, txtTime, txtLocation;
        ImageView imvEvent;
    }
}
