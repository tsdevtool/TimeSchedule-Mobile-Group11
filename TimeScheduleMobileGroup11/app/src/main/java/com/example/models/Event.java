package com.example.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Event implements Serializable {
    private int id;
    private String title; //tieu de cua su kien
    private String description;
    private String location; //dia diem to chuc su kien vi du toa E1
    private String image; //Hinh anh cua su kien
    private String time; //Thoi gian to chuc su kien
    private String classroom_code; //Ma lop to chuc su kien vi du phong E1.03.02
    private int statusId; //Trang thai

    public Event() {
    }

    public Event(  String classroom_code, String time, String image, String description, String title, String location) {
        this.classroom_code = classroom_code;
        this.time = time;
        this.image = image;
        this.description = description;
        this.title = title;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getClassroom_code() {
        return classroom_code;
    }

    public void setClassroom_code(String classroom_code) {
        this.classroom_code = classroom_code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
