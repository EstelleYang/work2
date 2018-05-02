package com.lzjs.uappoint.bean;

public class DoctorAcceptAmount {
    private String docName;
    private int amount;
    public DoctorAcceptAmount(String docName,int amount){
        this.docName = docName;
        this.amount = amount;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}