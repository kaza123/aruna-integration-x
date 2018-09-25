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
public class TeaIssue {
    private Integer indexNo;
    private String refNo;
    private String enterDate;
    private String type;
    private String updatedDate;
    private String updatedTime;
    private Integer branch;
    private Boolean check;

    public TeaIssue() {
    }

    public TeaIssue(Integer indexNo, String refNo, String enterDate, String type, String updatedDate, String updatedTime, Integer branch, Boolean check) {
        this.indexNo = indexNo;
        this.refNo = refNo;
        this.enterDate = enterDate;
        this.type = type;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.branch = branch;
        this.check = check;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
    
    
}
