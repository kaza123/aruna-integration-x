/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation_model;

import java.math.BigDecimal;

/**
 *
 * @author kasun
 */
public class Brocker {

    private Integer indexNo;
    private String refNo;
    private String clientNo;
    private String clientName;
    private String clientType;
    private String enterDate;
    private BigDecimal grossValue;
    private BigDecimal vat;
    private BigDecimal nbt;
    private BigDecimal insurence;
    private BigDecimal commission;
    private BigDecimal lotCharges;
    private BigDecimal other1;
    private BigDecimal other2;
    private BigDecimal netValue;
    private Integer Branch;
    private String updatedDate;
    private String updatedTime;
    private boolean check;

    public Brocker() {
    }

    public Brocker(Integer indexNo, String refNo, String clientNo, String clientName, String clientType, String enterDate, BigDecimal grossValue, BigDecimal vat, BigDecimal nbt, BigDecimal insurence, BigDecimal commission, BigDecimal lotCharges, BigDecimal other1, BigDecimal other2, BigDecimal netValue, Integer Branch, String updatedDate, String updatedTime, boolean check) {
        this.indexNo = indexNo;
        this.refNo = refNo;
        this.clientNo = clientNo;
        this.clientName = clientName;
        this.clientType = clientType;
        this.enterDate = enterDate;
        this.grossValue = grossValue;
        this.vat = vat;
        this.nbt = nbt;
        this.insurence = insurence;
        this.commission = commission;
        this.lotCharges = lotCharges;
        this.other1 = other1;
        this.other2 = other2;
        this.netValue = netValue;
        this.Branch = Branch;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.check = check;
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

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public BigDecimal getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(BigDecimal grossValue) {
        this.grossValue = grossValue;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getNbt() {
        return nbt;
    }

    public void setNbt(BigDecimal nbt) {
        this.nbt = nbt;
    }

    public BigDecimal getInsurence() {
        return insurence;
    }

    public void setInsurence(BigDecimal insurence) {
        this.insurence = insurence;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getLotCharges() {
        return lotCharges;
    }

    public void setLotCharges(BigDecimal lotCharges) {
        this.lotCharges = lotCharges;
    }

    public BigDecimal getOther1() {
        return other1;
    }

    public void setOther1(BigDecimal other1) {
        this.other1 = other1;
    }

    public BigDecimal getOther2() {
        return other2;
    }

    public void setOther2(BigDecimal other2) {
        this.other2 = other2;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public Integer getBranch() {
        return Branch;
    }

    public void setBranch(Integer Branch) {
        this.Branch = Branch;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
