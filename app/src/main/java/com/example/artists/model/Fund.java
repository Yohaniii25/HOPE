package com.example.artists.model;

public class Fund {

    private String fundId;
    private String fundName;
    private String fundDate;
    private String fundAmount;
    private String fundDescription;
    private String fundCategory;
    private String fundContactName;
    private String fundContactNumber;
    private String fundContactEmail;
    private String fundContactNIC;
//    private String eventCvrImgUrl;

    public Fund(){

    }

    public Fund(String fundId, String fundName, String fundDate, String fundAmount, String fundDescription,
                String fundCategory, String fundContactName, String fundContactNumber, String
                        fundContactEmail, String fundContactNIC) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.fundDate = fundDate;
        this.fundAmount = fundAmount;
        this.fundDescription = fundDescription;
        this.fundCategory = fundCategory;
        this.fundContactName = fundContactName;
        this.fundContactNumber = fundContactNumber;
        this.fundContactEmail = fundContactEmail;
        this.fundContactNIC = fundContactNIC;
//        this.eventCvrImgUrl = eventCvrImgUrl;
    }

    public Fund(String id, String name, String contactNumber) {
        this.fundId = id;
        this.fundName = name;
        this.fundContactNumber = contactNumber;
    }

    public Fund(String fundId, String fund_name, String fund_date, String fund_amount,
                 String fund_description, String fund_category, String fund_contact_name,
                 String fund_contact_number, String fund_contact_mail, String fund_contact_nic,
                 String uri) {
        this.fundId = fundId;
        this.fundName = fund_name;
        this.fundDate = fund_date;
        this.fundAmount = fund_amount;
        this.fundDescription = fund_description;
        this.fundCategory = fund_category;
        this.fundContactName = fund_contact_name;
        this.fundContactNumber = fund_contact_number;
        this.fundContactEmail = fund_contact_mail;
        this.fundContactNIC = fund_contact_nic;
//        this.fundCvrImgUrl = uri;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundDate() {
        return fundDate;
    }

    public void setFundDate(String fundDate) {
        this.fundDate = fundDate;
    }

    public String getFundAmount() {
        return fundAmount;
    }

    public void setFundAmount(String fundAmount) {
        this.fundAmount = fundAmount;
    }

    public String getFundDescription() {
        return fundDescription;
    }

    public void setFundDescription(String fundDescription) {
        this.fundDescription = fundDescription;
    }

    public String getFundCategory() {
        return fundCategory;
    }

    public void setFundCategory(String fundCategory) {
        this.fundCategory = fundCategory;
    }

    public String getFundContactName() {
        return fundContactName;
    }

    public void setFundContactName(String fundContactName) {
        this.fundContactName = fundContactName;
    }

    public String getFundContactNumber() {
        return fundContactNumber;
    }

    public void setFundContactNumber(String fundContactNumber) {
        this.fundContactNumber = fundContactNumber;
    }

    public String getFundContactEmail() {
        return fundContactEmail;
    }

    public void setFundContactEmail(String fundContactEmail) {
        this.fundContactEmail = fundContactEmail;
    }

    public String getFundContactNIC() {
        return fundContactNIC;
    }

    public void setFundContactNIC(String fundContactNIC) {
        this.fundContactNIC = fundContactNIC;
    }
}
