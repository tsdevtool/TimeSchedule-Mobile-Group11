package com.example.models;

import java.io.Serializable;
import java.util.Date;

public class SubjectDetail implements Serializable {
    private int lessionBegin; //Tiet bat dau
    private String classroomCode; //Ma lop hoc
    private Date time; //Ngay hoc
    private int statusId;
    private int classId;
    private int subjectId;

    public SubjectDetail() {
    }

    public SubjectDetail(int lessionBegin, String classroomCode, Date time, int statusId, int classId, int subjectId) {
        this.lessionBegin = lessionBegin;
        this.classroomCode = classroomCode;
        this.time = time;
        this.statusId = statusId;
        this.classId = classId;
        this.subjectId = subjectId;
    }

    public int getLessionBegin() {
        return lessionBegin;
    }

    public void setLessionBegin(int lessionBegin) {
        this.lessionBegin = lessionBegin;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public void setClassroomCode(String classroomCode) {
        this.classroomCode = classroomCode;
    }
}
