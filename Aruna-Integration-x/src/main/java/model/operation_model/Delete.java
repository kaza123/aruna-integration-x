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
public class Delete {
    private Integer indexNo;
    private String refNo;
    private String type;
    private String enterDate;
    private String updatedDate;
    private String updatedTime;
    private boolean check;

    public Delete() {
    }

    public Delete(Integer indexNo, String refNo, String type, String enterDate, String updatedDate, String updatedTime, boolean check) {
        this.indexNo = indexNo;
        this.refNo = refNo;
        this.type = type;
        this.enterDate = enterDate;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
}
