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
public class SupplierIssueDetail {

    private Integer indexNo;
    private Integer issueSummary;
    private String itemNo;
    private String itemName;
    private String itemType;
    private String itemUnit;
    private String itemBarcode;
    private String itemBrand;
    private String itemCategory;
    private BigDecimal costPrice;
    private BigDecimal salesPrice;
    private BigDecimal qty;
    private BigDecimal stockRemoveQty;
    private BigDecimal value;
    private Boolean isZeroItem;

    public SupplierIssueDetail() {
    }

    public SupplierIssueDetail(Integer indexNo, Integer issueSummary, String itemNo, String itemName, String itemType, String itemUnit, String itemBarcode, String itemBrand, String itemCategory, BigDecimal costPrice, BigDecimal salesPrice, BigDecimal qty, BigDecimal stockRemoveQty, BigDecimal value, Boolean isZeroItem) {
        this.indexNo = indexNo;
        this.issueSummary = issueSummary;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemUnit = itemUnit;
        this.itemBarcode = itemBarcode;
        this.itemBrand = itemBrand;
        this.itemCategory = itemCategory;
        this.costPrice = costPrice;
        this.salesPrice = salesPrice;
        this.qty = qty;
        this.stockRemoveQty = stockRemoveQty;
        this.value = value;
        this.isZeroItem = isZeroItem;
    }

    public Integer getIssueSummary() {
        return issueSummary;
    }

    public void setIssueSummary(Integer issueSummary) {
        this.issueSummary = issueSummary;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getStockRemoveQty() {
        return stockRemoveQty;
    }

    public void setStockRemoveQty(BigDecimal stockRemoveQty) {
        this.stockRemoveQty = stockRemoveQty;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getIsZeroItem() {
        return isZeroItem;
    }

    public void setIsZeroItem(Boolean isZeroItem) {
        this.isZeroItem = isZeroItem;
    }

}
