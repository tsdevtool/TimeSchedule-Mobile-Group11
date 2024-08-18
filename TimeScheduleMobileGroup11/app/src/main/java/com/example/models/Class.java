package com.example.models;

import java.io.Serializable;

public class Class implements Serializable {
    private String id;
    private String code; //ma lop hoc vi du 21DTHE4
    private String quantity;
    private String facultyId;

    public Class() {
    }

    public Class(String id, String facultyId, String quantity, String code) {
        this.id = id;
        this.facultyId = facultyId;
        this.quantity = quantity;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
