package com.sales.order.Generate.com.sales.order.Entity;

public class inventoryCycleCountEntity {

    private String businessUnit;
    private String itemNumber;
    private String location;
    
    //For Reviews
    private String fromCycleStatus;
    private String thruCycleStatus;
    private String p41240Version;

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

  //For Reviews
    public String getFromCycleStatus() {
        return fromCycleStatus;
    }

    public void setFromCycleStatus(String fromCycleStatus) {
        this.fromCycleStatus = fromCycleStatus;
    }

    public String getThruCycleStatus() {
        return thruCycleStatus;
    }

    public void setThruCycleStatus(String thruCycleStatus) {
        this.thruCycleStatus = thruCycleStatus;
    }

    public String getP41240Version() {
        return p41240Version;
    }

    public void setP41240Version(String p41240Version) {
        this.p41240Version = p41240Version;
    }
}