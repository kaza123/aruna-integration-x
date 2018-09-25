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
public class ChequePrintDetail {
    private Integer indexNo;
    private Integer chequePrint;
    private String supplierNo;
    private String chequeNo;
    private BigDecimal amount;

    public ChequePrintDetail() {
    }

    public ChequePrintDetail(Integer indexNo, Integer chequePrint, String supplierNo, String chequeNo, BigDecimal amount) {
        this.indexNo = indexNo;
        this.chequePrint = chequePrint;
        this.supplierNo = supplierNo;
        this.chequeNo = chequeNo;
        this.amount = amount;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public Integer getChequePrint() {
        return chequePrint;
    }

    public void setChequePrint(Integer chequePrint) {
        this.chequePrint = chequePrint;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

  
    
    
}
