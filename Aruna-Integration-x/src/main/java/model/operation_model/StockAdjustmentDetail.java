/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation_model;

import java.math.BigDecimal;
import javax.persistence.Column;

/**
 *
 * @author chama
 */
public class StockAdjustmentDetail {

    @Column(name = "index_no")
    private Integer indexNo;

    @Column(name = "stock_adjustment")
    private Integer stockAdjustment;

    @Column(name = "item_no")
    private String itemNo;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_unit")
    private String itemUnit;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "qty")
    private BigDecimal qty;

    public StockAdjustmentDetail() {
    }

    public StockAdjustmentDetail(Integer indexNo, Integer stockAdjustment, String itemNo, String itemName, String itemUnit, String barcode, BigDecimal costPrice, BigDecimal qty) {
        this.indexNo = indexNo;
        this.stockAdjustment = stockAdjustment;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.barcode = barcode;
        this.costPrice = costPrice;
        this.qty = qty;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public Integer getStockAdjustment() {
        return stockAdjustment;
    }

    public void setStockAdjustment(Integer stockAdjustment) {
        this.stockAdjustment = stockAdjustment;
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

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

}
