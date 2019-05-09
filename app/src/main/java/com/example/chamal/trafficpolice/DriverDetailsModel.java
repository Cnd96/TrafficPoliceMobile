package com.example.chamal.trafficpolice;

import java.util.ArrayList;

public class DriverDetailsModel {

    private String Name;
    private String LicenseNO;
    private String Address;
    private String DateOfIssue;
    private String DateOfExpire;
    private ArrayList<String> CatogeriesOfVehicles;
    private ArrayList<DriverDetailsFinesModel> DriverFinesList;


    private static final DriverDetailsModel ourInstance = new DriverDetailsModel();

    public static DriverDetailsModel getInstance() {
        return ourInstance;
    }

    private DriverDetailsModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLicenseNO() {
        return LicenseNO;
    }

    public void setLicenseNO(String licenseNO) {
        LicenseNO = licenseNO;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDateOfIssue() {
        return DateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        DateOfIssue = dateOfIssue;
    }

    public String getDateOfExpire() {
        return DateOfExpire;
    }

    public void setDateOfExpire(String dateOfExpire) {
        DateOfExpire = dateOfExpire;
    }

    public ArrayList<String> getCatogeriesOfVehicles() {
        return CatogeriesOfVehicles;
    }

    public void setCatogeriesOfVehicles(ArrayList<String> catogeriesOfVehicles) {
        CatogeriesOfVehicles = catogeriesOfVehicles;
    }

    public ArrayList<DriverDetailsFinesModel> getDriverFinesList() {
        return DriverFinesList;
    }

    public void setDriverFinesList(ArrayList<DriverDetailsFinesModel> driverFinesList) {
        DriverFinesList = driverFinesList;
    }
}
