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
public class SupplierIssueSummary {

    private Integer indexNo;
    private String refNo;
    private String routeNo;
    private String routeName;
    private String supplierNo;
    private String supplierName;
    private String type;
    private String enterDate;
    private String updatedDate;
    private String updatedTime;
    private Integer branch;
    private boolean check;

    public SupplierIssueSummary() {
    }

    public SupplierIssueSummary(Integer indexNo, String refNo, String routeNo, String routeName, String supplierNo, String supplierName, String type, String enterDate, String updatedDate, String updatedTime, Integer branch, boolean check) {
        this.indexNo = indexNo;
        this.refNo = refNo;
        this.routeNo = routeNo;
        this.routeName = routeName;
        this.supplierNo = supplierNo;
        this.supplierName = supplierName;
        this.type = type;
        this.enterDate = enterDate;
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

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
