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
 * @author chama
 */
public class Payment implements Serializable {

    private Integer indexNo;
    private String number;
    private String clientNo;
    private String clientName;
    private String clientType;
    private String enterDate;
    private String enterTime;
    private String updatedDate;
    private String updatedTime;
    private BigDecimal totalAmount;
    private BigDecimal cashAmount;
    private BigDecimal chequeAmount;
    private BigDecimal cardAmount;
    private BigDecimal overPaymentAmount;
    private Integer branch;
    private Boolean check;
    private Boolean isDownPayment;

    public Payment() {
    }

    public Payment(Integer indexNo, String number, String clientNo, String clientName, String clientType, String enterDate, String enterTime, String updatedDate, String updatedTime, BigDecimal totalAmount, BigDecimal cashAmount, BigDecimal chequeAmount, BigDecimal cardAmount, BigDecimal overPaymentAmount, Integer branch, Boolean check, Boolean isDownPayment) {
        this.indexNo = indexNo;
        this.number = number;
        this.clientNo = clientNo;
        this.clientName = clientName;
        this.clientType = clientType;
        this.enterDate = enterDate;
        this.enterTime = enterTime;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.totalAmount = totalAmount;
        this.cashAmount = cashAmount;
        this.chequeAmount = chequeAmount;
        this.cardAmount = cardAmount;
        this.overPaymentAmount = overPaymentAmount;
        this.branch = branch;
        this.check = check;
        this.isDownPayment = isDownPayment;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Boolean getIsDownPayment() {
        return isDownPayment;
    }

    public void setIsDownPayment(Boolean isDownPayment) {
        this.isDownPayment = isDownPayment;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(BigDecimal chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimal getOverPaymentAmount() {
        return overPaymentAmount;
    }

    public void setOverPaymentAmount(BigDecimal overPaymentAmount) {
        this.overPaymentAmount = overPaymentAmount;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

}
