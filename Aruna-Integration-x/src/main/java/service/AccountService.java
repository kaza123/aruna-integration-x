/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Constant;
import controller.AccountController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.account_model.MAccAccount;
import model.account_model.MBranch;
import model.account_model.MClient;
import model.account_model.MSupplier;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
import model.account_model.TTypeIndexDetail;
import model.operation_model.Advance;
import model.operation_model.AdvanceDetail;
import model.operation_model.Brocker;
import model.operation_model.ChequePrint;
import model.operation_model.ChequePrintDetail;
import model.operation_model.Delete;
import model.operation_model.FinalPayment;
import model.operation_model.FinalPaymentDetail;
import model.operation_model.Invoice;
import model.operation_model.InvoiceDetail;
import model.operation_model.Payment;
import model.operation_model.PaymentDetail;
import model.operation_model.PaymentInformation;
import model.operation_model.StockAdjustment;
import model.operation_model.StockAdjustmentDetail;
import model.operation_model.SupplierIssueDetail;
import model.operation_model.SupplierIssueSummary;
import model.operation_model.TeaIssue;
import model.operation_model.TeaIssueDetail;

/**
 *
 * @author chama
 */
public class AccountService {

    public static HashMap<Integer, Integer> saveSupplier(Grn grn, Integer user, Connection connection) throws SQLException {
        Integer supplierSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SUPPLIER_SUB_ACCOUNT_OF, connection);
        if (supplierSubAccountOf < 0 || supplierSubAccountOf == null) {
            throw new RuntimeException("Supplier Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Supplier");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(grn.getSupName());
        mAccAccount.setSubAccountOf(supplierSubAccountOf);
        mAccAccount.setUser(user);
        Integer accAccountIndex = saveAccAccount(mAccAccount, connection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("Supplier account save fail !");
        }

        MSupplier supplier = new MSupplier();
        supplier.setAccAccount(accAccountIndex);
        supplier.setContactName(grn.getSupName());
        supplier.setName(grn.getSupName());
        supplier.setContactNo(null);
        supplier.setType(Constant.SUPPLIER_NORMAL);

        Integer saveSupplierMaster = AccountController.getInstance().saveSupplierMaster(supplier, connection);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, accAccountIndex);
        map.put(2, saveSupplierMaster);
        return map;
    }

    public static Integer saveAccAccount(MAccAccount accAccount, Connection connection) throws SQLException {

        MAccAccount subAccountOf = AccountController.getInstance().getSubAccount(accAccount.getSubAccountOf(), connection);
        accAccount.setLevel(Integer.parseInt(subAccountOf.getLevel()) + 1 + "");
        accAccount.setAccMain(subAccountOf.getAccMain());
        accAccount.setSubAccountCount(0);
        accAccount.setAccMain(subAccountOf.getAccMain());
        accAccount.setAccCode(subAccountOf.getSubAccountCount() == 0 ? subAccountOf.getAccCode()
                + ".01" : subAccountOf.getAccCode() + (subAccountOf.getSubAccountCount() < 9 ? (".0"
                + (subAccountOf.getSubAccountCount() + 1)) : subAccountOf.getSubAccountCount() <= 99 ? "." + (subAccountOf.getSubAccountCount() + 1)
                : getUpdate99(subAccountOf.getSubAccountCount(), accAccount.getSubAccountOf(), connection)));

        subAccountOf.setIsAccAccount(false);
        subAccountOf.setSubAccountCount(subAccountOf.getSubAccountCount() + 1);
        Integer index = AccountController.getInstance().updateAccAccount(subAccountOf, connection);
        if (index > 0) {
            return AccountController.getInstance().saveAccAccount(accAccount, connection);
        }
        return -1;
    }

    private static String getUpdate99(Integer count, Integer subAccOf, Connection connection) throws SQLException {
        //update
        if (count == 99) {

            List<MAccAccount> findBySubAccountOf = AccountController.getInstance().getSubAccountOfList(subAccOf, connection);
            for (MAccAccount mAccAccount : findBySubAccountOf) {
                Integer last = Integer.parseInt(mAccAccount.getLevel()) - 1;
                String[] split = new String[3];
                split = mAccAccount.getAccCode().split("\\.");

                int lastNo = Integer.parseInt(split[last]);
                if (lastNo < 10) {
                    split[last] = "00" + lastNo;
                } else if (lastNo < 100) {
                    split[last] = "0" + lastNo;

                }
                String accCode = String.join(".", split);
                mAccAccount.setAccCode(accCode);
                AccountController.getInstance().updateAccAccount(mAccAccount, connection);
            }

        }
        //return number
        int lastCount = count + 1;
        return "." + lastCount;
    }

    public static String saveGrnDetail(GrnDetail detail, Grn grn, HashMap<Integer, Integer> map, Integer user, Connection accConnection) throws SQLException {
        detail.setItemNo(map.get(2) + "");
        AccountController.getInstance().saveGrnDetail(detail, accConnection);

        if (detail.getItemType().equals(Constant.ITEM_STOCK)) {
            saveStockLedger(detail, grn, accConnection);
        }

        return detail.getGrn() + "";
    }

    public static HashMap<Integer, Integer> saveItem(GrnDetail detail, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;
        
        switch (detail.getItemType().toUpperCase()) {
            case Constant.ITEM_STOCK :
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            case Constant.ITEM_NON_STOCK:
                itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.NON_STOCK_ITEM_SUB_ACCOUNT_OF, connection);
                break;
            default:
                break;
        }
        if (itemSubAccountOf < 0) {
            throw new RuntimeException(detail.getItemType() + " Sub Account of Setting was Empty !");
        }
        Integer cat = getCategory(detail.getItemCategory(), connection);
        Integer brand = getBrand(detail.getItemBrand(), connection);
        Integer subCat = getSubCategory(detail.getItemSubCategory(), connection);

        detail.setItemCategory(cat + "");
        detail.setItemBrand(brand + "");
        detail.setItemSubCategory(subCat + "");

        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(detail, itemSubAccountOf, connection);
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(detail, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        if (Constant.ITEM_STOCK.equals(detail.getItemType()) || Constant.ITEM_NON_STOCK.equals(detail.getItemType())) {
            if (detail.getReorderMax().doubleValue() > 0 || detail.getReorderMin().doubleValue() > 0) {
                saveReOrderLevel(saveItemMaster, detail, connection);
            }
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, itemSubAccountOf);
        hashMap.put(2, saveItemMaster);
        return hashMap;
    }

    public static Integer saveGrn(Grn grn, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        Integer grnIndex = AccountController.getInstance().saveGrn(grn, map, accConnection);
        if (grnIndex < 0) {
            throw new RuntimeException("Grn Save fail !");
        }
        return grnIndex;
    }

    private static void saveReOrderLevel(Integer item, GrnDetail detail, Connection connection) throws SQLException {
        ArrayList<MBranch> branchList = getBranchList(connection);
        for (MBranch branch : branchList) {
            int saveReOrderLevel = AccountController.getInstance().saveReOrderLevel(item, branch.getIndexNo(), detail.getReorderMax(), detail.getReorderMin(), connection);
            if (saveReOrderLevel < 0) {
                throw new RuntimeException("ReOrder Level Save Fail !");
            }
        }
    }

    public static ArrayList<MBranch> getBranchList(Connection connection) throws SQLException {
        return AccountController.getInstance().getBranchList(connection);
    }

    private static void saveStockLedger(GrnDetail detail, Grn grn, Connection accConnection) throws SQLException {
        Integer saveStock = saveStock(grn.getBranch(), accConnection);

        AccountController.getInstance().saveStockLedger(detail, grn, saveStock, accConnection);

    }

    private static Integer saveStock(Integer branch, Connection accConnection) throws SQLException {
        Integer stockIndex = AccountController.getInstance().findStock(branch, Constant.STOCK_MAIN, accConnection);
        if (stockIndex < 0) {
            //save new default stock
            stockIndex = AccountController.getInstance().saveStock(branch, Constant.STOCK_MAIN, accConnection);
            if (stockIndex < 0) {
                throw new RuntimeException("Default Stock Save Fail !");
            }
        }
        return stockIndex;

    }

    public static void updateSupplier(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        updateSupplierMaster(grn, supplierMap, accConnection);
        updateSupplierAccount(grn, supplierMap, accConnection);
    }

    private static void updateSupplierMaster(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateSupplierMaster(grn, supplierMap, accConnection);

    }

    private static void updateSupplierAccount(Grn grn, HashMap<Integer, Integer> supplierMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateSupplierAccount(grn, supplierMap, accConnection);

    }

    public static void updateItem(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        updateItemMaster(detail, map, accConnection);
        updateItemUnitMaster(detail, map, accConnection);

    }

    private static void updateItemMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemMaster(detail, map, accConnection);
    }

    private static void updateItemUnitMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemUnitMaster(detail, map, accConnection);
    }

    public static Integer saveSupplierLedger(Grn grn, Integer grnIndex, Integer supplierIndex, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveSupplierLedger(grn, grnIndex, supplierIndex, accConnection);
    }

    public static HashMap<Integer, Object> saveAccountLedgerWithSupplierNbtVat(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, Integer user, Connection accConnection) throws SQLException {
        HashMap<Integer, Object> ledgerMap = AccountController.getInstance().saveSupplierAccountLedger(grn, supplierMap, grnIndex, user, accConnection);
        if (grn.getNbtValue().doubleValue() > 0) {
            //save nbt account
            AccountController.getInstance().saveNbtForLedger(grn, supplierMap, grnIndex, ledgerMap, user, accConnection);
        }
        if (grn.getVatValue().doubleValue() > 0) {
            //save vat account
            AccountController.getInstance().saveVatForLedger(grn, supplierMap, grnIndex, ledgerMap, user, accConnection);

        }
        return ledgerMap;
    }

    public static Integer saveAccLedgerWithItem(GrnDetail detail, int branch, HashMap<Integer, Integer> map, HashMap<Integer, Object> ledgerMap, Integer user, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveAccLedgerItem(detail, branch, map, ledgerMap, user, accConnection);
    }

    public static Integer saveTypeIndexDetail(String masterRef, String type, Integer accIndex, Integer accountIndex, Connection operaConnection) throws SQLException {
        TTypeIndexDetail typeIndexDetail = new TTypeIndexDetail();
        typeIndexDetail.setAccountRefId(accIndex);
        typeIndexDetail.setMasterRefId(masterRef);
        typeIndexDetail.setAccountIndex(accountIndex);
        typeIndexDetail.setType(type);
        return AccountController.getInstance().saveTypeIndexDetail(typeIndexDetail, operaConnection);
    }

    public static TTypeIndexDetail CheckTypeIndexDetail(String type, String typeIndex, Connection operaConnection) throws SQLException {
        return AccountController.getInstance().CheckTypeIndexDetail(type, typeIndex, operaConnection);
    }

    public static HashMap<Integer, Integer> saveCustomer(Invoice invoice, Integer user, Connection accConnection) throws SQLException {
        Integer supplierSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.CUSTOMER_SUB_ACCOUNT_OF, accConnection);
        if (supplierSubAccountOf < 0) {
            throw new RuntimeException("Customer Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Customer");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(invoice.getClientName() + "(" + invoice.getClientNo() + ")");
        mAccAccount.setSubAccountOf(supplierSubAccountOf);
        mAccAccount.setUser(user);
        Integer accAccountIndex = saveAccAccount(mAccAccount, accConnection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("Supplier account save fail !");
        }

//        MClient client = new MClient();
//        client.setAccAccount(accAccountIndex);
//        client.setName(invoice.getClientName());
//        client.setBranch(invoice.getBranch());
//        client.setResident(invoice.getClientRecident());
//
//        Integer saveCustomerMaster = AccountController.getInstance().saveCustomerMaster(client, accConnection);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, accAccountIndex);
        map.put(2, 0);
        return map;
    }

    public static HashMap<Integer, Integer> saveCustomer(Payment payment, Integer user, Connection accConnection) throws SQLException {
        Integer supplierSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.CUSTOMER_SUB_ACCOUNT_OF, accConnection);
        if (supplierSubAccountOf < 0) {
            throw new RuntimeException("Customer Sub Account of Setting was Empty !");
        }
        MAccAccount mAccAccount = new MAccAccount();
        mAccAccount.setIndexNo(null);
        mAccAccount.setAccType("COMMON");
        mAccAccount.setCop(false);
        mAccAccount.setDescription("System Integoration new Customer");
        mAccAccount.setIsAccAccount(true);
        mAccAccount.setName(payment.getClientName() + "(" + payment.getClientNo() + ")");
        mAccAccount.setSubAccountOf(supplierSubAccountOf);
        mAccAccount.setUser(user);
        Integer accAccountIndex = saveAccAccount(mAccAccount, accConnection);
        if (accAccountIndex < 0) {
            throw new RuntimeException("Supplier account save fail !");
        }

        MClient client = new MClient();
        client.setAccAccount(accAccountIndex);
        client.setName(payment.getClientName());
        client.setBranch(payment.getBranch());

        Integer saveCustomerMaster = AccountController.getInstance().saveCustomerMaster(client, accConnection);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, accAccountIndex);
        map.put(2, saveCustomerMaster);
        return map;
    }

    public static void updateCustomer(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        updateCustomerMaster(invoice, customerMap, accConnection);
        updateCustomerAccount(invoice, customerMap, accConnection);
    }

    private static void updateCustomerMaster(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateCustomerMaster(invoice, customerMap, accConnection);
    }

    private static void updateCustomerAccount(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateCustomerAccount(invoice, customerMap, accConnection);

    }

    public static HashMap<Integer, Integer> saveInvoice(Invoice invoice, List<InvoiceDetail> invDetailList, HashMap<Integer, Integer> customerMap, Integer vehicle, Integer user, Connection accConnection) throws SQLException {
        HashMap<Integer, Integer> map = AccountController.getInstance().saveInvoice(invoice, invDetailList, customerMap, vehicle, user, accConnection);
        if (map.size() <= 0) {
            throw new RuntimeException("Invoice Save fail !");
        }
        return map;
    }

    public static Integer saveVehicle(Invoice invoice, Integer customer, Connection accConnection) throws SQLException {
        return AccountController.getInstance().saveVehicleMaster(invoice, customer, accConnection);
    }

    public static void updateItemFromInvoice(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        updateItemMaster(detail, map, accConnection);
        updateItemUnitMaster(detail, map, accConnection);
    }

    private static void updateItemMaster(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemMaster(detail, map, accConnection);
    }

    private static void updateItemUnitMaster(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        AccountController.getInstance().updateItemUnitMaster(detail, map, accConnection);
    }

    public static HashMap<Integer, Integer> saveItem(InvoiceDetail detail, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;
//
        itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.SERVICE_ITEM_SUB_ACCOUNT_OF, connection);

        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(detail, itemSubAccountOf, connection);
        if (saveItemMaster <= 0) {
            throw new RuntimeException("Item master save fail !");
        }
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(detail, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, itemSubAccountOf);
        hashMap.put(2, saveItemMaster);
        hashMap.put(3, saveItemUnit);

        return hashMap;
    }

    public static String saveInvoiceDetail(InvoiceDetail detail, Invoice invoice, HashMap<Integer, Integer> itemMap, HashMap<Integer, Integer> invoiceMap, Connection accConnection) throws SQLException {
        Integer saveStock = saveStock(invoice.getBranch(), accConnection);
        AccountController.getInstance().saveInvoiceDetail(detail, invoice, itemMap, invoiceMap, saveStock, accConnection);
        return invoice.getInvoiceNo();
    }

    public static Integer savePayment(Payment payment, Connection accConnection) throws SQLException {
        return AccountController.getInstance().savePayment(payment, accConnection);
    }

    public static void saveCustomerLedger(PaymentDetail paymentDetail1, Integer paymentIndex, Payment payment, TTypeIndexDetail customerTypeIndexDetail, HashMap<Integer, Object> numberMap, Integer user, Connection accConnection) throws SQLException {
        TTypeIndexDetail invTypeIndexDetail = CheckTypeIndexDetail(payment.getClientType()+"-INV", paymentDetail1.getInvoice() + "", accConnection);
        if (invTypeIndexDetail.getType() == null) {
            throw new RuntimeException("Can't find invoice (" + paymentDetail1.getInvoice() + ") from Account System !");
        }
        Integer saveAccountLedger = AccountController.getInstance().saveAccountLedgerCustomer(paymentDetail1, paymentIndex, payment, invTypeIndexDetail, customerTypeIndexDetail, numberMap, user, accConnection);
        if (saveAccountLedger <= 0) {
            throw new RuntimeException("Account Ledger Customer Save fail !");
        }
    }

    public static HashMap<Integer, Object> getAccLedgerNumber(Integer branch, Connection accConnection) throws SQLException {
        return AccountController.getAccLedgerNumber(branch, Constant.SYSTEM_INTEGRATION_PAYMENT, accConnection);

    }

    public static void savePaymentInformation(PaymentInformation paymentInformation, Integer paymentIndex, Payment payment, TTypeIndexDetail customerTypeIndexDetail, HashMap<Integer, Object> numberMap, Integer user, Connection accConnection) throws SQLException {
        //save payment information table
        Integer savePaymentInformation = savePaymentInformation(paymentInformation, paymentIndex, payment, accConnection);
        if (savePaymentInformation <= 0) {
            throw new RuntimeException("Payment Information Save Fail !");
        }
        // payment save acc ledger
        AccountController.getInstance().savePaymentAccLedger(paymentInformation, paymentIndex, payment, customerTypeIndexDetail, numberMap, user, accConnection);

        //
    }

    private static Integer savePaymentInformation(PaymentInformation paymentInformation, Integer paymentIndex, Payment payment, Connection accConnection) throws SQLException {
        if (paymentInformation.getType().equals(Constant.PAYMENT_CASH)) {
            paymentInformation.setBank(null);
            paymentInformation.setBankBranch(null);
            paymentInformation.setCardReader(null);
            paymentInformation.setCardType(null);
            paymentInformation.setChequeDate(null);
            paymentInformation.setPayment(paymentIndex);

            return AccountController.getInstance().savePaymentInformation(paymentInformation, accConnection);

        } else if (paymentInformation.getType().equals(Constant.PAYMENT_CHEQUE)) {
            HashMap<Integer, Integer> map = saveBankAndBankBranch(paymentInformation, accConnection);
            paymentInformation.setBank(map.get(1) + "");
            paymentInformation.setBankBranch(map.get(2) + "");
            paymentInformation.setCardReader(null);
            paymentInformation.setCardType(null);
            paymentInformation.setPayment(paymentIndex);
            paymentInformation.setNumber(paymentInformation.getNumber());
            return AccountController.getInstance().savePaymentInformation(paymentInformation, accConnection);

        } else if (paymentInformation.getType().equals(Constant.PAYMENT_CARD)) {
            HashMap<Integer, Integer> map = saveBankAndBankBranch(paymentInformation, accConnection);
            Integer cardTypeIndex = saveCardType(paymentInformation.getCardType(), accConnection);
            paymentInformation.setBank(map.get(1) + "");
            paymentInformation.setBankBranch(map.get(2) + "");
            paymentInformation.setCardType(cardTypeIndex + "");
            paymentInformation.setPayment(paymentIndex);
            paymentInformation.setNumber(paymentInformation.getNumber());
            Integer cardReaderIndex = AccountController.getInstance().checkCardReader(paymentInformation.getCardReader(), payment.getBranch(), accConnection);
            if (cardReaderIndex <= 0) {
                throw new RuntimeException("Card Reader Setting is empty !");
            }
            return AccountController.getInstance().savePaymentInformation(paymentInformation, accConnection);

        } else {
            throw new RuntimeException("Payment type is Invalided. Available types are CASH,CHEQUE,CARD !");
        }
    }

    private static HashMap<Integer, Integer> saveBankAndBankBranch(PaymentInformation paymentInformation, Connection accConnection) throws SQLException {
        HashMap<Integer, Integer> map = new HashMap<>();
        map = AccountController.getInstance().checkBankAndBankBranch(paymentInformation, accConnection);
        if (map == null) {
            Integer bankIndex = AccountController.getInstance().checkBank(paymentInformation.getBank(), accConnection);
            if (bankIndex <= 0) {
                //save bank and branch
                return AccountController.getInstance().saveBankAndBankBranch(paymentInformation.getBank(), paymentInformation.getBankBranch(), accConnection);
            } else {
                //save only bank branch
                Integer bankBranchIndex = AccountController.getInstance().saveBankBranch(paymentInformation.getBankBranch(), bankIndex, accConnection);
                map.put(1, bankIndex);
                map.put(2, bankBranchIndex);
                return map;
            }
        } else {
            return map;
        }
    }

    private static Integer saveCardType(String cardType, Connection accConnection) throws SQLException {
        Integer checkCardType = AccountController.getInstance().checkCardType(cardType, accConnection);
        if (checkCardType > 0) {
            return checkCardType;
        }
        return AccountController.getInstance().saveCardType(cardType, accConnection);
    }

    public static Integer checkLoginUser(String name, String pswd, Connection accConnection) throws SQLException {
        return AccountController.getInstance().checkLoginUser(name, pswd, accConnection);
    }

    public static HashMap<Integer, Integer> saveItem(StockAdjustmentDetail detail, Connection connection) throws SQLException {
        Integer itemSubAccountOf = -1;

        itemSubAccountOf = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, connection);

        if (itemSubAccountOf < 0) {
            throw new RuntimeException("Item Sub Account of Setting was Empty !");
        }
        Integer saveItemMaster = AccountController.getInstance().saveItemMaster(detail, itemSubAccountOf, connection);
        Integer saveItemUnit = AccountController.getInstance().saveItemUnitMaster(detail, saveItemMaster, connection);
        if (saveItemUnit <= 0) {
            throw new RuntimeException("Item Unit save fail !");
        }

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, itemSubAccountOf);
        hashMap.put(2, saveItemMaster);
        return hashMap;
    }

    public static int saveStockAdjustment(StockAdjustment adjustment, Connection accConnection) throws SQLException {
        return AccountController.saveStockAdjustment(adjustment, accConnection);
    }

    public static Integer saveStockAdjustmentToLedger(StockAdjustment adjustment, Integer user, Integer formIndexNo, Connection accConnection, Connection operaConnection) throws SQLException {

        List<StockAdjustmentDetail> detailList = OperationService.getAdjustmentDetail(adjustment.getIndexNo(), operaConnection);
        if (adjustment.getFormType().equals(Constant.ITEM_CHANGE_FORM)) {
            if (detailList.size() != 2) {
                throw new RuntimeException("Can't find two rows from Item Change Detail !");
            }
            Double totalCost = 0.00;
            for (StockAdjustmentDetail detail : detailList) {
                AccountService.saveStockAdjustmentDetail(detail, formIndexNo, accConnection);
                HashMap<Integer, Integer> itemMap = new HashMap<>();

                TTypeIndexDetail typeIndexDetailItem = AccountService.CheckTypeIndexDetail(Constant.ITEM, detail.getItemNo().trim(), accConnection);
                if (typeIndexDetailItem.getType() == null || typeIndexDetailItem == null) {
                    itemMap = AccountService.saveItem(detail, accConnection);
                    //type index detail save with item
                    Integer typeIndexId = AccountService.saveTypeIndexDetail(detail.getItemNo(), Constant.ITEM, itemMap.get(1), itemMap.get(2), accConnection);

                    if (typeIndexId < 0) {
                        throw new RuntimeException("Type Index detail save fail !");
                    }
                    System.out.println("New Item( " + detail.getItemName() + " ) Save Success !");
                } else {
                    itemMap.put(1, typeIndexDetailItem.getAccountRefId());
                    itemMap.put(2, typeIndexDetailItem.getAccountIndex());
                }
                if (detail.getQty().doubleValue() < 0) {
                    //minis qty
                    totalCost = AccountController.getInstance().saveStockLedger(adjustment, detail, user, formIndexNo, itemMap, accConnection);
                    if (totalCost <= 0) {
                        throw new RuntimeException("Stock Ledger save fail. because stock empty !");
                    }
                    System.out.println("Stock Adjusted from " + Constant.ITEM_CHANGE_FORM + " - " + detail.getItemNo() + " - " + detail.getItemName() + " - (" + detail.getQty() + ")");

                } else {
                    // get total cost and devided by qty
                    System.out.println("Stock Adjusted from " + Constant.ITEM_CHANGE_FORM + " - " + detail.getItemNo().trim() + " - " + detail.getItemName() + " - " + detail.getQty());
                    return AccountController.getInstance().saveStockLedger(adjustment, detail, totalCost, user, formIndexNo, itemMap, accConnection);
                }
            }

        } else if (adjustment.getFormType().equals(Constant.ADJUSTMENT)) {
            if (detailList.size() != 1) {
                throw new RuntimeException("Can't find a row from Item Change Detail !");
            }
            Integer stockAccount = -1;
            Integer stockAdjustmentAccount = -1;

            stockAccount = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ITEM_SUB_ACCOUNT_OF, accConnection);
            stockAdjustmentAccount = AccountController.getInstance().getSubAccountOf(Constant.STOCK_ADJUSTMENT_CONTROL_ACCOUNT, accConnection);

            if (stockAccount < 0) {
                throw new RuntimeException("Stock Account Setting was Empty !");
            }
            if (stockAdjustmentAccount < 0) {
                throw new RuntimeException("Stock Adjustment Account Setting was Empty !");
            }

            StockAdjustmentDetail detail = detailList.get(0);
            HashMap<Integer, Integer> itemMap = new HashMap<>();
            TTypeIndexDetail typeIndexDetailItem = AccountService.CheckTypeIndexDetail(Constant.ITEM, detail.getItemNo().trim(), accConnection);
            if (typeIndexDetailItem.getType() == null || typeIndexDetailItem == null) {

                itemMap = AccountService.saveItem(detail, accConnection);
                //type index detail save with item
                Integer typeIndexId = AccountService.saveTypeIndexDetail(detail.getItemNo(), Constant.ITEM, itemMap.get(1), itemMap.get(2), accConnection);

                if (typeIndexId < 0) {
                    throw new RuntimeException("Type Index detail save fail !");
                }
                System.out.println("New Item( " + detail.getItemName() + " ) Save Success !");
            } else {
                itemMap.put(1, typeIndexDetailItem.getAccountRefId());
                itemMap.put(2, typeIndexDetailItem.getAccountIndex());
            }

            if (detail.getQty().doubleValue() > 0) {
                // plus qty     
//                return AccountController.getInstance().saveStockAdjustmentToAccountPlus(adjustment, detail, stockAccount, stockAdjustmentAccount, user, formIndexNo, itemMap, accConnection);
//                if (detail.getCostPrice().doubleValue() <= 0) {
//                    throw new RuntimeException("Error ! Cost Price is zero !");
//                }
                Integer save = AccountController.getInstance().saveStockLedgerFromAdjustmentPlusQty(adjustment, detail, itemMap, user, formIndexNo, accConnection);
                if (save <= 0) {
                    throw new RuntimeException("Stock Ledger Save Fail !");
                } else {
                    System.out.println("Stock Adjusted from " + Constant.ADJUSTMENT + " - " + detail.getItemNo() + " - " + detail.getItemName() + " - " + detail.getQty());
                    return save;
                }
            } else {
                // minus qty     
                Double totalCost = AccountController.getInstance().saveStockLedgerFromAdjustmentMinusQty(adjustment, detail, itemMap, user, formIndexNo, accConnection);
                if (totalCost < 0) {
                    throw new RuntimeException("Stock Ledger Save Fail !");
                } else {
                    System.out.println("Stock Adjusted from " + Constant.ADJUSTMENT + " - " + detail.getItemNo() + " - " + detail.getItemName() + " - (" + detail.getQty() + ")");
                    return 1;
                }
            }
        } else {
            throw new RuntimeException("Adjustment Type Doesn't Match !");
        }

        return -1;
    }

    public static void saveStockAdjustmentDetail(StockAdjustmentDetail detail, int saveStockAdjustment, Connection accConnection) throws SQLException {
        AccountController.getInstance().saveStockAdjustmentDetail(detail, saveStockAdjustment, accConnection);
    }

    public static Integer tAccLedgerByCustomer(TTypeIndexDetail typeDetail, Integer account, Connection accConnection) throws SQLException {
        return AccountController.getInstance().tAccLedgerByCustomer(typeDetail, account, accConnection);
    }

    public static String getCompanyName(Connection accConnection) throws SQLException {
        return AccountController.getCompantName(accConnection);
    }

    static Integer executeAdvance(Advance advance, List<AdvanceDetail> advanceDetailList, String date, Integer loginUser, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_advance(?,?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(advance));
        ps.setObject(2, mapper.writeValueAsString(advanceDetailList));
        ps.setInt(3, loginUser);
        ps.setString(4, date);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println(advance.getType() + " Save Success. Ref No is : " + advance.getRefNo());
            System.out.println("Account Code : " + rst.getString(2) + "   Amount is : " + rst.getDouble(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }

    public static Integer executeIssue(SupplierIssueSummary summary, List<SupplierIssueDetail> issueDetailList, String date, Integer loginUser, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_supplier_issue(?,?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(summary));
        ps.setObject(2, mapper.writeValueAsString(issueDetailList));
        ps.setInt(3, loginUser);
        ps.setString(4, date);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println(summary.getType() + " Save Success. Ref No is : " + summary.getRefNo());
            System.out.println("Account Code : " + rst.getString(2) + "   Amount is : " + rst.getDouble(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }

    private static Integer getCategory(String name, Connection connection) throws SQLException {
        if (name == null) {
            return null;
        }
        Integer cat = AccountController.getInstance().getCategory(name, connection);
        if (cat <= 0 || cat == null) {
            cat = AccountController.getInstance().saveCategory(name, connection);
        }
        return cat;
    }

    private static Integer getBrand(String name, Connection connection) throws SQLException {
        if (name == null) {
            return null;
        }
        Integer cat = AccountController.getInstance().getBrand(name, connection);
        if (cat <= 0 || cat == null) {
            cat = AccountController.getInstance().saveBrand(name, connection);
        }
        return cat;
    }

    private static Integer getSubCategory(String name, Connection connection) throws SQLException {
        if (name == null) {
            return null;
        }
        Integer cat = AccountController.getInstance().getSubCategory(name, connection);
        if (cat <= 0 || cat == null) {
            cat = AccountController.getInstance().saveSubCategory(name, connection);
        }
        return cat;
    }

    public static Integer executeFinalPayment(FinalPayment summary, List<FinalPaymentDetail> detailList, String date, Integer loginUser, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_final_payment(?,?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(summary));
        ps.setObject(2, mapper.writeValueAsString(detailList));
        ps.setInt(3, loginUser);
        ps.setString(4, date);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println("Final Payment Save Success. Ref No is : " + summary.getRefNo());
            System.out.println("Account Code : " + rst.getString(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }

    public static Integer executeChequePayment(ChequePrint summary, List<ChequePrintDetail> detailList, String date, Integer loginUser, String type, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_cheque_print(?,?,?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(summary));
        ps.setObject(2, mapper.writeValueAsString(detailList));
        ps.setInt(3, loginUser);
        ps.setString(4, date);
        ps.setString(5, type);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println(type + " Save Success. Ref No is : " + summary.getRefNo() + " Route No is " + summary.getRouteNo());
            System.out.println("Account Code : " + rst.getString(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }

    public static Integer executeTeaIssue(TeaIssue summary, List<TeaIssueDetail> detailList, String date, Integer loginUser, String type, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_tea_issue(?,?,?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(summary));
        ps.setObject(2, mapper.writeValueAsString(detailList));
        ps.setInt(3, loginUser);
        ps.setString(4, date);
        ps.setString(5, type);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println(type + " Save Success. Ref No is : " + summary.getRefNo() + " Enter Date is " + summary.getEnterDate());
            System.out.println("Account Code : " + rst.getString(2)+" Total Amount : "+rst.getString(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }

    public static Integer executeBrocker(Brocker summary, String date, Integer loginUser, String type, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_brocker(?,?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(summary));
        ps.setInt(2, loginUser);
        ps.setString(3, date);
        ps.setString(4, type);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println(type + " Save Success. Ref No is : " + summary.getRefNo() + " Enter Date is " + summary.getEnterDate());
            System.out.println("Account Code : " + rst.getString(2)+" Total Amount : "+rst.getString(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }
   

    static Integer executeDelete(Delete delete, String date, Integer loginUser, Connection accountConnection) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String query = "call p_execute_delete(?,?,?)";
        PreparedStatement ps = accountConnection.prepareStatement(query);
        ps.setObject(1, mapper.writeValueAsString(delete));
        ps.setInt(2, loginUser);
        ps.setString(3, date);
        ResultSet rst = ps.executeQuery();
        if (rst.next()) {
            System.out.println(delete.getType() + " Delete Success. Ref No is : " + delete.getRefNo() + " Enter Date is " + delete.getEnterDate());
            System.out.println("Account Code : " + rst.getString(1));
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            return 1;
        }
        return -1;
    }

}
