package com.example.timeschedule_mobile_group11.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapters.ScheduleAdapter;
import com.example.models.Subject;
import com.example.models.SubjectDetail;
import com.example.models.User;
import com.example.timeschedule_mobile_group11.EventAdapter;
import com.example.timeschedule_mobile_group11.MainActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.FragmentHomeBinding;
import com.example.timeschedule_mobile_group11.databinding.FragmentScheduleBinding;
import com.example.timeschedule_mobile_group11.dialog.DialogContact;
import com.example.utils.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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


    ScheduleAdapter adapter;

    FragmentScheduleBinding binding;
    List<Subject> subjects;
    List<SubjectDetail> subjectDetails;

    private DatabaseReference subjectRef, subjectDetailsRef, usersRef;
//    private ScheduleAdapter adapter;

    DatabaseReference mDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        loadSubjectDetails();
//        showSubjectInformation();
        binding = FragmentScheduleBinding.inflate(getLayoutInflater());
        subjects = new ArrayList<>();
        subjectDetails = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference(Firebase.USERS);
        subjectRef = FirebaseDatabase.getInstance().getReference(Firebase.SUBJECT);
        subjectDetailsRef = FirebaseDatabase.getInstance().getReference(Firebase.SUBJECT_DETAILS);

//        getClassIdOfUser();
        getSubjectDetails();
        return binding.getRoot();


    }

    private void showSubjectInformation(List<SubjectDetail> subjectDetails, String classId) {
        if (adapter == null) {
            adapter = new ScheduleAdapter(requireContext(), subjectDetails, classId);
            binding.lvSubjects.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(subjectDetails);
            adapter.notifyDataSetChanged();
        }
    }

    private void getSubjectDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();


        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String classId  = snapshot.child(Firebase.USERS_CLASS_ID).getValue(String.class);
//                    Toast.makeText(getContext(), classId, Toast.LENGTH_SHORT).show();
                    subjectDetailsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dbsnapshot) {
                            for (DataSnapshot sb: dbsnapshot.getChildren()){
                                SubjectDetail subjectDetail = new SubjectDetail();
                                subjectDetail.setClassId((String) sb.child(Firebase.COL_SUBJECT_DETAILS_CLASS_ID).getValue());
//                                Toast.makeText(getContext(), "hehe: " + subjectDetail.getClassId(), Toast.LENGTH_SHORT).show();
                                if(classId.equals(subjectDetail.getClassId())){
//                                    subjectDetail.setLessionBegin((Integer) sb.child(Firebase.COL_SUBJECT_DETAILS_LESSION_BEGIN).getValue());
                                    Object lessionBeginObj = sb.child(Firebase.COL_SUBJECT_DETAILS_LESSION_BEGIN).getValue();

                                    if (lessionBeginObj instanceof Long) {
                                        subjectDetail.setLessionBegin(((Long) lessionBeginObj).intValue());  // Chuyển đổi từ Long sang Integer
                                    } else if (lessionBeginObj instanceof Integer) {
                                        subjectDetail.setLessionBegin((Integer) lessionBeginObj);
                                    }

//                                    Toast.makeText(getContext(), String.valueOf(subjectDetail.getLessionBegin()), Toast.LENGTH_SHORT).show();

                                    subjectDetail.setSubjectId((String) sb.child(Firebase.COL_SUBJECT_DETAILS_SUBJECT_ID).getValue());
                                    subjectDetail.setClassroomCode((String) sb.child(Firebase.COL_SUBJECT_DETAILS_CLASSROOM_CODE).getValue());
                                    Object statusIdObj = sb.child(Firebase.COL_SUBJECT_DETAILS_STATUS_ID).getValue();
                                    if (statusIdObj instanceof Long) {
                                        String statusId = String.valueOf(statusIdObj);  // Chuyển đổi từ Long sang String
                                        subjectDetail.setStatusId(statusId);
                                    } else if (statusIdObj instanceof String) {
                                        subjectDetail.setStatusId((String) statusIdObj);  // Ép kiểu nếu đã là String
                                    }
                                    subjectDetail.setTime((String) sb.child(Firebase.COL_SUBJECT_DETAILS_TIME).getValue());
                                    subjectDetails.add(subjectDetail);
                                }

//                    Toast.makeText(getContext(), subjectDetail.getClassId(), Toast.LENGTH_SHORT).show();
                            }
                            if(!subjectDetails.isEmpty()){
                                Toast.makeText(getContext(), "TKB", Toast.LENGTH_SHORT).show();
                                showSubjectInformation(subjectDetails, classId);
                            }

//                subjectDetails.notify();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Get list subject of user is fail!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    notification(R.string.toastNoData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Not found class Identification of you", Toast.LENGTH_SHORT).show();
            }
        });


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