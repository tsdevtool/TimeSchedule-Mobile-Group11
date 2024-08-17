package com.example.adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.models.Subject;
import com.example.models.SubjectDetail;
import com.example.timeschedule_mobile_group11.LoginActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.utils.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<SubjectDetail> {

    private Context context;
    private List<SubjectDetail> subjectDetails;

    private String classId;
    private String subjectName;
    private String className;

    private DatabaseReference subjectRef, classRef;
    private FirebaseUser user;

    public ScheduleAdapter(Context context, List<SubjectDetail> subjectDetails, String classId) {
        super(context, 0, subjectDetails);
        this.context = context;
        this.subjectDetails = subjectDetails;
        this.subjectRef = FirebaseDatabase.getInstance().getReference(Firebase.SUBJECT);
        this.classRef = FirebaseDatabase.getInstance().getReference(Firebase.CLASS);
        this.classId = classId;
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_list_schedule, viewGroup, false);
        }

        holder = new ViewHolder();
        holder.txtTime = view.findViewById(R.id.txtTime);
        holder.txtLessionBegin = view.findViewById(R.id.txtLessionBegin);
        holder.txtSubjectName = view.findViewById(R.id.txtSubjectName);
        holder.txtClassroom = view.findViewById(R.id.txtClassroomCode);
        holder.txtClass = view.findViewById(R.id.txtClass);

        SubjectDetail subjectDetail = getItem(i);
        holder.txtTime.setText(subjectDetail.getTime());
        holder.txtLessionBegin.setText(String.valueOf(subjectDetail.getLessionBegin()) + " - " + (subjectDetail.getLessionBegin() + 5));
//        getSubjectName(subjectDetail.getSubjectId());
        subjectRef.child(subjectDetail.getSubjectId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.txtSubjectName.setText(snapshot.child(Firebase.COL_SUBJECT_NAME).getValue(String.class));
                }else{
//                    holder.txtSubjectName.setText("Not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        holder.txtSubjectName.setText(subjectName);
        holder.txtClassroom.setText(getContext().getString(R.string.classroom) + ": " + subjectDetail.getClassroomCode());
//        getClassOfUser(subjectDetail.getClassId());
//        holder.txtClass.setText(className);
        classRef.child(subjectDetail.getClassId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.txtClass.setText(getContext().getString(R.string.classs) + ": " + snapshot.child(Firebase.COL_CLASS_CODE).getValue(String.class));
                }else{
                   holder.txtClass.setText("Not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    public static class ViewHolder{
        TextView txtTime, txtLessionBegin, txtSubjectName, txtClassroom, txtClass;

    }




}
