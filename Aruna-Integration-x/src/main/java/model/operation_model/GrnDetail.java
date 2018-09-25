/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation_model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author kasun
 */
public class GrnDetail implements Serializable {

    private Integer indexNo;
    private Integer grn;
    private String itemNo;
    private String itemName;
    private String itemType;
    private String itemUnit;
    private String itemBarcode;
    private String itemCategory;
    private String itemBrand;
    private String itemSubCategory;
    private String itemGroup;
    private BigDecimal cost;
    private BigDecimal qty;
    private BigDecimal value;
    private BigDecimal discount;
    private BigDecimal discountValue;
    private BigDecimal netValue;
    private BigDecimal reorderMax;
    private BigDecimal reorderMin;
    private BigDecimal salesPrice;

    public GrnDetail() {
    }

    public GrnDetail(Integer indexNo, Integer grn, String itemNo, String itemName, String itemType, String itemUnit, String itemBarcode, String itemCategory, String itemBrand, String itemSubCategory, String itemGroup, BigDecimal cost, BigDecimal qty, BigDecimal value, BigDecimal discount, BigDecimal discountValue, BigDecimal netValue, BigDecimal reorderMax, BigDecimal reorderMin, BigDecimal salesPrice) {
        this.indexNo = indexNo;
        this.grn = grn;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemUnit = itemUnit;
        this.itemBarcode = itemBarcode;
        this.itemCategory = itemCategory;
        this.itemBrand = itemBrand;
        this.itemSubCategory = itemSubCategory;
        this.itemGroup = itemGroup;
        this.cost = cost;
        this.qty = qty;
        this.value = value;
        this.discount = discount;
        this.discountValue = discountValue;
        this.netValue = netValue;
        this.reorderMax = reorderMax;
        this.reorderMin = reorderMin;
        this.salesPrice = salesPrice;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemSubCategory() {
        return itemSubCategory;
    }

    public void setItemSubCategory(String itemSubCategory) {
        this.itemSubCategory = itemSubCategory;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public BigDecimal getReorderMax() {
        return reorderMax;
    }

    public void setReorderMax(BigDecimal reorderMax) {
        this.reorderMax = reorderMax;
    }

    public BigDecimal getReorderMin() {
        return reorderMin;
    }

    public void setReorderMin(BigDecimal reorderMin) {
        this.reorderMin = reorderMin;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getGrn() {
        return grn;
    }

    public void setGrn(Integer grn) {
        this.grn = grn;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

}
