/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation_model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author kasun
 */
@Entity
@Table(name = "grn")
public class Grn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "index_no")
    private Integer indexNo;

    @Column(name = "grn_no")
    private String grnNo;

    @Column(name = "enter_date")
    private String enterDate;

    @Column(name = "enter_time")
    private String enterTime;

    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "updated_time")
    private String updatedTime;

    @Column(name = "sup_no")
    private String supNo;

    @Column(name = "sup_name")
    private String supName;

    @Column(name = "ref_no")
    private String refNo;

    @Column(name = "branch")
    private int branch;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @Column(name = "nbt")
    private BigDecimal nbt;

    @Column(name = "nbt_value")
    private BigDecimal nbtValue;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "vat_value")
    private BigDecimal vatValue;

    @Column(name = "final_value")
    private BigDecimal finalValue;

    @Column(name = "`check`")
    private boolean check;

    @Column(name = "credit_period")
    private Integer creditPeriod;

    public Grn() {
    }

    public Grn(Integer indexNo, String grnNo, String enterDate, String enterTime, String updatedDate, String updatedTime, String supNo, String supName, String refNo, int branch, BigDecimal totalValue, BigDecimal nbt, BigDecimal nbtValue, BigDecimal vat, BigDecimal vatValue, BigDecimal finalValue, boolean check, Integer creditPeriod) {
        this.indexNo = indexNo;
        this.grnNo = grnNo;
        this.enterDate = enterDate;
        this.enterTime = enterTime;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.supNo = supNo;
        this.supName = supName;
        this.refNo = refNo;
        this.branch = branch;
        this.totalValue = totalValue;
        this.nbt = nbt;
        this.nbtValue = nbtValue;
        this.vat = vat;
        this.vatValue = vatValue;
        this.finalValue = finalValue;
        this.check = check;
        this.creditPeriod = creditPeriod;
    }

    public Integer getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(Integer creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getGrnNo() {
        return grnNo;
    }

    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
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

    public String getSupNo() {
        return supNo;
    }

    public void setSupNo(String supNo) {
        this.supNo = supNo;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getNbt() {
        return nbt;
    }

    public void setNbt(BigDecimal nbt) {
        this.nbt = nbt;
    }

    public BigDecimal getNbtValue() {
        return nbtValue;
    }

    public void setNbtValue(BigDecimal nbtValue) {
        this.nbtValue = nbtValue;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getVatValue() {
        return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
        this.vatValue = vatValue;
    }

    public BigDecimal getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(BigDecimal finalValue) {
        this.finalValue = finalValue;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
