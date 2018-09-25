/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import common.Constant;
import java.awt.Color;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.SwingWorker;
import service.TransactionService;
import sync_service.SyncService;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SystemIntegrationSyncGUI extends javax.swing.JFrame {

    Integer loginUser = -1;
    JScrollBar vertical;
    private Integer view = 1;

    public SystemIntegrationSyncGUI() {
        initComponents();
        ImageIcon imageIcon = new ImageIcon("./images/task.png");
        setIconImage(imageIcon.getImage());
        vertical = scrollPane.getVerticalScrollBar();

        viewSubCategory(view);

        initOthers();
        lblProcess.setText("");

        try {
            getDetailCount(lblDate.getText());
        } catch (SQLException ex) {
            System.out.println("get detail count function not support !");
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeGrn(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executeGrn(date, loginUser1, vertical, proBarLoading);
        getDetailCount(date);
    }

    private void executeInvoice(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executeInvoice(date, loginUser1, vertical, proBarLoading);
        getDetailCount(date);
    }

    private void executePayment(String date, Integer loginUser1) throws SQLException {
        SyncService.getInstance().executePayment(date, loginUser1, vertical, proBarLoading);
        getDetailCount(date);
    }

    private void executeStockAdjustment(String date, Integer loginUser) throws SQLException {
        SyncService.getInstance().executeStockAdjustment(date, loginUser, vertical, proBarLoading);
        getDetailCount(date);
    }

    private void executeSupplierAdvance(String date, Integer loginUser, String type) throws SQLException {
        SyncService.getInstance().executeSupplierAdvance(date, loginUser, vertical, type, proBarLoading);
        getDetailCount(date);
    }

    private void executeSupplierIssue(String date, Integer loginUser, String type) throws SQLException {
        SyncService.getInstance().executeSupplierIssue(date, loginUser, vertical, type, proBarLoading);
        getDetailCount(date);
    }

    private void executeFinalPayment(String date, Integer loginUser) throws SQLException {
        SyncService.getInstance().executeFinalPayment(date, loginUser, vertical, proBarLoading);
        getDetailCount(date);
    }

    private void executeChequePrint(String date, Integer loginUser, String type) throws SQLException {
        SyncService.getInstance().executeChequePrint(date, loginUser, vertical, type, proBarLoading);
        getDetailCount(date);
    }

    private void executeTeaIssue(String date, Integer loginUser, String type) throws SQLException {
        SyncService.getInstance().executeTeaIssue(date, loginUser, vertical, type, proBarLoading);
        getDetailCount(date);
    }

    private void executeBocker(String date, Integer loginUser, String type) throws SQLException {
        SyncService.getInstance().executeBrocker(date, loginUser, vertical, type, proBarLoading);
        getDetailCount(date);
    }

    private void executeDelete(String date, Integer loginUser) throws SQLException {
        SyncService.getInstance().executeDelete(date, loginUser, vertical, proBarLoading);
        getDetailCount(date);
        
    }

    private void getDetailCount(String date) throws SQLException {

        HashMap hashMap = SyncService.getInstance().getAdvanceCount(date);
        lblSupplierAdvance.setText("Supplier Advance - " + (hashMap.get(Constant.SUPPLIER_ADVANCE) == null ? 0 : hashMap.get(Constant.SUPPLIER_ADVANCE)));
        lblSupplierLoan.setText("Supplier Loan - " + (hashMap.get(Constant.SUPPLIER_LOAN) == null ? 0 : hashMap.get(Constant.SUPPLIER_LOAN)));
        lblSupplierSettlement.setText("Supplier Settlement - " + (hashMap.get(Constant.SUPPLIER_SETTLEMENT) == null ? 0 : hashMap.get(Constant.SUPPLIER_SETTLEMENT)));

        lblEmployeeAdvance.setText("Employee Advance - " + (hashMap.get(Constant.EMPLOYEE_ADVANCE) == null ? 0 : hashMap.get(Constant.EMPLOYEE_ADVANCE)));
        lblEmployeeLoan.setText("Employee Loan - " + (hashMap.get(Constant.EMPLOYEE_LOAN) == null ? 0 : hashMap.get(Constant.EMPLOYEE_LOAN)));
        lblEmployeeSettlement.setText("Employee Settlement - " + (hashMap.get(Constant.EMPLOYEE_SETTLEMENT) == null ? 0 : hashMap.get(Constant.EMPLOYEE_SETTLEMENT)));

        HashMap traCount = SyncService.getInstance().getSupplierIssueCount(date);
        lblSupplierFertilizer.setText("Supplier Fertilizer - " + (traCount.get(Constant.SUPPLIER_FERTILIZER) == null ? 0 : traCount.get(Constant.SUPPLIER_FERTILIZER)));
        lblSupplierOther.setText("Supplier Other - " + (traCount.get(Constant.SUPPLIER_OTHER) == null ? 0 : traCount.get(Constant.SUPPLIER_OTHER)));

        lblEmployeeFertilizer.setText("Employee Fertilizer - " + (traCount.get(Constant.EMPLOYEE_FERTILIZER) == null ? 0 : traCount.get(Constant.EMPLOYEE_FERTILIZER)));
        lblEmployeeOther.setText("Employee Other - " + (traCount.get(Constant.EMPLOYEE_OTHER) == null ? 0 : traCount.get(Constant.EMPLOYEE_OTHER)));

        HashMap teaMap = SyncService.getInstance().getTeaIssueCount(date);
        lblSupplierTea.setText("Supplier Tea - " + (teaMap.get(Constant.SUPPLIER_TEA) == null ? 0 : teaMap.get(Constant.SUPPLIER_TEA)));
        lblEmployeeTea.setText("Employee Tea - " + (teaMap.get(Constant.EMPLOYEE_TEA) == null ? 0 : teaMap.get(Constant.EMPLOYEE_TEA)));

        HashMap chqPrtMap = SyncService.getInstance().getChequePrintCount(date);
        lblChequePrint.setText("Cheque Print - " + (chqPrtMap.get(Constant.CHEQUE_PRINT) == null ? 0 : chqPrtMap.get(Constant.CHEQUE_PRINT)));
        lblBankDeposit.setText("Bank Deposit - " + (chqPrtMap.get(Constant.BANK_DEPOSIT) == null ? 0 : chqPrtMap.get(Constant.BANK_DEPOSIT)));

        HashMap brockerMap = SyncService.getInstance().getBrockerCount(date);
        lblBrocker.setText("Broker - " + (brockerMap.get(Constant.BROKER) == null ? 0 : brockerMap.get(Constant.BROKER)));
        lblLocal.setText("Local - " + (brockerMap.get(Constant.LOCAL) == null ? 0 : brockerMap.get(Constant.LOCAL)));

        Integer stockAdjustmentCount = SyncService.getInstance().getStockAdjustmentCount(date);
        lblStockAdjust.setText("Stock Adjustment" + " - " + stockAdjustmentCount);

        Integer grnCount = SyncService.getInstance().getGrnCount(date);
        lblGrn.setText("GRN" + " - " + grnCount);

        Integer fPCount = SyncService.getInstance().getFinalPaymentCount(date);
        lblFinalPayment.setText("Final Payment - " + fPCount);

//        Integer invoiceCount = SyncService.getInstance().getInvoiceCount(date);
//        lblBrocker.setText("Invoice" + " - " + invoiceCount);
        Integer paymentCount = SyncService.getInstance().getPaymentCount(date);
        lblPayment.setText("Payment" + " - " + paymentCount);
        
        Integer no = SyncService.getInstance().getDeleteCount(date);
        lblDelete.setText("Delete - " + no);

    }

    @SuppressWarnings("unchecked")
    private void initOthers() {
        try {
            setTitle("Account Integration System");
            setLocationRelativeTo(null);
            txtLog.setEditable(false);
            String date = SyncService.getInstance().getTransactionDate();
            lblDate.setText(date);

            //set company
            String companyName = TransactionService.getInstance().getCompanyName();
            lblCompanyName.setText(companyName);

            TextAreaOutputStream textAreaOutputStream = new TextAreaOutputStream(txtLog);
            System.setOut(new PrintStream(textAreaOutputStream));
        } catch (SQLException ex) {
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLoginUser(Integer loginUser) {
        this.loginUser = loginUser;
        System.out.println("login User - " + loginUser);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSubCategory = new javax.swing.JPanel();
        lblGrn = new javax.swing.JLabel();
        lblStockAdjust = new javax.swing.JLabel();
        lblBrocker = new javax.swing.JLabel();
        lblPayment = new javax.swing.JLabel();
        lblSupplierAdvance = new javax.swing.JLabel();
        lblSupplierLoan = new javax.swing.JLabel();
        lblSupplierSettlement = new javax.swing.JLabel();
        lblSupplierTea = new javax.swing.JLabel();
        lblSupplierFertilizer = new javax.swing.JLabel();
        lblSupplierOther = new javax.swing.JLabel();
        lblFinalPayment = new javax.swing.JLabel();
        lblEmployeeLoan = new javax.swing.JLabel();
        lblEmployeeSettlement = new javax.swing.JLabel();
        lblEmployeeAdvance = new javax.swing.JLabel();
        lblEmployeeTea = new javax.swing.JLabel();
        lblEmployeeFertilizer = new javax.swing.JLabel();
        lblEmployeeOther = new javax.swing.JLabel();
        lblChequePrint = new javax.swing.JLabel();
        lblBankDeposit = new javax.swing.JLabel();
        lblLocal = new javax.swing.JLabel();
        lblDelete = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        lblProcess = new javax.swing.JLabel();
        lblCompanyName = new javax.swing.JLabel();
        lblClear = new javax.swing.JLabel();
        lblClose = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblClose1 = new javax.swing.JLabel();
        proBarLoading = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblCatMain = new javax.swing.JLabel();
        lblCatSupplier = new javax.swing.JLabel();
        lblCatEmployee = new javax.swing.JLabel();
        lblCatFinalPayment = new javax.swing.JLabel();
        lblCeperator = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(550, 330));
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlSubCategory.setBackground(new java.awt.Color(255, 255, 255));

        lblGrn.setBackground(new java.awt.Color(141, 152, 222));
        lblGrn.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblGrn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrn.setText("GRN");
        lblGrn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblGrn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblGrn.setMaximumSize(new java.awt.Dimension(168, 20));
        lblGrn.setMinimumSize(new java.awt.Dimension(168, 20));
        lblGrn.setOpaque(true);
        lblGrn.setPreferredSize(new java.awt.Dimension(168, 30));
        lblGrn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGrnMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblGrn);

        lblStockAdjust.setBackground(new java.awt.Color(141, 152, 222));
        lblStockAdjust.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblStockAdjust.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStockAdjust.setText("Stock Adjustment");
        lblStockAdjust.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblStockAdjust.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblStockAdjust.setMinimumSize(new java.awt.Dimension(168, 20));
        lblStockAdjust.setOpaque(true);
        lblStockAdjust.setPreferredSize(new java.awt.Dimension(168, 30));
        lblStockAdjust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblStockAdjustMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblStockAdjust);

        lblBrocker.setBackground(new java.awt.Color(141, 152, 222));
        lblBrocker.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblBrocker.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBrocker.setText("Brocker");
        lblBrocker.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBrocker.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblBrocker.setMinimumSize(new java.awt.Dimension(168, 20));
        lblBrocker.setOpaque(true);
        lblBrocker.setPreferredSize(new java.awt.Dimension(168, 30));
        lblBrocker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBrockerMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblBrocker);

        lblPayment.setBackground(new java.awt.Color(141, 152, 222));
        lblPayment.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblPayment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPayment.setText("Payment");
        lblPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPayment.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPayment.setMinimumSize(new java.awt.Dimension(168, 20));
        lblPayment.setOpaque(true);
        lblPayment.setPreferredSize(new java.awt.Dimension(168, 30));
        lblPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPaymentMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblPayment);

        lblSupplierAdvance.setBackground(new java.awt.Color(141, 192, 222));
        lblSupplierAdvance.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblSupplierAdvance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierAdvance.setText("Supplier Advance");
        lblSupplierAdvance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSupplierAdvance.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSupplierAdvance.setMinimumSize(new java.awt.Dimension(168, 20));
        lblSupplierAdvance.setOpaque(true);
        lblSupplierAdvance.setPreferredSize(new java.awt.Dimension(168, 30));
        lblSupplierAdvance.setRequestFocusEnabled(false);
        lblSupplierAdvance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierAdvanceMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblSupplierAdvance);

        lblSupplierLoan.setBackground(new java.awt.Color(141, 192, 222));
        lblSupplierLoan.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblSupplierLoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierLoan.setText("Supplier Loan");
        lblSupplierLoan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSupplierLoan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSupplierLoan.setMinimumSize(new java.awt.Dimension(168, 20));
        lblSupplierLoan.setOpaque(true);
        lblSupplierLoan.setPreferredSize(new java.awt.Dimension(168, 30));
        lblSupplierLoan.setRequestFocusEnabled(false);
        lblSupplierLoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierLoanMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblSupplierLoan);

        lblSupplierSettlement.setBackground(new java.awt.Color(141, 192, 222));
        lblSupplierSettlement.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblSupplierSettlement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierSettlement.setText("Supplier Settlement");
        lblSupplierSettlement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSupplierSettlement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSupplierSettlement.setMinimumSize(new java.awt.Dimension(168, 20));
        lblSupplierSettlement.setOpaque(true);
        lblSupplierSettlement.setPreferredSize(new java.awt.Dimension(168, 30));
        lblSupplierSettlement.setRequestFocusEnabled(false);
        lblSupplierSettlement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierSettlementMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblSupplierSettlement);

        lblSupplierTea.setBackground(new java.awt.Color(141, 192, 222));
        lblSupplierTea.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblSupplierTea.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierTea.setText("Supplier Tea");
        lblSupplierTea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSupplierTea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSupplierTea.setMinimumSize(new java.awt.Dimension(168, 20));
        lblSupplierTea.setOpaque(true);
        lblSupplierTea.setPreferredSize(new java.awt.Dimension(168, 30));
        lblSupplierTea.setRequestFocusEnabled(false);
        lblSupplierTea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierTeaMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblSupplierTea);

        lblSupplierFertilizer.setBackground(new java.awt.Color(141, 192, 222));
        lblSupplierFertilizer.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblSupplierFertilizer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierFertilizer.setText("Supplier Fertilizer");
        lblSupplierFertilizer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSupplierFertilizer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSupplierFertilizer.setMinimumSize(new java.awt.Dimension(168, 20));
        lblSupplierFertilizer.setOpaque(true);
        lblSupplierFertilizer.setPreferredSize(new java.awt.Dimension(168, 30));
        lblSupplierFertilizer.setRequestFocusEnabled(false);
        lblSupplierFertilizer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierFertilizerMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblSupplierFertilizer);

        lblSupplierOther.setBackground(new java.awt.Color(141, 192, 222));
        lblSupplierOther.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblSupplierOther.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierOther.setText("Supplier Other");
        lblSupplierOther.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSupplierOther.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSupplierOther.setMinimumSize(new java.awt.Dimension(168, 20));
        lblSupplierOther.setOpaque(true);
        lblSupplierOther.setPreferredSize(new java.awt.Dimension(168, 30));
        lblSupplierOther.setRequestFocusEnabled(false);
        lblSupplierOther.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierOtherMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblSupplierOther);

        lblFinalPayment.setBackground(new java.awt.Color(201, 222, 222));
        lblFinalPayment.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblFinalPayment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFinalPayment.setText("Final Payment");
        lblFinalPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFinalPayment.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFinalPayment.setOpaque(true);
        lblFinalPayment.setPreferredSize(new java.awt.Dimension(168, 30));
        lblFinalPayment.setRequestFocusEnabled(false);
        lblFinalPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFinalPaymentMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblFinalPayment);

        lblEmployeeLoan.setBackground(new java.awt.Color(201, 192, 222));
        lblEmployeeLoan.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblEmployeeLoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeLoan.setText("Employee Loan");
        lblEmployeeLoan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEmployeeLoan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeLoan.setMinimumSize(new java.awt.Dimension(168, 20));
        lblEmployeeLoan.setOpaque(true);
        lblEmployeeLoan.setPreferredSize(new java.awt.Dimension(168, 30));
        lblEmployeeLoan.setRequestFocusEnabled(false);
        lblEmployeeLoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeLoanMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblEmployeeLoan);

        lblEmployeeSettlement.setBackground(new java.awt.Color(201, 192, 222));
        lblEmployeeSettlement.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblEmployeeSettlement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeSettlement.setText("Employee Settlement");
        lblEmployeeSettlement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEmployeeSettlement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeSettlement.setMinimumSize(new java.awt.Dimension(168, 20));
        lblEmployeeSettlement.setOpaque(true);
        lblEmployeeSettlement.setPreferredSize(new java.awt.Dimension(168, 30));
        lblEmployeeSettlement.setRequestFocusEnabled(false);
        lblEmployeeSettlement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeSettlementMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblEmployeeSettlement);

        lblEmployeeAdvance.setBackground(new java.awt.Color(201, 192, 222));
        lblEmployeeAdvance.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblEmployeeAdvance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeAdvance.setText("Employee Advance");
        lblEmployeeAdvance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEmployeeAdvance.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeAdvance.setMinimumSize(new java.awt.Dimension(168, 20));
        lblEmployeeAdvance.setOpaque(true);
        lblEmployeeAdvance.setPreferredSize(new java.awt.Dimension(168, 30));
        lblEmployeeAdvance.setRequestFocusEnabled(false);
        lblEmployeeAdvance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeAdvanceMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblEmployeeAdvance);

        lblEmployeeTea.setBackground(new java.awt.Color(201, 192, 222));
        lblEmployeeTea.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblEmployeeTea.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeTea.setText("Employee Tea");
        lblEmployeeTea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEmployeeTea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeTea.setMinimumSize(new java.awt.Dimension(168, 20));
        lblEmployeeTea.setOpaque(true);
        lblEmployeeTea.setPreferredSize(new java.awt.Dimension(168, 30));
        lblEmployeeTea.setRequestFocusEnabled(false);
        lblEmployeeTea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeTeaMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblEmployeeTea);

        lblEmployeeFertilizer.setBackground(new java.awt.Color(201, 192, 222));
        lblEmployeeFertilizer.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblEmployeeFertilizer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeFertilizer.setText("Employee Fertilizer");
        lblEmployeeFertilizer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEmployeeFertilizer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeFertilizer.setMinimumSize(new java.awt.Dimension(168, 20));
        lblEmployeeFertilizer.setOpaque(true);
        lblEmployeeFertilizer.setPreferredSize(new java.awt.Dimension(168, 30));
        lblEmployeeFertilizer.setRequestFocusEnabled(false);
        lblEmployeeFertilizer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeFertilizerMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblEmployeeFertilizer);

        lblEmployeeOther.setBackground(new java.awt.Color(201, 192, 222));
        lblEmployeeOther.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblEmployeeOther.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeOther.setText("Employee Other");
        lblEmployeeOther.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEmployeeOther.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeOther.setMinimumSize(new java.awt.Dimension(168, 20));
        lblEmployeeOther.setOpaque(true);
        lblEmployeeOther.setPreferredSize(new java.awt.Dimension(168, 30));
        lblEmployeeOther.setRequestFocusEnabled(false);
        lblEmployeeOther.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeOtherMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblEmployeeOther);

        lblChequePrint.setBackground(new java.awt.Color(201, 222, 222));
        lblChequePrint.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblChequePrint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChequePrint.setText("Cheque Print");
        lblChequePrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblChequePrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblChequePrint.setMinimumSize(new java.awt.Dimension(168, 20));
        lblChequePrint.setOpaque(true);
        lblChequePrint.setPreferredSize(new java.awt.Dimension(168, 30));
        lblChequePrint.setRequestFocusEnabled(false);
        lblChequePrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblChequePrintMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblChequePrint);

        lblBankDeposit.setBackground(new java.awt.Color(201, 222, 222));
        lblBankDeposit.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblBankDeposit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBankDeposit.setText("Bank Deposit");
        lblBankDeposit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBankDeposit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblBankDeposit.setMinimumSize(new java.awt.Dimension(168, 20));
        lblBankDeposit.setOpaque(true);
        lblBankDeposit.setPreferredSize(new java.awt.Dimension(168, 30));
        lblBankDeposit.setRequestFocusEnabled(false);
        lblBankDeposit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBankDepositMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblBankDeposit);

        lblLocal.setBackground(new java.awt.Color(141, 152, 222));
        lblLocal.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLocal.setText("Local");
        lblLocal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblLocal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblLocal.setMinimumSize(new java.awt.Dimension(168, 20));
        lblLocal.setOpaque(true);
        lblLocal.setPreferredSize(new java.awt.Dimension(168, 30));
        lblLocal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLocalMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblLocal);

        lblDelete.setBackground(new java.awt.Color(141, 152, 222));
        lblDelete.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDelete.setText("Delete");
        lblDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblDelete.setMinimumSize(new java.awt.Dimension(168, 20));
        lblDelete.setOpaque(true);
        lblDelete.setPreferredSize(new java.awt.Dimension(168, 30));
        lblDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDeleteMouseClicked(evt);
            }
        });
        pnlSubCategory.add(lblDelete);

        getContentPane().add(pnlSubCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 208, 190, 340));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtLog.setColumns(20);
        txtLog.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        txtLog.setRows(5);
        scrollPane.setViewportView(txtLog);

        lblProcess.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        lblProcess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProcess.setText("Processing . . .");

        lblCompanyName.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompanyName.setText("Company name goes here");

        lblClear.setBackground(new java.awt.Color(91, 192, 222));
        lblClear.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClear.setText("Clear");
        lblClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblClear.setOpaque(true);
        lblClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClearMouseClicked(evt);
            }
        });

        lblClose.setBackground(new java.awt.Color(217, 83, 79));
        lblClose.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblClose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClose.setText("X");
        lblClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblClose.setOpaque(true);
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bodoni MT", 1, 12)); // NOI18N
        jLabel7.setText("Last Updated Date  :  2018-09-17");

        jLabel8.setFont(new java.awt.Font("Bodoni MT", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Software By Supervision Technology (PVT), Ltd");

        lblClose1.setBackground(new java.awt.Color(255, 136, 0));
        lblClose1.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblClose1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClose1.setText("_");
        lblClose1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblClose1.setOpaque(true);
        lblClose1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClose1MouseClicked(evt);
            }
        });

        proBarLoading.setToolTipText("");
        proBarLoading.setStringPainted(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(proBarLoading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(lblProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblClear, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblClose, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)))
                        .addGap(13, 13, 13))
                    .addComponent(scrollPane)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblCompanyName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblProcess, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(proBarLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(10, 10, 10))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 690, 600));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(249, 249, 249));
        jLabel1.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Date");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblDate.setBackground(new java.awt.Color(249, 249, 249));
        lblDate.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDate.setText("2018-02-02");
        lblDate.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        lblCatMain.setBackground(new java.awt.Color(141, 152, 222));
        lblCatMain.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblCatMain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCatMain.setText("Transactions");
        lblCatMain.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCatMain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCatMain.setOpaque(true);
        lblCatMain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCatMainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCatMainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCatMainMouseExited(evt);
            }
        });

        lblCatSupplier.setBackground(new java.awt.Color(141, 192, 222));
        lblCatSupplier.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblCatSupplier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCatSupplier.setText("Supplier");
        lblCatSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCatSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCatSupplier.setOpaque(true);
        lblCatSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCatSupplierMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCatSupplierMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCatSupplierMouseExited(evt);
            }
        });

        lblCatEmployee.setBackground(new java.awt.Color(201, 192, 222));
        lblCatEmployee.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblCatEmployee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCatEmployee.setText("Employee");
        lblCatEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCatEmployee.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCatEmployee.setOpaque(true);
        lblCatEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCatEmployeeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCatEmployeeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCatEmployeeMouseExited(evt);
            }
        });

        lblCatFinalPayment.setBackground(new java.awt.Color(201, 222, 222));
        lblCatFinalPayment.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        lblCatFinalPayment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCatFinalPayment.setText("Final Payment");
        lblCatFinalPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCatFinalPayment.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCatFinalPayment.setOpaque(true);
        lblCatFinalPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCatFinalPaymentMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCatFinalPaymentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCatFinalPaymentMouseExited(evt);
            }
        });

        lblCeperator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCatFinalPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCeperator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCatSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCatMain, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCatEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCatFinalPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCatSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCatEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCatMain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCeperator)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 190, 160));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(91, 192, 222));
        jLabel6.setFont(new java.awt.Font("Bodoni MT", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Next Date");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 190, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setDefaultCloseOperation(SystemIntegrationSyncGUI.EXIT_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        Login login = new Login();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void lblClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClearMouseClicked
        dataClear();
        proBarLoading.setValue(0);
        System.out.println("Data Cleared !");
    }//GEN-LAST:event_lblClearMouseClicked

    private void lblGrnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGrnMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.GRN);
        }
    }//GEN-LAST:event_lblGrnMouseClicked

    private void lblStockAdjustMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblStockAdjustMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.ADJUSTMENT);
        }
    }//GEN-LAST:event_lblStockAdjustMouseClicked

    private void lblBrockerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBrockerMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.BROKER);
        }
    }//GEN-LAST:event_lblBrockerMouseClicked

    private void lblPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPaymentMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.PAYMENT);
        }
    }//GEN-LAST:event_lblPaymentMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        if (optionPain() == 0) {
            try {
                String date = SyncService.getInstance().getNextDate(lblDate.getText());
                lblDate.setText(date);
                dataClear();
            } catch (SQLException | ParseException ex) {
                Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void lblClose1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClose1MouseClicked
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lblClose1MouseClicked

    private void lblSupplierAdvanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierAdvanceMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.SUPPLIER_ADVANCE);
        }
    }//GEN-LAST:event_lblSupplierAdvanceMouseClicked

    private void lblSupplierLoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierLoanMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.SUPPLIER_LOAN);
        }
    }//GEN-LAST:event_lblSupplierLoanMouseClicked

    private void lblSupplierSettlementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierSettlementMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.SUPPLIER_SETTLEMENT);
        }
    }//GEN-LAST:event_lblSupplierSettlementMouseClicked

    private void lblSupplierTeaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierTeaMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.SUPPLIER_TEA);
        }
    }//GEN-LAST:event_lblSupplierTeaMouseClicked

    private void lblSupplierFertilizerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierFertilizerMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.SUPPLIER_FERTILIZER);
        }
    }//GEN-LAST:event_lblSupplierFertilizerMouseClicked

    private void lblSupplierOtherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierOtherMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.SUPPLIER_OTHER);
        }
    }//GEN-LAST:event_lblSupplierOtherMouseClicked

    private void lblFinalPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFinalPaymentMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.FINAL_PAYNEMT);
        }
    }//GEN-LAST:event_lblFinalPaymentMouseClicked

    private void lblEmployeeLoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeLoanMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.EMPLOYEE_LOAN);
        }
    }//GEN-LAST:event_lblEmployeeLoanMouseClicked

    private void lblEmployeeSettlementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeSettlementMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.EMPLOYEE_SETTLEMENT);
        }
    }//GEN-LAST:event_lblEmployeeSettlementMouseClicked

    private void lblEmployeeAdvanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeAdvanceMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.EMPLOYEE_ADVANCE);
        }
    }//GEN-LAST:event_lblEmployeeAdvanceMouseClicked

    private void lblEmployeeTeaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeTeaMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.EMPLOYEE_TEA);
        }
    }//GEN-LAST:event_lblEmployeeTeaMouseClicked

    private void lblEmployeeFertilizerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeFertilizerMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.EMPLOYEE_FERTILIZER);
        }
    }//GEN-LAST:event_lblEmployeeFertilizerMouseClicked

    private void lblEmployeeOtherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeOtherMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.EMPLOYEE_OTHER);
        }
    }//GEN-LAST:event_lblEmployeeOtherMouseClicked

    private void lblChequePrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChequePrintMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.CHEQUE_PRINT);
        }
    }//GEN-LAST:event_lblChequePrintMouseClicked

    private void lblBankDepositMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBankDepositMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.BANK_DEPOSIT);
        }
    }//GEN-LAST:event_lblBankDepositMouseClicked

    private void lblCatMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatMainMouseClicked
        view = 1;
        viewSubCategory(view);
    }//GEN-LAST:event_lblCatMainMouseClicked

    private void lblCatMainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatMainMouseEntered

    }//GEN-LAST:event_lblCatMainMouseEntered

    private void lblCatMainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatMainMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatMainMouseExited

    private void lblCatSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatSupplierMouseClicked
        view = 2;
        viewSubCategory(view);
    }//GEN-LAST:event_lblCatSupplierMouseClicked

    private void lblCatSupplierMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatSupplierMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatSupplierMouseEntered

    private void lblCatSupplierMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatSupplierMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatSupplierMouseExited

    private void lblCatEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatEmployeeMouseClicked
        view = 3;
        viewSubCategory(view);
    }//GEN-LAST:event_lblCatEmployeeMouseClicked

    private void lblCatEmployeeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatEmployeeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatEmployeeMouseEntered

    private void lblCatEmployeeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatEmployeeMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatEmployeeMouseExited

    private void lblCatFinalPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatFinalPaymentMouseClicked
        view = 4;
        viewSubCategory(view);
    }//GEN-LAST:event_lblCatFinalPaymentMouseClicked

    private void lblCatFinalPaymentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatFinalPaymentMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatFinalPaymentMouseEntered

    private void lblCatFinalPaymentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCatFinalPaymentMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCatFinalPaymentMouseExited

    private void lblLocalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLocalMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, Constant.LOCAL);
        }
    }//GEN-LAST:event_lblLocalMouseClicked

    private void lblDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDeleteMouseClicked
        if (optionPain() == 0) {
            loader(lblDate.getText(), loginUser, "DELETE");
        }
    }//GEN-LAST:event_lblDeleteMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//   
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblBankDeposit;
    private javax.swing.JLabel lblBrocker;
    private javax.swing.JLabel lblCatEmployee;
    private javax.swing.JLabel lblCatFinalPayment;
    private javax.swing.JLabel lblCatMain;
    private javax.swing.JLabel lblCatSupplier;
    private javax.swing.JLabel lblCeperator;
    private javax.swing.JLabel lblChequePrint;
    private javax.swing.JLabel lblClear;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblClose1;
    private javax.swing.JLabel lblCompanyName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDelete;
    private javax.swing.JLabel lblEmployeeAdvance;
    private javax.swing.JLabel lblEmployeeFertilizer;
    private javax.swing.JLabel lblEmployeeLoan;
    private javax.swing.JLabel lblEmployeeOther;
    private javax.swing.JLabel lblEmployeeSettlement;
    private javax.swing.JLabel lblEmployeeTea;
    private javax.swing.JLabel lblFinalPayment;
    private javax.swing.JLabel lblGrn;
    private javax.swing.JLabel lblLocal;
    private javax.swing.JLabel lblPayment;
    private javax.swing.JLabel lblProcess;
    private javax.swing.JLabel lblStockAdjust;
    private javax.swing.JLabel lblSupplierAdvance;
    private javax.swing.JLabel lblSupplierFertilizer;
    private javax.swing.JLabel lblSupplierLoan;
    private javax.swing.JLabel lblSupplierOther;
    private javax.swing.JLabel lblSupplierSettlement;
    private javax.swing.JLabel lblSupplierTea;
    private javax.swing.JPanel pnlSubCategory;
    private javax.swing.JProgressBar proBarLoading;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables

    private int optionPain() {
        return JOptionPane.showConfirmDialog(null, "Are you sure ?", "Warning", JOptionPane.YES_OPTION);
    }

    private void dataClear() {
        txtLog.setText("");
        try {
            getDetailCount(lblDate.getText());
        } catch (SQLException ex) {
            System.out.println("get detail count function not support !");
            Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loader(String date, Integer loginUser, String type) {
        lblProcess.setText("Loading...");
//        lblProcess.setIcon(new ImageIcon("../images/loader.gif"));
        txtLog.setText("");
        proBarLoading.setValue(0);
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (null == type) {
                        throw new RuntimeException("transaction type doesn't match into doInBackground method !");
                    } else {
                        switch (type) {
                            case Constant.ADJUSTMENT:
                                executeStockAdjustment(date, loginUser);
                                break;
                            case Constant.GRN:
                                executeGrn(date, loginUser);
                                break;
                            case Constant.BROKER:
                                executeBocker(date, loginUser, Constant.BROKER);
                                break;
                            case Constant.LOCAL:
                                executeBocker(date, loginUser, Constant.LOCAL);
                                break;
                            case Constant.PAYMENT:
                                executePayment(date, loginUser);
                                break;
                            case Constant.SUPPLIER_ADVANCE:
                                executeSupplierAdvance(date, loginUser, Constant.SUPPLIER_ADVANCE);
                                break;
                            case Constant.SUPPLIER_LOAN:
                                executeSupplierAdvance(date, loginUser, Constant.SUPPLIER_LOAN);
                                break;
                            case Constant.SUPPLIER_SETTLEMENT:
                                executeSupplierAdvance(date, loginUser, Constant.SUPPLIER_SETTLEMENT);
                                break;
                            case Constant.EMPLOYEE_ADVANCE:
                                executeSupplierAdvance(date, loginUser, Constant.EMPLOYEE_ADVANCE);
                                break;
                            case Constant.EMPLOYEE_LOAN:
                                executeSupplierAdvance(date, loginUser, Constant.EMPLOYEE_LOAN);
                                break;
                            case Constant.EMPLOYEE_SETTLEMENT:
                                executeSupplierAdvance(date, loginUser, Constant.EMPLOYEE_SETTLEMENT);
                                break;
                            case Constant.SUPPLIER_TEA:
                                executeTeaIssue(date, loginUser, Constant.SUPPLIER_TEA);
                                break;
                            case Constant.SUPPLIER_FERTILIZER:
                                executeSupplierIssue(date, loginUser, Constant.SUPPLIER_FERTILIZER);
                                break;
                            case Constant.SUPPLIER_OTHER:
                                executeSupplierIssue(date, loginUser, Constant.SUPPLIER_OTHER);
                                break;
                            case Constant.EMPLOYEE_TEA:
                                executeTeaIssue(date, loginUser, Constant.EMPLOYEE_TEA);
                                break;
                            case Constant.EMPLOYEE_FERTILIZER:
                                executeSupplierIssue(date, loginUser, Constant.EMPLOYEE_FERTILIZER);
                                break;
                            case Constant.EMPLOYEE_OTHER:
                                executeSupplierIssue(date, loginUser, Constant.EMPLOYEE_OTHER);
                                break;
                            case Constant.FINAL_PAYNEMT:
                                executeFinalPayment(date, loginUser);
                                break;
                            case Constant.CHEQUE_PRINT:
                                executeChequePrint(date, loginUser, Constant.CHEQUE_PRINT);
                                break;
                            case Constant.BANK_DEPOSIT:
                                executeChequePrint(date, loginUser, Constant.BANK_DEPOSIT);
                                break;
                            case "DELETE":
                                executeDelete(date, loginUser);
                                break;

                            default:
                                throw new RuntimeException("transaction type doesn't match into doInBackground method !");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    lblProcess.setText("");
                    Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }

            @Override
            protected void done() {
                lblProcess.setText("");
            }

        }.execute();
    }

    private void viewSubCategory(Integer view) {
        allHide();
        if (null == view) {

        } else {
            switch (view) {
                case 1:
                    lblCatMain.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                    lblGrn.setVisible(true);
                    lblBrocker.setVisible(true);
                    lblLocal.setVisible(true);
                    lblPayment.setVisible(true);
                    lblStockAdjust.setVisible(true);
                    lblDelete.setVisible(true);
                    break;
                case 2:
                    lblCatSupplier.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                    lblSupplierAdvance.setVisible(true);
                    lblSupplierFertilizer.setVisible(true);
                    lblSupplierLoan.setVisible(true);
                    lblSupplierOther.setVisible(true);
                    lblSupplierSettlement.setVisible(true);
                    lblSupplierTea.setVisible(true);
                    break;
                case 3:
                    lblCatEmployee.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                    lblEmployeeAdvance.setVisible(true);
                    lblEmployeeFertilizer.setVisible(true);
                    lblEmployeeLoan.setVisible(true);
                    lblEmployeeOther.setVisible(true);
                    lblEmployeeSettlement.setVisible(true);
                    lblEmployeeTea.setVisible(true);
                    break;
                case 4:
                    lblCatFinalPayment.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                    lblFinalPayment.setVisible(true);
                    lblChequePrint.setVisible(true);
                    lblBankDeposit.setVisible(true);
                    break;
                default:
                    break;
            }
        }
    }

    private void allHide() {
        lblGrn.setVisible(false);
        lblBrocker.setVisible(false);
        lblLocal.setVisible(false);
        lblPayment.setVisible(false);
        lblStockAdjust.setVisible(false);
        lblDelete.setVisible(false);

        lblSupplierAdvance.setVisible(false);
        lblSupplierFertilizer.setVisible(false);
        lblSupplierLoan.setVisible(false);
        lblSupplierOther.setVisible(false);
        lblSupplierSettlement.setVisible(false);
        lblSupplierTea.setVisible(false);

        lblEmployeeAdvance.setVisible(false);
        lblEmployeeFertilizer.setVisible(false);
        lblEmployeeLoan.setVisible(false);
        lblEmployeeOther.setVisible(false);
        lblEmployeeSettlement.setVisible(false);
        lblEmployeeTea.setVisible(false);

        lblFinalPayment.setVisible(false);
        lblChequePrint.setVisible(false);
        lblBankDeposit.setVisible(false);

        lblCatMain.setBorder(null);
        lblCatSupplier.setBorder(null);
        lblCatEmployee.setBorder(null);
        lblCatFinalPayment.setBorder(null);

    }

}
