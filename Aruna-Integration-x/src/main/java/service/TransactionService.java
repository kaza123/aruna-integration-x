/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import common.Constant;
import db_connections.DataSourceWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
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
import model.operation_model.SupplierIssueDetail;
import model.operation_model.SupplierIssueSummary;
import model.operation_model.TeaIssue;
import model.operation_model.TeaIssueDetail;

/**
 *
 * @author chama
 */
public class TransactionService {

    private static TransactionService instance;
    private final DataSourceWrapper operationDataSourceWrapper;
    private final DataSourceWrapper accountDataSourceWrapper;
//    private static final Logger LOGGER = Logger.getLogger(TransactionService.class);

    public TransactionService() throws SQLException {

//        
        this.operationDataSourceWrapper = ConnectionService.getInstance().getOperationDataSourceWrapper();
        this.accountDataSourceWrapper = ConnectionService.getInstance().getAccuntDataSourceWrapper();

    }

    public static TransactionService getInstance() throws SQLException {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public Integer saveGrn(Grn grn, Integer user) {

        Connection operaConnection = null;
        Connection accConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accConnection = accountDataSourceWrapper.getConnection();
            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accConnection.setAutoCommit(false);
            // Execute a query to create statment
            List<GrnDetail> grnDetail = OperationService.getGrnDetail(grn.getIndexNo(), operaConnection);
            if (grnDetail.isEmpty()) {
                throw new RuntimeException("Grn Detail was Empty !");
            }
            HashMap<Integer, Integer> supplierMap = new HashMap<>();
            TTypeIndexDetail typeIndexDetail;
            typeIndexDetail = AccountService.CheckTypeIndexDetail(Constant.SUPPLIER, grn.getSupNo(), accConnection);
            if (typeIndexDetail.getType() == null) {
                //type index detail save with supplier
                supplierMap = AccountService.saveSupplier(grn, user, accConnection);

                Integer typeIndexId = AccountService.saveTypeIndexDetail(grn.getSupNo(), Constant.SUPPLIER, supplierMap.get(1), supplierMap.get(2), accConnection);

                if (typeIndexId < 0) {
                    throw new RuntimeException("Type Index detail save fail !");
                }
                System.out.println("New Supplier ( " + grn.getSupName() + " ) Save Success !");

            } else {
                supplierMap.put(1, typeIndexDetail.getAccountRefId());
                supplierMap.put(2, typeIndexDetail.getAccountIndex());
                AccountService.updateSupplier(grn, supplierMap, accConnection);

            }
//          save grn
            Integer grnIndex = AccountService.saveGrn(grn, supplierMap, accConnection);

//          save acc ledger    
            HashMap<Integer, Object> ledgerMap = AccountService.saveAccountLedgerWithSupplierNbtVat(grn, supplierMap, grnIndex, user, accConnection);
//          save item
            HashMap<Integer, Integer> map = new HashMap<>();
            for (GrnDetail detail : grnDetail) {
//                type index check from item
                TTypeIndexDetail typeIndexDetailItem = AccountService.CheckTypeIndexDetail(Constant.ITEM, detail.getItemNo(), accConnection);
                if (typeIndexDetailItem.getType() == null) {

                    map = AccountService.saveItem(detail, accConnection);
                    //type index detail save with item
                    Integer typeIndexId = AccountService.saveTypeIndexDetail(detail.getItemNo(), Constant.ITEM, map.get(1), map.get(2), accConnection);

                    if (typeIndexId < 0) {
                        throw new RuntimeException("Type Index detail save fail !");
                    }
                    System.out.println("New Item( " + detail.getItemName() + " ) Save Success !");

                } else {
                    map.put(1, typeIndexDetailItem.getAccountRefId());
                    map.put(2, typeIndexDetailItem.getAccountIndex());

                    AccountService.updateItem(detail, map, accConnection);
                }
                detail.setGrn(grnIndex);

                String grnNo = AccountService.saveGrnDetail(detail, grn, map, user, accConnection);
                if (null == grnNo) {
                    throw new RuntimeException("Grn Number was empty or Grn save failed !");
                }

                //save acc ledger with item
                AccountService.saveAccLedgerWithItem(detail, grn.getBranch(), map, ledgerMap, user, accConnection);

            }
            Integer saveSupplierLedger = AccountService.saveSupplierLedger(grn, grnIndex, supplierMap.get(2), accConnection);
            if (saveSupplierLedger <= 0) {
                throw new RuntimeException("Supplier Ledger Save fail !");
            }

            Integer masterId = OperationService.updateGrn(grn, operaConnection);

            if (masterId > 0) {
                System.out.println(grn.getGrnNo() + " - " + grn.getFinalValue() + " - " + grn.getSupName() + " Grn Save Success !");
            } else {
                throw new RuntimeException("Grn Update fail !");
            }

            System.out.println(" ");
            //commit
            operaConnection.commit();
            accConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked success !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }

        return grn.getIndexNo();
    }

    public Integer saveInvoice(Invoice invoice, Integer user) {
        System.out.println("Invoice Processing");

        Connection operaConnection = null;
        Connection accConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accConnection.setAutoCommit(false);

//             Execute a query to create statment
            List<InvoiceDetail> invoiceDetail = OperationService.getInvoiceDetail(invoice.getIndexNo(), operaConnection);
            if (invoiceDetail.isEmpty()) {
                throw new RuntimeException("Invoice Detail was Empty !");
            }

            HashMap<Integer, Integer> customerMap = new HashMap<>();
            TTypeIndexDetail typeIndexDetail;
            typeIndexDetail = AccountService.CheckTypeIndexDetail(Constant.CUSTOMER, invoice.getClientNo(), accConnection);
            if (typeIndexDetail.getType() == null) {
                //type index detail save with customer
                customerMap = AccountService.saveCustomer(invoice, user, accConnection);

                Integer typeIndexId = AccountService.saveTypeIndexDetail(invoice.getClientNo(), Constant.CUSTOMER, customerMap.get(1), customerMap.get(2), accConnection);

                if (typeIndexId < 0) {
                    throw new RuntimeException("Type Index detail save fail !");
                }
                System.out.println("New Customer ( " + invoice.getClientName() + " ) Save Success !");

            } else {
                customerMap.put(1, typeIndexDetail.getAccountRefId());
                customerMap.put(2, typeIndexDetail.getAccountIndex());
                AccountService.updateCustomer(invoice, customerMap, accConnection);

            }
            Integer vehicleIndex = -1;
            TTypeIndexDetail typeIndexDetailVehicle;
            if (invoice.getVehicleNo() != null) {

                typeIndexDetailVehicle = AccountService.CheckTypeIndexDetail(Constant.VEHICLE, invoice.getVehicleNo(), accConnection);
                if (typeIndexDetailVehicle.getType() == null) {
                    //type index detail save with customer
                    vehicleIndex = AccountService.saveVehicle(invoice, customerMap.get(2), accConnection);
                    if (vehicleIndex <= 0) {
                        throw new RuntimeException("New Vehicle save Fail !");
                    }

                    Integer typeIndexId = AccountService.saveTypeIndexDetail(invoice.getVehicleNo(), Constant.VEHICLE, 0, vehicleIndex, accConnection);

                    if (typeIndexId < 0) {
                        throw new RuntimeException("Type Index detail save fail !");
                    }
                    System.out.println("New Vehicle ( " + invoice.getVehicleNo() + " ) Save Success !");

                } else {
                    vehicleIndex = typeIndexDetailVehicle.getAccountIndex();
                }
            } else {
                vehicleIndex = null;
            }
//          save invoice
            HashMap<Integer, Integer> invoiceMap = AccountService.saveInvoice(invoice, invoiceDetail, customerMap, vehicleIndex, user, accConnection);
//
//          save item
            HashMap<Integer, Integer> itemMap = new HashMap<>();
            for (InvoiceDetail detail : invoiceDetail) {
////                type index check from item
                TTypeIndexDetail typeIndexDetailItem = AccountService.CheckTypeIndexDetail(Constant.ITEM, detail.getItemNo(), accConnection);
                if (typeIndexDetailItem.getType() == null) {
                    if (!detail.getItemType().equals(Constant.ITEM_SERVICE)) {
                        throw new RuntimeException("Can't find Item (" + detail.getItemName() + ") from account system !");
                    } else {
                        itemMap = AccountService.saveItem(detail, accConnection);
                        //type index detail save with item
                        Integer typeIndexId = AccountService.saveTypeIndexDetail(detail.getItemNo(), Constant.ITEM, itemMap.get(1), itemMap.get(2), accConnection);

                        if (typeIndexId < 0) {
                            throw new RuntimeException("Type Index detail save fail !");
                        }
                        System.out.println("New Item( " + detail.getItemName() + " ) Save Success !");

                    }
//
                } else {
                    itemMap.put(1, typeIndexDetailItem.getAccountRefId());
                    itemMap.put(2, typeIndexDetailItem.getAccountIndex());
                    if (!detail.getIsZeroItem()) {
                        AccountService.updateItemFromInvoice(detail, itemMap, accConnection);
                    }
                }
                detail.setInvoice(invoiceMap.get(1));
                String invoiceNo = AccountService.saveInvoiceDetail(detail, invoice, itemMap, invoiceMap, accConnection);
                if (null == invoiceNo) {
                    throw new RuntimeException("Invoice save failed !");
                }

            }
//            Integer saveSupplierLedger = AccountService.saveSupplierLedger(grn, grnIndex, supplierMap.get(2), accConnection);
//            if (saveSupplierLedger <= 0) {
//                throw new RuntimeException("Supplier Ledger Save fail !");
//            }
            Integer typeIndexId = AccountService.saveTypeIndexDetail(invoice.getInvoiceNo(), Constant.INVOICE, invoiceMap.get(1), invoiceMap.get(2), accConnection);
            if (typeIndexId <= 0) {
                throw new RuntimeException("Type index detail Invoice save fail !");
            }
            Integer masterId = OperationService.updateInvoice(invoice, operaConnection);
//
            if (masterId > 0) {
                System.out.println(invoice.getInvoiceNo() + " - " + invoice.getNetAmount() + " - " + invoice.getClientName() + " Invoice Save Success !");
            } else {
                throw new RuntimeException("Invoice Update fail !");
            }

            System.out.println(" ");
            //commit
            operaConnection.commit();
            accConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }

        return invoice.getIndexNo();
    }

    public Integer savePayment(Payment payment, Integer user) {
        Connection operaConnection = null;
        Connection accConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accConnection.setAutoCommit(false);

            //Execute a query to create statment
            TTypeIndexDetail customerTypeIndexDetail;
            customerTypeIndexDetail = AccountService.CheckTypeIndexDetail(payment.getClientType(), payment.getClientNo(), accConnection);
            HashMap<Integer, Integer> customerMap = new HashMap<>();
            if (customerTypeIndexDetail.getType() == null) {
//                throw new RuntimeException("Customer Not found !");
                // save change customer
                customerMap = AccountService.saveCustomer(payment, user, accConnection);

                Integer typeIndexId = AccountService.saveTypeIndexDetail(payment.getClientNo(), payment.getClientType(), customerMap.get(1), customerMap.get(2), accConnection);

                if (typeIndexId < 0) {
                    throw new RuntimeException("Type Index detail save fail !");
                }
                System.out.println("New "+payment.getClientType()+" ( " + payment.getClientNo() + "-" + payment.getClientName() + " ) Save Success !");
                customerTypeIndexDetail = AccountService.CheckTypeIndexDetail(payment.getClientType(), payment.getClientNo(), accConnection);
            }
            Integer paymentIndex = AccountService.savePayment(payment, accConnection);
            if (paymentIndex <= 0) {
                throw new RuntimeException("Payment Save Fail !");
            }

            List<PaymentDetail> paymentDetail = OperationService.getPaymentDetail(payment.getIndexNo(), operaConnection);
            if (paymentDetail.isEmpty()) {
                throw new RuntimeException("Payment Detail was Empty !");
            }
            HashMap<Integer, Object> numberMap = AccountService.getAccLedgerNumber(payment.getBranch(), accConnection);

            for (PaymentDetail paymentDetail1 : paymentDetail) {
                String invCustomerNo = OperationService.getCustomerNoByInvoice(paymentDetail1.getInvoice(),payment.getClientType(), operaConnection);
                if (!invCustomerNo.equals(payment.getClientNo())) {
                    System.out.println("change customer form " + invCustomerNo + " to " + payment.getClientNo());
                    TTypeIndexDetail typeDetail = AccountService.CheckTypeIndexDetail(payment.getClientType(), paymentDetail1.getInvoice(), accConnection);
                    Integer save = AccountService.tAccLedgerByCustomer(typeDetail, customerTypeIndexDetail.getType() == null ? customerMap.get(1) : customerTypeIndexDetail.getAccountRefId(), accConnection);
                    if (save <= 0) {
                        throw new RuntimeException("tAccLedger update by customer is fail !");
                    }
                }
                AccountService.saveCustomerLedger(paymentDetail1, paymentIndex, payment, customerTypeIndexDetail, numberMap, user, accConnection);
            }

            List<PaymentInformation> paymentInformationList = OperationService.getPaymentInformations(payment.getIndexNo(), operaConnection);
            if (paymentInformationList.isEmpty()) {
                throw new RuntimeException("Payment Informations was Empty !");
            }
            for (PaymentInformation paymentInformation : paymentInformationList) {
                AccountService.savePaymentInformation(paymentInformation, paymentIndex, payment, customerTypeIndexDetail, numberMap, user, accConnection);
            }

            Integer masterId = OperationService.updatePayment(payment.getIndexNo(), operaConnection);

            if (masterId > 0) {
                System.out.println(payment.getNumber() + " - " + payment.getTotalAmount() + " Payment Save Success !");
            } else {
                throw new RuntimeException("Payment Update fail !");
            }

            System.out.println(" ");
            //commit
            operaConnection.commit();
            accConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }

        return 1;
    }

    public Integer checkLoginUser(String name, String pswd) {
        Connection accConnection = null;
        Integer loginUser = -1;
        try {
            //Open a connection
            accConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            accConnection.setAutoCommit(false);
            // Execute a query to create statment
            loginUser = AccountService.checkLoginUser(name, pswd, accConnection);

            //commit
            accConnection.commit();

            //Clean-up environment
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked success !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
        return loginUser;

    }

    public void saveStockAdjustment(StockAdjustment adjustment, Integer user) {
        Connection operaConnection = null;
        Connection accConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accConnection.setAutoCommit(false);

//             Execute a query to create statment
            System.out.println(" start");
            int saveStockAdjustment = AccountService.saveStockAdjustment(adjustment, accConnection);
            System.out.println(saveStockAdjustment + " 1");

            Integer saveIndex = AccountService.saveStockAdjustmentToLedger(adjustment, user, saveStockAdjustment, accConnection, operaConnection);
            System.out.println(saveIndex + " 2");
            if (saveIndex <= 0) {
                throw new RuntimeException("Stock Ledger Save Fail !");
            }

            Integer masterId = OperationService.updateAdjustment(adjustment.getIndexNo(), operaConnection);
            if (masterId <= 0) {
                throw new RuntimeException("Stock Adjustment Status update fail !");
            }
            System.out.println("Stock Adjustment Save Success ! ");
            System.out.println(" ");
            //commit
            operaConnection.commit();
            accConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public String getCompanyName() {
//        Connection operaConnection = null;
        Connection accConnection = null;
        String companyName = "";
        try {
            accConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            accConnection.setAutoCommit(false);
            companyName = AccountService.getCompanyName(accConnection);

            //commit
            accConnection.commit();

            //Clean-up environment
            accConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);

                if (accConnection != null) {
                    accConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
        return companyName;
    }

    public void executeSupplierAdvance(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            String advanceType = null;
            switch (type) {
                case Constant.SUPPLIER_ADVANCE:
                    advanceType = Constant.SUPPLIER_ADVANCE;
                    break;
                case Constant.SUPPLIER_LOAN:
                    advanceType = Constant.SUPPLIER_LOAN;
                    break;
                case Constant.SUPPLIER_SETTLEMENT:
                    advanceType = Constant.SUPPLIER_SETTLEMENT;
                    break;
                case Constant.EMPLOYEE_ADVANCE:
                    advanceType = Constant.EMPLOYEE_ADVANCE;
                    break;
                case Constant.EMPLOYEE_LOAN:
                    advanceType = Constant.EMPLOYEE_LOAN;
                    break;
                case Constant.EMPLOYEE_SETTLEMENT:
                    advanceType = Constant.EMPLOYEE_SETTLEMENT;
                    break;
                default:
                    throw new RuntimeException("main type doesn't match ");
            }

            int advanceListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);

            List<Advance> advanceList = OperationService.getAdvanceByType(date, advanceType, operaConnection);
            advanceListSize = advanceList.size();
            if (advanceListSize == 0) {
                throw new RuntimeException("Integration Supplier Advance is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (Advance advance : advanceList) {
                List<AdvanceDetail> advanceDetailList = OperationService.getAdvanceDetail(advance.getIndexNo(), operaConnection);
                if (advanceDetailList.isEmpty()) {
                    throw new RuntimeException("Empty Integration Supplier Advance Details !");
                }
                Integer save = AccountService.executeAdvance(advance, advanceDetailList, date, loginUser, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / advanceListSize * updatedSize + ""));
                    Integer update = OperationService.updateAdvanceStatus(advance.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }
                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == advanceListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println(advanceType + " Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == advanceListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public void executeSupplierIssue(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            String issueType = null;
            switch (type) {
                case Constant.SUPPLIER_FERTILIZER:
                    issueType = Constant.SUPPLIER_FERTILIZER;
                    break;
                case Constant.SUPPLIER_OTHER:
                    issueType = Constant.SUPPLIER_OTHER;
                    break;
                case Constant.EMPLOYEE_FERTILIZER:
                    issueType = Constant.EMPLOYEE_FERTILIZER;
                    break;
                case Constant.EMPLOYEE_OTHER:
                    issueType = Constant.EMPLOYEE_OTHER;
                    break;
                default:
                    throw new RuntimeException("main type doesn't match ");
            }
            int issueListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);

            List<SupplierIssueSummary> issueList = OperationService.getIssueByType(date, issueType, operaConnection);
            issueListSize = issueList.size();
            if (issueListSize == 0) {
                throw new RuntimeException("Integration Supplier Issue is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (SupplierIssueSummary summary : issueList) {
                List<SupplierIssueDetail> issueDetailList = OperationService.getIssueDetail(summary.getIndexNo(), operaConnection);
                if (issueDetailList.isEmpty()) {
                    throw new RuntimeException("Empty Integration Supplier issue Details !");
                }
                Integer save = AccountService.executeIssue(summary, issueDetailList, date, loginUser, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / issueListSize * updatedSize + ""));
                    Integer update = OperationService.updateIssueStatus(summary.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }

                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println(issueType + " Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public void executeFinalPayment(String date, Integer loginUser, JScrollBar vertical, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            int issueListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);

            List<FinalPayment> data = OperationService.getFinalPayment(date, operaConnection);
            issueListSize = data.size();
            if (issueListSize == 0) {
                throw new RuntimeException("Integration Data is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (FinalPayment summary : data) {
                List<FinalPaymentDetail> detailList = OperationService.getFinalPaymentDetail(summary.getIndexNo(), operaConnection);
                if (detailList.isEmpty()) {
                    throw new RuntimeException("Empty Integration Details !");
                }
                Integer save = AccountService.executeFinalPayment(summary, detailList, date, loginUser, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / issueListSize * updatedSize + ""));
                    Integer update = OperationService.updateFinalPayment(summary.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }

                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println("Final Payment Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public void executeChequePrint(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            int issueListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);

            List<ChequePrint> data = OperationService.getChequePrint(date, type, operaConnection);
            issueListSize = data.size();
            if (issueListSize == 0) {
                throw new RuntimeException("Integration Data is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (ChequePrint summary : data) {
                List<ChequePrintDetail> detailList = OperationService.getChequePrintDetail(summary.getIndexNo(), operaConnection);
                if (detailList.isEmpty()) {
                    throw new RuntimeException("Empty Integration Details !");
                }
                Integer save = AccountService.executeChequePayment(summary, detailList, date, loginUser, type, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / issueListSize * updatedSize + ""));
                    Integer update = OperationService.updateChequePrint(summary.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }

                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println("Final Payment Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public void executeTeaIssue(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            int issueListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);

            List<TeaIssue> data = OperationService.getTeaIssue(date, type, operaConnection);
            issueListSize = data.size();
            if (issueListSize == 0) {
                throw new RuntimeException("Integration Data is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (TeaIssue summary : data) {
                List<TeaIssueDetail> detailList = OperationService.getTeaIssueDetail(summary.getIndexNo(), operaConnection);
                if (detailList.isEmpty()) {
                    throw new RuntimeException("Empty Integration Details !");
                }
                Integer save = AccountService.executeTeaIssue(summary, detailList, date, loginUser, type, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / issueListSize * updatedSize + ""));
                    Integer update = OperationService.updateTeaIssue(summary.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }

                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println("Final Payment Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public void executeBrocker(String date, Integer loginUser, JScrollBar vertical, String type, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            int issueListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);
            System.out.println(date+" - "+type);
            List<Brocker> data = OperationService.getBrocker(date, type, operaConnection);
            issueListSize = data.size();
            if (issueListSize == 0) {
                throw new RuntimeException("Integration Data is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (Brocker summary : data) {
               
                Integer save = AccountService.executeBrocker(summary, date, loginUser, type, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / issueListSize * updatedSize + ""));
                    Integer update = OperationService.updateBrocker(summary.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }

                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println("Final Payment Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == issueListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

    public void executeDelete(String date, Integer loginUser, JScrollBar vertical, JProgressBar proBarLoading) {
        Connection operaConnection = null;
        Connection accountConnection = null;
        try {
            //Open a connection
            operaConnection = operationDataSourceWrapper.getConnection();
            accountConnection = accountDataSourceWrapper.getConnection();

            //Set auto commit as false.
            operaConnection.setAutoCommit(false);
            accountConnection.setAutoCommit(false);

//             Execute a query to create statment
            int deleteListSize = 0;
            int updatedSize = 0;

            operaConnection.setAutoCommit(false);
            List<Delete> data = OperationService.getDeleteData(date, operaConnection);
            deleteListSize = data.size();
            if (deleteListSize == 0) {
                throw new RuntimeException("Integration Data is Empty !");
            }
            accountConnection.setAutoCommit(false);
            for (Delete delete : data) {
               
                Integer save = AccountService.executeDelete(delete, date, loginUser, accountConnection);
                if (save > 0) {
                    updatedSize++;
                    proBarLoading.setValue(Integer.parseInt(100 / deleteListSize * updatedSize + ""));
                    Integer update = OperationService.updateDelete(delete.getIndexNo(), operaConnection);
                    if (update > 0) {
                        //commit
                        operaConnection.commit();
                        accountConnection.commit();
                    }

                } else {
                    operaConnection.rollback();
                    accountConnection.rollback();
                }
                vertical.setValue(vertical.getMaximum());
            }
            if (updatedSize == deleteListSize) {
                proBarLoading.setValue(100);
            }

            System.out.println("Final Payment Save Completed ! ");
            System.out.println(" ");
            if (updatedSize == deleteListSize) {
                proBarLoading.setValue(100);
            }
            //commit
            operaConnection.commit();
            accountConnection.commit();

            //Clean-up environment
            operaConnection.close();
            accountConnection.close();

        } catch (Exception e) {
            try {
                System.out.println("COMPILE ERROR ! , check the data and try again !");
                System.out.println(e);
                if (operaConnection != null) {
                    operaConnection.rollback();
                }
                if (accountConnection != null) {
                    accountConnection.rollback();
                }
                System.out.println("Transactions Rollbacked !");
            } catch (SQLException se2) {
                System.out.println("Can't find database Connections !");

            }
        }
    }

}
