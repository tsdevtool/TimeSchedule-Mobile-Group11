package com.example.timeschedule_mobile_group11.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapters.EventDetailAdapter;
import com.example.adapters.ScheduleAdapter;
import com.example.models.Event;
import com.example.models.EventDetail;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.FragmentExamScheduleBinding;
import com.example.utils.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExamScheduleFragment() {
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
    public static ExamScheduleFragment newInstance(String param1, String param2) {
        ExamScheduleFragment fragment = new ExamScheduleFragment();
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

    EventDetailAdapter adapter;
    List<EventDetail> eventDetailList;
    List<Event> eventList;

    FragmentExamScheduleBinding binding;
    FirebaseUser user;
    DatabaseReference eventDetailRef, eventRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExamScheduleBinding.inflate(getLayoutInflater());
        user = FirebaseAuth.getInstance().getCurrentUser();
        eventDetailRef = FirebaseDatabase.getInstance().getReference(Firebase.EVENT_DETAIL);
        eventRef = FirebaseDatabase.getInstance().getReference(Firebase.EVENT);
        eventDetailList = new ArrayList<>();
        String id = user.getUid();
        getEventOfUser(id);
        showInfomation();
//        showEvent();
        return binding.getRoot();
    }

    private void showInfomation() {
        binding.lvEventsFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventDetail eventDetail = eventDetailList.get(i);
                eventRef.child(eventDetail.getEventId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.event_information_dialog);

                            ImageView imvImage  = dialog.findViewById(R.id.imvImageEventInformation);
                            String imageUrl = snapshot.child(Firebase.COL_EVENT_IMAGE).getValue(String.class);
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Picasso.get()
                                        .load(imageUrl)
                                        .into(imvImage);              // ImageView để hiển thị ảnh
                            }
                            TextView txtTitle = dialog.findViewById(R.id.txtTitleEventInformation);
                            txtTitle.setText(snapshot.child(Firebase.COL_EVENT_TITLE).getValue(String.class));

                            TextView txtTime = dialog.findViewById(R.id.txtTimeEventInformation);
                            txtTime.setText(snapshot.child(Firebase.COL_EVENT_TIME).getValue(String.class));

                            TextView txtDescription = dialog.findViewById(R.id.txtDescriptionEventInformation);
                            txtDescription.setText(snapshot.child(Firebase.COL_EVENT_DESCRIPTION).getValue(String.class));

                            TextView txtLocation = dialog.findViewById(R.id.txtLocationEventInformation);
                            txtLocation.setText(snapshot.child(Firebase.COL_EVENT_LOCATION).getValue(String.class));

                            TextView txtClassroom = dialog.findViewById(R.id.txtClassroomEventInformation);
                            txtClassroom.setText(snapshot.child(Firebase.COL_EVENT_CLASSROOM_CODE).getValue(String.class));

                            TextView txtQuestion = dialog.findViewById(R.id.txtQuestionEventInformation);
                            txtQuestion.setText(eventDetail.getQuestion());

                            Button btnBack = dialog.findViewById(R.id.btnCancelEventInformation);
                            btnBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        notification(R.string.error);
                    }
                });
            }
        });
    }


    private void getEventOfUser(String userId) {
        eventDetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot db: snapshot.getChildren()){
                        EventDetail eventDetail = new EventDetail();
                        eventDetail.setQuestion(db.child(Firebase.COL_EVENT_DETAIL_QUESTION).getValue(String.class));
                        eventDetail.setUserId(db.child(Firebase.COL_EVENT_DETAIL_USER_ID).getValue(String.class));
                        eventDetail.setEventId(db.child(Firebase.COL_EVENT_DETAIL_EVENT_ID).getValue(String.class));


                        eventDetailList.add(eventDetail);
//                        Toast.makeText(getContext(), eventDetail.toString(), Toast.LENGTH_SHORT).show();
                    }
                    if(!eventDetailList.isEmpty()){
                        Toast.makeText(getContext(), "SK", Toast.LENGTH_SHORT).show();
                        showEventDetailInformation(eventDetailList);
                    }
                }else{
                    notification(R.string.toastNoData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                notification(R.string.toastNoData);
            }
        });
    }

    private void showEventDetailInformation(List<EventDetail> eventDetailList) {
        if (adapter == null) {
            adapter = new EventDetailAdapter(requireContext(), eventDetailList);
            binding.lvEventsFragment.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(eventDetailList);
            adapter.notifyDataSetChanged();
        }
    }

    private void notification(int notification){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_notification_dialog);
        Button back = dialog.findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView notificationBody = dialog.findViewById(R.id.txtNotification);
        notificationBody.setText(notification);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}