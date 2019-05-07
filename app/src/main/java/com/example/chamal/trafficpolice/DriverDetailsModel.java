package com.example.chamal.trafficpolice;

public class DriverDetailsModel {

    private String Name;
    private String LicenseNO;
    private String Address;
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
}
