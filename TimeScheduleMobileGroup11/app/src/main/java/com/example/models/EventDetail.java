package com.example.models;

import java.io.Serializable;

public class EventDetail implements Serializable {
    private String question;
    private String userId;
    private String eventId;

    public EventDetail() {
    }

    public EventDetail(String question, String userId, String eventId) {
        this.question = question;
        this.userId = userId;
        this.eventId = eventId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "EventDetail{" +
                "question='" + question + '\'' +
                ", userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
