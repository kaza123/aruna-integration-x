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
public class FinalPaymentDetail {

    private Integer indexNo;
    private Integer finalPayment;
    private String routeNo;
    private String routeName;
    private String supplierNo;
    private String supplierName;
    private BigDecimal glValue;
    private BigDecimal transport;
    private BigDecimal advanceInterest;
    private BigDecimal loanCapital;
    private BigDecimal loanInterest;
    private BigDecimal fertilizer;
    private BigDecimal other;
    private BigDecimal stamp;
    private BigDecimal cash;
    private BigDecimal bank;
    private BigDecimal cheque;

    public FinalPaymentDetail() {
    }

    public FinalPaymentDetail(Integer indexNo, Integer finalPayment, String routeNo, String routeName, String supplierNo, String supplierName, BigDecimal glValue, BigDecimal transport, BigDecimal advanceInterest, BigDecimal loanCapital, BigDecimal loanInterest, BigDecimal fertilizer, BigDecimal other, BigDecimal stamp, BigDecimal cash, BigDecimal bank, BigDecimal cheque) {
        this.indexNo = indexNo;
        this.finalPayment = finalPayment;
        this.routeNo = routeNo;
        this.routeName = routeName;
        this.supplierNo = supplierNo;
        this.supplierName = supplierName;
        this.glValue = glValue;
        this.transport = transport;
        this.advanceInterest = advanceInterest;
        this.loanCapital = loanCapital;
        this.loanInterest = loanInterest;
        this.fertilizer = fertilizer;
        this.other = other;
        this.stamp = stamp;
        this.cash = cash;
        this.bank = bank;
        this.cheque = cheque;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public Integer getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(Integer finalPayment) {
        this.finalPayment = finalPayment;
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

    public BigDecimal getGlValue() {
        return glValue;
    }

    public void setGlValue(BigDecimal glValue) {
        this.glValue = glValue;
    }

    public BigDecimal getTransport() {
        return transport;
    }

    public void setTransport(BigDecimal transport) {
        this.transport = transport;
    }

    public BigDecimal getAdvanceInterest() {
        return advanceInterest;
    }

    public void setAdvanceInterest(BigDecimal advanceInterest) {
        this.advanceInterest = advanceInterest;
    }

    public BigDecimal getLoanCapital() {
        return loanCapital;
    }

    public void setLoanCapital(BigDecimal loanCapital) {
        this.loanCapital = loanCapital;
    }

    public BigDecimal getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(BigDecimal loanInterest) {
        this.loanInterest = loanInterest;
    }

    public BigDecimal getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(BigDecimal fertilizer) {
        this.fertilizer = fertilizer;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }

    public BigDecimal getStamp() {
        return stamp;
    }

    public void setStamp(BigDecimal stamp) {
        this.stamp = stamp;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getBank() {
        return bank;
    }

    public void setBank(BigDecimal bank) {
        this.bank = bank;
    }

    public BigDecimal getCheque() {
        return cheque;
    }

    public void setCheque(BigDecimal cheque) {
        this.cheque = cheque;
    }

}
