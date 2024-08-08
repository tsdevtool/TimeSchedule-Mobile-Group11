package com.example.models;

import java.io.Serializable;

public class Subject implements Serializable {
    private int id;
    private String code; //Ma mon hoc vi du CMP177
    private String name; //Ten mon hoc vi du Lap trinh thiet bi di dong
    private int numberOfCredit; //So tin chi
    private int numberOfLesson; //So buoi hoc

    public Subject() {
    }

    public Subject(int id, int numberOfLesson, int numberOfCredit, String name, String code) {
        this.id = id;
        this.numberOfLesson = numberOfLesson;
        this.numberOfCredit = numberOfCredit;
        this.name = name;
        this.code = code;
    }

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
    }

    public int getNumberOfCredit() {
        return numberOfCredit;
    }

    public void setNumberOfCredit(int numberOfCredit) {
        this.numberOfCredit = numberOfCredit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
