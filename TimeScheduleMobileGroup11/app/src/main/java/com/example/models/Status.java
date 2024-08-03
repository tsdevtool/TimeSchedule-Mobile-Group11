package com.example.models;

import java.io.Serializable;

public class Status implements Serializable {
    private int id;
    private String status; //Thong tin duoc set mac dinh

    public Status() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
