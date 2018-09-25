/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation_model;


/**
 *
 * @author kasun
 */
public class FinalPayment {
    private Integer IndexNo;
    private String refNo;
    private String enterDate;
    private String finalPaymentDate;
    private String updatedDate;
    private String updatedTime;
    private Integer branch;
    private boolean check;

    public FinalPayment() {
    }

    public FinalPayment(Integer IndexNo, String refNo, String enterDate, String finalPaymentDate, String updatedDate, String updatedTime, Integer branch, boolean check) {
        this.IndexNo = IndexNo;
        this.refNo = refNo;
        this.enterDate = enterDate;
        this.finalPaymentDate = finalPaymentDate;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.branch = branch;
        this.check = check;
    }

    public Integer getIndexNo() {
        return IndexNo;
    }

    public void setIndexNo(Integer IndexNo) {
        this.IndexNo = IndexNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getFinalPaymentDate() {
        return finalPaymentDate;
    }

    public void setFinalPaymentDate(String finalPaymentDate) {
        this.finalPaymentDate = finalPaymentDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
}
