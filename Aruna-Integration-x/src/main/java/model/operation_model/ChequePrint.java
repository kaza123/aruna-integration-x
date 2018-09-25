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
public class ChequePrint {
    private Integer indexNo;
    private String refNo;
    private String type;
    private String routeNo;
    private String enterDate;
    private String chequeDate;
    private String updatedDate;
    private String updatedTime;
    private Integer bankAccount;
    private Integer branch;
    private Boolean check;

    public ChequePrint() {
    }

    public ChequePrint(Integer index_no, String refNo, String type, String routeNo, String enterDate, String chequeDate, String updatedDate, String updatedTime, Integer bankAccount, Integer branch, Boolean check) {
        this.indexNo = index_no;
        this.refNo = refNo;
        this.type = type;
        this.routeNo = routeNo;
        this.enterDate = enterDate;
        this.chequeDate = chequeDate;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.bankAccount = bankAccount;
        this.branch = branch;
        this.check = check;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer index_no) {
        this.indexNo = index_no;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
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

    public Integer getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Integer bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public Boolean getChech() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
    
    
}
