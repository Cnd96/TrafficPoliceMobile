package com.example.chamal.trafficpolice;

public class SmsSingleton {
    private static final SmsSingleton ourInstance = new SmsSingleton();

    public static SmsSingleton getInstance() {
        return ourInstance;
    }

    private SmsSingleton() {
    }

    private String fineId;
    private String finePlace;
    private String fineTime;
    private String fineDate;

    public String getFineId() {
        return fineId;
    }

    public void setFineId(String fineId) {
        this.fineId = fineId;
    }

    public String getFinePlace() {
        return finePlace;
    }

    public void setFinePlace(String finePlace) {
        this.finePlace = finePlace;
    }

    public String getFineTime() {
        return fineTime;
    }

    public void setFineTime(String fineTime) {
        this.fineTime = fineTime;
    }

    public String getFineDate() {
        return fineDate;
    }

    public void setFineDate(String fineDate) {
        this.fineDate = fineDate;
    }
}
