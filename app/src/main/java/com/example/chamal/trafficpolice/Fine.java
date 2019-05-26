package com.example.chamal.trafficpolice;

public class Fine {
    private String fineId;
    private String finePlace;
    private String driverLicenseNo;
    private String driverVehicleNo;
    private String fineTime;
    private String validUntilDate;
    private String fineDate;
    private String policemanId;
    private Boolean fineStatus;
    private int totalAmountPaid;

    public Fine(String fineId, String finePlace, String driverLicenseNo, String driverVehicleNo, String fineTime, String validUntilDate, String fineDate, String policemanId, Boolean fineStatus, int totalAmountPaid) {
        this.fineId = fineId;
        this.finePlace = finePlace;
        this.driverLicenseNo = driverLicenseNo;
        this.driverVehicleNo = driverVehicleNo;
        this.fineTime = fineTime;
        this.validUntilDate = validUntilDate;
        this.fineDate = fineDate;
        this.policemanId = policemanId;
        this.fineStatus = fineStatus;
        this.totalAmountPaid = totalAmountPaid;
    }

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

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    public String getDriverVehicleNo() {
        return driverVehicleNo;
    }

    public void setDriverVehicleNo(String driverVehicleNo) {
        this.driverVehicleNo = driverVehicleNo;
    }

    public String getFineTime() {
        return fineTime;
    }

    public void setFineTime(String fineTime) {
        this.fineTime = fineTime;
    }

    public String getValidUntilDate() {
        return validUntilDate;
    }

    public void setValidUntilDate(String validUntilDate) {
        this.validUntilDate = validUntilDate;
    }

    public String getFineDate() {
        return fineDate;
    }

    public void setFineDate(String fineDate) {
        this.fineDate = fineDate;
    }

    public String getPolicemanId() {
        return policemanId;
    }

    public void setPolicemanId(String policemanId) {
        this.policemanId = policemanId;
    }

    public Boolean getFineStatus() {
        return fineStatus;
    }

    public void setFineStatus(Boolean fineStatus) {
        this.fineStatus = fineStatus;
    }

    public int getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(int totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }
}
