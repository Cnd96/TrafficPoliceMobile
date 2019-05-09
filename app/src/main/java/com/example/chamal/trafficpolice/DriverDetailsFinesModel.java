package com.example.chamal.trafficpolice;

public class DriverDetailsFinesModel {

    private int id;
    private int amount;
    private int totalAmountPaid;
    private boolean fineStatus;
    private String date;

    public DriverDetailsFinesModel(int id, int amount, int totalAmountPaid, boolean fineStatus, String date) {
        this.id = id;
        this.amount = amount;
        this.totalAmountPaid = totalAmountPaid;
        this.fineStatus = fineStatus;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(int totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public boolean isFineStatus() {
        return fineStatus;
    }

    public void setFineStatus(boolean fineStatus) {
        this.fineStatus = fineStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
