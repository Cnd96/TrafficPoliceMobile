package com.example.chamal.trafficpolice;

public class FineOffences {

    private String fineId;
    private String[] sectionOfAct;

    public FineOffences(String fineId, String[] sectionOfAct) {
        this.fineId = fineId;
        this.sectionOfAct = sectionOfAct;
    }

    public String getFineId() {
        return fineId;
    }

    public void setFineId(String fineId) {
        this.fineId = fineId;
    }

    public String[] getSectionOfAct() {
        return sectionOfAct;
    }

    public void setSectionOfAct(String[] sectionOfAct) {
        this.sectionOfAct = sectionOfAct;
    }
}
