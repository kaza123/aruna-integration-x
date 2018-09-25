/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync_service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import model.operation_model.Grn;
import model.operation_model.Invoice;
import model.operation_model.Payment;
import model.operation_model.StockAdjustment;
import service.OperationService;
import service.TransactionService;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SyncService {

    private final OperationService operationService;

    private static SyncService instance;

    public static SyncService getInstance() throws SQLException {
        if (instance == null) {
            instance = new SyncService();
        }

        return instance;
    }

    public SyncService() throws SQLException {
        this.operationService = new OperationService();
    }

    public void executeGrn(String date, Integer user, JScrollBar vertical, JProgressBar proBarLoading) throws SQLException {
        ArrayList<Grn> grnList = operationService.getNotCheckGrnList(date);
        if (grnList.isEmpty()) {
            System.out.println("Integration Grn is empty!");
        } else {
            System.out.println("Finded " + grnList.size() + " Grn to Integrate with account System !");
        }
        int updatedSize=0;
        for (Grn grn : grnList) {
            TransactionService.getInstance().saveGrn(grn, user);
            updatedSize++;
            proBarLoading.setValue(Integer.parseInt(100 / grnList.size() * updatedSize + ""));
            vertical.setValue(vertical.getMaximum());
        }
    }

    public void executeInvoice(String date, Integer user, JScrollBar vertical, JProgressBar proBarLoading) throws SQLException {
        ArrayList<Invoice> invoiceList = operationService.getNotCheckInvoiceList(date);
        if (invoiceList.isEmpty()) {
            System.out.println("Integration Invoice is empty!");
        } else {
            System.out.println("Finded " + invoiceList.size() + " Invoice to Integrate with account System !");
        }
        int updatedSize=0;
        for (Invoice invoice : invoiceList) {
            TransactionService.getInstance().saveInvoice(invoice, user);
             updatedSize++;
            proBarLoading.setValue(Integer.parseInt(100 / invoiceList.size() * updatedSize + ""));
            vertical.setValue(vertical.getMaximum());
        }

    }

    public void executePayment(String date, Integer user, JScrollBar vertical, JProgressBar proBarLoading) throws SQLException {
        ArrayList<Payment> paymentList = operationService.getNotCheckPaymentList(date);
        if (paymentList.isEmpty()) {
            System.out.println("Integration Payment is empty!");
        } else {
            System.out.println("Finded " + paymentList.size() + " Payment to Integrate with account System !");
        }
        int updatedSize=0;
        for (Payment payment : paymentList) {
            TransactionService.getInstance().savePayment(payment, user);
              updatedSize++;
            proBarLoading.setValue(Integer.parseInt(100 / paymentList.size() * updatedSize + ""));
            vertical.setValue(vertical.getMaximum());
        }
    }

    public Integer getGrnCount(String date) throws SQLException {
        return operationService.getNotGrnCount(date);
    }

    public Integer getInvoiceCount(String date) throws SQLException {
        return operationService.getNotCheckInvoiceCount(date);
    }

    public Integer getPaymentCount(String date) throws SQLException {
        return operationService.getNotCheckPaymentCount(date);
    }

    public Integer checkLoginUser(String name, String pswd) throws SQLException {
        return TransactionService.getInstance().checkLoginUser(name, pswd);
    }

    public Integer getStockAdjustmentCount(String date) throws SQLException {
        return operationService.getNotStockAdjustmentCount(date);
    }

    public void executeStockAdjustment(String date, Integer user, JScrollBar vertical, JProgressBar proBarLoading) throws SQLException {
        ArrayList<StockAdjustment> stockAdjustmentList = operationService.getNotCheckStockAdjustmentList(date);
        if (stockAdjustmentList.isEmpty()) {
            System.out.println("Integration Stock Adjustment is empty!");
        } else {
            System.out.println("Finded " + stockAdjustmentList.size() + " StockAdjustment to Integrate with account System !");
        }
        int updatedSize=0;
        for (StockAdjustment adjustment : stockAdjustmentList) {
            TransactionService.getInstance().saveStockAdjustment(adjustment, user);
            updatedSize++;
            proBarLoading.setValue(Integer.parseInt(100 / stockAdjustmentList.size() * updatedSize + ""));
            vertical.setValue(vertical.getMaximum());
        }
    }

    public String getTransactionDate() throws SQLException {
        return operationService.getTransactionDate();
    }

    public String getNextDate(String date) throws SQLException, ParseException {
        return operationService.getNextDate(date);
    }

    public HashMap getAdvanceCount(String date) throws SQLException {
        return operationService.getAdvanceCount(date);
    }

    public HashMap getSupplierIssueCount(String date) throws SQLException {
        return operationService.getSupplierIssueCount(date);
    }

    public void executeSupplierAdvance(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeSupplierAdvance(date,loginUser,vertical,type,proBarLoading);
    }

    public void executeSupplierIssue(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeSupplierIssue(date,loginUser,vertical,type,proBarLoading);
    }

//    public void executeSupplierIssue(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public HashMap getTeaIssueCount(String date)  throws SQLException {
         return operationService.getTeaIssueCount(date);
    }

    public Integer getFinalPaymentCount(String date) throws SQLException {
         return operationService.getFinalPaynemtCount(date);
    }

    public void executeFinalPayment(String date, Integer loginUser, JScrollBar vertical, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeFinalPayment(date,loginUser,vertical,proBarLoading);
    }

    public HashMap getChequePrintCount(String date) throws SQLException {
         return operationService.getChequePrintCount(date);
    }

    public void executeChequePrint(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeChequePrint(date,loginUser,vertical,type,proBarLoading);
    }

    public void executeTeaIssue(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeTeaIssue(date,loginUser,vertical,type,proBarLoading);
    }

    public HashMap getBrockerCount(String date) throws SQLException {
         return operationService.getBrockerCount(date);
    }

    public void executeBrocker(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeBrocker(date,loginUser,vertical,type,proBarLoading);
        
    }

    public void executeDelete(String date, Integer loginUser, JScrollBar vertical, JProgressBar proBarLoading) throws SQLException {
        TransactionService.getInstance().executeDelete(date,loginUser,vertical,proBarLoading);
    }

    public Integer getDeleteCount(String date) throws SQLException {
        return operationService.getNotCheckDeleteCount(date);
    }


}
