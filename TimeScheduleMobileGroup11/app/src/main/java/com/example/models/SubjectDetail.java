package com.example.models;

import java.io.Serializable;
import java.util.Date;

public class SubjectDetail implements Serializable {
    private int lessionBegin; //Tiet bat dau
    private String classroomCode; //Ma lop hoc
    private String time; //Ngay hoc
    private String statusId;
    private String classId;
    private String subjectId;

    public SubjectDetail() {
    }

    public SubjectDetail(int lessionBegin, String subjectId, String classId, String statusId, String time, String classroomCode) {
        this.lessionBegin = lessionBegin;
        this.subjectId = subjectId;
        this.classId = classId;
        this.statusId = statusId;
        this.time = time;
        this.classroomCode = classroomCode;
    }

    public int getLessionBegin() {
        return lessionBegin;
    }

    public void setLessionBegin(int lessionBegin) {
        this.lessionBegin = lessionBegin;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public void setClassroomCode(String classroomCode) {
        this.classroomCode = classroomCode;
    }
}
