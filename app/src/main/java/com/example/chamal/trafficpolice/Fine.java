package com.example.chamal.trafficpolice;

import android.widget.Toast;

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

    public Fine(String fineId, String finePlace, String driverLicenseNo, String driverVehicleNo, String fineTime, String validUntilDate, String fineDate, String policemanId, Boolean fineStatus, int totalAmountPaid) throws Exception {

       try{
           setFineId(fineId);
        setFinePlace(finePlace);
        setDriverLicenseNo(driverLicenseNo);
        setDriverVehicleNo(driverVehicleNo);
        this.fineTime = fineTime;
        this.validUntilDate = validUntilDate;
        this.fineDate = fineDate;
        this.policemanId = policemanId;
        this.fineStatus = fineStatus;
        this.totalAmountPaid = totalAmountPaid;}
        catch (Exception e){
           throw new Exception(e);
        }
    }

    public String getFineId() {
        return fineId;
    }

    public void setFineId(String fineId) throws Exception {

        boolean fineIdValidity=fineId.matches("\\d{4,7}");
        if(!fineIdValidity)
        {
          throw new Exception("Enter valid Fine ID");
        };
        this.fineId = fineId;
    }

    public String getFinePlace() {
        return finePlace;
    }

    public void setFinePlace(String finePlace) throws Exception {
        if(finePlace.length()==0)
        {
            throw new Exception("Enter a place");
        };
        this.finePlace = finePlace;
    }

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) throws Exception {

        boolean licensNoValidity=driverLicenseNo.matches("[A-Z]{1}\\d{7}");

        if(!licensNoValidity)
        {
            throw new Exception("Wrong license number");
        };
        this.driverLicenseNo = driverLicenseNo;
    }

    public String getDriverVehicleNo() {
        return driverVehicleNo;
    }

    public void setDriverVehicleNo(String driverVehicleNo) throws Exception {

        boolean vehicleValidity1=driverVehicleNo.matches("[A-Za-z]{2}-[A-Za-z]{2}-\\d{4}");
        boolean vehicleValidity2=driverVehicleNo.matches("[A-Za-z]{2}-[A-Za-z]{3}-\\d{4}");

        if(!(vehicleValidity1||vehicleValidity2))
        {
            throw new Exception("Wrong vehicle number");
        };
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
