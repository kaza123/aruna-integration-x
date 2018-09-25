/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author chama
 */
public interface Constant {
    //acc setting
    public static final String CUSTOMER_SUB_ACCOUNT_OF = "customer_sub_account_of";
    public static final String SUPPLIER_SUB_ACCOUNT_OF = "supplier_sub_account_of";
    public static final String STOCK_ITEM_SUB_ACCOUNT_OF = "stock_item_sub_account_of";
    public static final String SERVICE_ITEM_SUB_ACCOUNT_OF = "service_item_sub_account_of";
    public static final String NON_STOCK_ITEM_SUB_ACCOUNT_OF = "non_stock_item_sub_account_of";
    public static final String ITEM_SALES_CASH_IN = "item_sales_cash_in";
    public static final String CHEQUE_IN_HAND = "cheque_in_hand";
    public static final String ITEM_DISCOUNT_OUT = "item_discount_out";
    public static final String ITEM_SALES_INCOME = "item_sales";
    public static final String SERVICE_SALES_INCOME = "service_sales";
    public static final String NBT_ACCOUNT_OUT = "nbt_account_out";
    public static final String VAT_ACCOUNT_OUT = "vat_account_out";
    public static final String NBT_ACCOUNT_IN = "nbt_account_in";
    public static final String VAT_ACCOUNT_IN = "vat_account_in";
    public static final String STOCK_ADJUSTMENT_CONTROL_ACCOUNT = "stock_adjustment_contol_account";
//    public static final String UNREALIZED_ISSUED = "unrealized_issued";
//    public static final String UNREALIZED_RECEIVED = "unrealized_received";
//    public static final String OVER_PAYMENT_ISSUED = "over_payment_issue";
//    public static final String OVER_PAYMENT_RECEIVED = "over_payment_received";
//    public static final String INVENTORY = "inventory";
//    public static final String ITEM_SALES = "item_sales";
//    public static final String ITEM_DISCOUNT_OUT = "item_discount_out";
    
    // master type
    public static final String CUSTOMER = "CUSTOMER";
    public static final String SUPPLIER = "SUPPLIER";
    public static final String INVOICE = "INVOICE";
    public static final String SUPPLIER_NORMAL = "NORMAL";
    public static final String VEHICLE = "VEHICLE";
    public static final String ACCRUED_SUPPLIER= "ACCRUED_SUPPLIER";
    public static final String GL_SUPPLIER = "GL_SUPPLIER";
    public static final String ITEM = "ITEM";
    public static final String LOCAL = "LOCAL";
    public static final String BROKER = "BROKER";
    public static final String GRN = "GRN";
    public static final String PAYMENT = "PAYMENT";
    public static final String ROUTE = "ROUTE";
    public static final String SUPPLIER_ADVANCE = "SUPPLIER_ADVANCE";
    public static final String SUPPLIER_LOAN = "SUPPLIER_LOAN";
    public static final String SUPPLIER_SETTLEMENT = "SUPPLIER_SETTLEMENT";
    public static final String EMPLOYEE_ADVANCE = "EMPLOYEE_ADVANCE";
    public static final String EMPLOYEE_LOAN = "EMPLOYEE_LOAN";
    public static final String EMPLOYEE_SETTLEMENT = "EMPLOYEE_SETTLEMENT";
    public static final String SUPPLIER_TEA = "SUPPLIER_TEA";
    public static final String SUPPLIER_FERTILIZER = "SUPPLIER_FERTILIZER";
    public static final String SUPPLIER_OTHER = "SUPPLIER_OTHER";
    public static final String EMPLOYEE_TEA = "EMPLOYEE_TEA";
    public static final String EMPLOYEE_FERTILIZER = "EMPLOYEE_FERTILIZER";
    public static final String EMPLOYEE_OTHER = "EMPLOYEE_OTHER";
    public static final String FINAL_PAYNEMT = "FINAL_PAYNEMT";
    public static final String CHEQUE_PRINT = "CHEQUE_PRINT";
    public static final String BANK_DEPOSIT = "BANK_DEPOSIT";
    
    
    
    //form name
    public static final String SYSTEM_INTEGRATION = "SYSTEM_INTEGRATION";
    public static final String SYSTEM_INTEGRATION_GRN = "SYSTEM_INTEGRATION_GRN";
    public static final String SYSTEM_INTEGRATION_INVOICE = "SYSTEM_INTEGRATION_INVOICE";
    public static final String SYSTEM_INTEGRATION_PAYMENT = "SYSTEM_INTEGRATION_PAYMENT";
    public static final String SYSTEM_INTEGRATION_STOCK_ADJUSTMENT = "SYSTEM_INTEGRATION_STOCK_ADJUSTMENT";
    public static final String ITEM_CHANGE_FORM = "ITEM_CHANGE";
    public static final String ADJUSTMENT = "ADJUSTMENT";
    
    //status
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_DELETED = "DELETED";
    public static final String STATUS_REJECT = "REJECT";
    
    //item type  
    public static final String ITEM_NON_STOCK = "NON_STOCK";
    public static final String ITEM_STOCK = "STOCK";
    public static final String ITEM_SERVICE = "SERVICE";
    
    //stock
    public static final String STOCK_MAIN = "STOCK MAIN";
    public static final String STOCK_FRONT = "STOCK FRONT";
    public static final String STOCK_OTHER = "STOCK OTHER";
    
    //item unit type
    public static final String ITEM_UNIT_MAIN = "MAIN";
    public static final String ITEM_UNIT_SUB = "OTHER";
    
    // payment type
    public static final String PAYMENT_CASH = "CASH";
    public static final String PAYMENT_CHEQUE = "CHEQUE";
    public static final String PAYMENT_CARD = "CARD";
    
    //form Code
    public static final String CODE_INTEGRATION_GRN = "ISGRN";
    public static final String CODE_INTEGRATION_INVOICE = "ISINV";
    public static final String CODE_INTEGRATION_PAYMENT = "ISPMT";
    public static final String CODE_INTEGRATION_STOCK_ADJUSTMENT = "ISSTKAJM";
//    public static final String CODE_JOURNAL = "JNL";
//    public static final String CODE_BANK_RECONCILIATION = "RECON";
//    public static final String CODE_SUPPLIER_PAYMENT = "SUPPAY";
//    public static final String CODE_ACCRUED_BILL = "ACRDBIL";
//    public static final String CODE_FUND_TRANSFER = "FNDTRA";
//    public static final String CODE_CHEQUE_RETURN = "CHQRTN";
//    public static final String CODE_PAYMENT_VOUCHER = "PAYVUC";
//    public static final String CODE_DIRECT_GRN = "DGRN";
//    public static final String CODE_GRN = "GRN";
//    public static final String CODE_ITEM_SALES = "ITMSAL";
    
    
    
    
}
