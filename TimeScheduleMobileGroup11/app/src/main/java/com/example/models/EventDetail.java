package com.example.models;

import java.io.Serializable;

public class EventDetail implements Serializable {
    private String question;
    private int userId;
    private int eventId;

    public EventDetail() {
    }

    public EventDetail(String question, int eventId, int userId) {
        this.question = question;
        this.eventId = eventId;
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
