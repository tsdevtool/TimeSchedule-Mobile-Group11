package com.example.models;

import java.io.Serializable;

public class Faculty implements Serializable {
    private int id;
    private String code; //Ma khoa vi du DTH la khoa cong nghe thong tin
    private String name; //Ten cua khoa vi du "Cong Nghe Thong Tin"

    public Faculty() {
    }

    public Faculty(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
