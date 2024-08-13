package com.example.models;

import java.io.Serializable;

public class User implements Serializable {
    private String userCode;
    private String email;
    private String password;
    private String fullName;
    private String avatarUrl;
    private String dayOfBirth;
    private boolean sex;
    private String dayAdmission;
    private String classId;
    private String facultyId;
    private String roleId;

    public User() {
    }


    public User(String userCode, String dayAdmission, boolean sex, String dayOfBirth, String avatarUrl, String fullName, String password, String email) {
        this.userCode = userCode;
        this.dayAdmission = dayAdmission;
        this.sex = sex;
        this.dayOfBirth = dayOfBirth;
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
    }

    public User(String userCode, String email, String password, String fullName, String avatarUrl, String dayOfBirth, boolean sex, String dayAdmission, String classId, String facultyId, String roleId) {
        this.userCode = userCode;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.dayOfBirth = dayOfBirth;
        this.sex = sex;
        this.dayAdmission = dayAdmission;
        this.classId = classId;
        this.facultyId = facultyId;
        this.roleId = roleId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDayAdmission() {
        return dayAdmission;
    }

    public void setDayAdmission(String dayAdmission) {
        this.dayAdmission = dayAdmission;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
