package com.example.models;

import java.io.Serializable;

public class Class implements Serializable {
    private int id;
    private String code; //ma lop hoc vi du 21DTHE4
    private int quantity;
    private int facultyId;

    public Class() {
    }

    public Class(int id, int facultyId, int quantity, String code) {
        this.id = id;
        this.facultyId = facultyId;
        this.quantity = quantity;
        this.code = code;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
