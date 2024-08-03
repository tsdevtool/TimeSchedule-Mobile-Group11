package com.example.models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String fullname;
    private String avatar;
    private String email;
    private Date dayAdmission;
    private int classId;
    private int facultyId;
    private int roleId;

    public User(int id, String username, String fullname, String password, String avatar, String email, Date dayAdmission, int classId, int facultyId, int roleId) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.dayAdmission = dayAdmission;
        this.classId = classId;
        this.facultyId = facultyId;
        this.roleId = roleId;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public Date getDayAdmission() {
        return dayAdmission;
    }

    public void setDayAdmission(Date dayAdmission) {
        this.dayAdmission = dayAdmission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
