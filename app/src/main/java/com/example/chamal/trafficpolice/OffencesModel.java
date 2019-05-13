package com.example.chamal.trafficpolice;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OffencesModel {
    String offence;
    int amount;
    String sectionOfAct;

    public OffencesModel(String offence, int amount,String sectionOfAct) {
        this.offence = offence;
        this.amount = amount;
        this.sectionOfAct=sectionOfAct;
    }

    static Map<String,OffencesModel> offenceMapping=new HashMap<>();

    static public void createOffences(){
        offenceMapping.put("Section130",new OffencesModel("Failure to have a License to drive a specific class of vehicles",1000,"Section130"));
        offenceMapping.put("Section140",new OffencesModel("Non-compliance with Speed limits provisions",2000,"Section140"));
        offenceMapping.put("Section157A",new OffencesModel("Non-use of seat belts",1000,"Section157A"));
        offenceMapping.put("Section153",new OffencesModel("Using inappropiate signals when driving and C.",1000,"Section153"));
        offenceMapping.put("Section155A",new OffencesModel("Excessive emission of smoke and C.",2000,"Section155A"));
        offenceMapping.put("Section152",new OffencesModel("Unobstructed control of vehicle when driving",1000,"Section152"));
        offenceMapping.put("Section158",new OffencesModel("Failure to wear protective helmets when driving",1000,"Section158"));
        offenceMapping.put("Section159",new OffencesModel("Prohibition to distribute advertisements from a vehicle in motion",2000,"Section159"));
        offenceMapping.put("Section160",new OffencesModel("Prohibit excessive use of noise from a vehicle",1000,"Section160"));
        offenceMapping.put("Section164",new OffencesModel("Non-compliance with traffic signs",1000,"Section164"));
        offenceMapping.put("Section190",new OffencesModel("Violation of regulations",1000,"Section190"));
        offenceMapping.put("Section148",new OffencesModel("Failure to comply with road rules",1000,"Section148"));
        offenceMapping.put("Section135",new OffencesModel("Failure to carry a driving licence when driving",2000,"Section135"));

    }

    public String getOffence() {
        return offence;
    }

    public void setOffence(String offence) {
        this.offence = offence;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSectionOfAct() {
        return sectionOfAct;
    }

    public void setSectionOfAct(String sectionOfAct) {
        this.sectionOfAct = sectionOfAct;
    }
}
