/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.OperationController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.operation_model.Advance;
import model.operation_model.AdvanceDetail;
import model.operation_model.Brocker;
import model.operation_model.ChequePrint;
import model.operation_model.ChequePrintDetail;
import model.operation_model.Delete;
import model.operation_model.FinalPayment;
import model.operation_model.FinalPaymentDetail;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
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
public class OperationService {

    public static List<GrnDetail> getGrnDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getGrnDetail(indexNo, operaConnection);
    }

    public static List<InvoiceDetail> getInvoiceDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getInvoiceDetail(indexNo, operaConnection);
    }

    public static Integer updateInvoice(Invoice invoice, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().updateInvoice(invoice, operaConnection);
    }

    public static List<PaymentDetail> getPaymentDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getPaymentDetail(indexNo, operaConnection);
    }

    public static List<PaymentInformation> getPaymentInformations(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getPaymentInformations(indexNo, operaConnection);
    }

    public static List<StockAdjustmentDetail> getAdjustmentDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getAdjustmentDetail(indexNo, operaConnection);
    }

    public static Integer updateAdjustment(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().updateAdjustment(indexNo, operaConnection);
    }

    public static String getCustomerNoByInvoice(String invoice, String type, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getCustomerNoByInvoice(invoice, type, operaConnection);
    }

    public static List<Advance> getAdvanceByType(String date, String type, Connection operaConnection) throws SQLException {
        String query = "call p_get_advance(?,?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setString(1, date);
        ps.setString(2, type);
        ResultSet rst = ps.executeQuery();
        List<Advance> returnList = new ArrayList<>();
        while (rst.next()) {
            Advance advance = new Advance();
            advance.setIndexNo(rst.getInt(1));
            advance.setRefNo(rst.getString(2));
            advance.setEnterDate(rst.getString(3));
            advance.setUpdatedDate(rst.getString(4));
            advance.setUpdatedTime(rst.getString(5));
            advance.setType(rst.getString(6));
            advance.setBranch(rst.getInt(7));
            advance.setAccAccount(rst.getInt(8));
            advance.setPaymentType(rst.getString(9));
            advance.setChequeDate(rst.getString(10));
            advance.setChequeNo(rst.getString(11));
            advance.setAmount(rst.getBigDecimal(12));
            advance.setCheck(rst.getBoolean(13));
            returnList.add(advance);
        }
        return returnList;
    }

    public static List<AdvanceDetail> getAdvanceDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "call p_get_advance_detail(?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        ResultSet rst = ps.executeQuery();
        List<AdvanceDetail> returnList = new ArrayList<>();
        while (rst.next()) {
            AdvanceDetail advanceDetail = new AdvanceDetail();
            advanceDetail.setIndexNo(rst.getInt(1));
            advanceDetail.setAdvance(rst.getInt(2));
            advanceDetail.setRouteNo(rst.getString(3));
            advanceDetail.setRouteName(rst.getString(4));
            advanceDetail.setSupplierNo(rst.getString(5));
            advanceDetail.setSupplierName(rst.getString(6));
            advanceDetail.setAmount(rst.getBigDecimal(7));

            returnList.add(advanceDetail);
        }
        return returnList;
    }

    public static Integer updateAdvanceStatus(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "call p_update_advance_status(?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        return ps.executeUpdate();
    }

    public static List<SupplierIssueSummary> getIssueByType(String date, String type, Connection operaConnection) throws SQLException {
        String query = "call p_get_issue(?,?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setString(1, date);
        ps.setString(2, type);
        ResultSet rst = ps.executeQuery();
        List<SupplierIssueSummary> returnList = new ArrayList<>();
        while (rst.next()) {
            SupplierIssueSummary issueSummary = new SupplierIssueSummary();
            issueSummary.setIndexNo(rst.getInt(1));
            issueSummary.setRefNo(rst.getString(2));
            issueSummary.setRouteNo(rst.getString(3));
            issueSummary.setRouteName(rst.getString(4));
            issueSummary.setSupplierNo(rst.getString(5));
            issueSummary.setSupplierName(rst.getString(6));
            issueSummary.setType(rst.getString(7));
            issueSummary.setEnterDate(rst.getString(8));
            issueSummary.setUpdatedDate(rst.getString(9));
            issueSummary.setUpdatedTime(rst.getString(10));
            issueSummary.setBranch(rst.getInt(11));
            issueSummary.setCheck(rst.getBoolean(12));
            returnList.add(issueSummary);
        }
        return returnList;
    }

    public static List<SupplierIssueDetail> getIssueDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "call p_get_issue_detail(?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        ResultSet rst = ps.executeQuery();
        List<SupplierIssueDetail> returnList = new ArrayList<>();
        while (rst.next()) {
            SupplierIssueDetail issueDetail = new SupplierIssueDetail();
            issueDetail.setIndexNo(rst.getInt(1));
            issueDetail.setIssueSummary(rst.getInt(2));
            issueDetail.setItemNo(rst.getString(3));
            issueDetail.setItemName(rst.getString(4));
            issueDetail.setItemType(rst.getString(5));
            issueDetail.setItemUnit(rst.getString(6));
            issueDetail.setItemBarcode(rst.getString(7));
            issueDetail.setItemBrand(rst.getString(8));
            issueDetail.setItemCategory(rst.getString(9));
            issueDetail.setCostPrice(rst.getBigDecimal(10));
            issueDetail.setSalesPrice(rst.getBigDecimal(11));
            issueDetail.setQty(rst.getBigDecimal(12));
            issueDetail.setStockRemoveQty(rst.getBigDecimal(13));
            issueDetail.setValue(rst.getBigDecimal(14));
            issueDetail.setIsZeroItem(rst.getBoolean(15));

            returnList.add(issueDetail);
        }
        return returnList;
    }

    public static Integer updateIssueStatus(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "call p_update_issue_status(?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        return ps.executeUpdate();
    }

    public static List<FinalPayment> getFinalPayment(String date, Connection operaConnection) throws SQLException {
        String query = "call p_get_final_payment(?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setString(1, date);
        ResultSet rst = ps.executeQuery();
        List<FinalPayment> data = new ArrayList<>();
        while (rst.next()) {
            FinalPayment finalPayment = new FinalPayment();
            finalPayment.setIndexNo(rst.getInt(1));
            finalPayment.setRefNo(rst.getString(2));
            finalPayment.setEnterDate(rst.getString(3));
            finalPayment.setFinalPaymentDate(rst.getString(4));
            finalPayment.setUpdatedDate(rst.getString(5));
            finalPayment.setUpdatedTime(rst.getString(6));
            finalPayment.setBranch(rst.getInt(7));
            finalPayment.setCheck(rst.getBoolean(8));

            data.add(finalPayment);
        }
        return data;
    }

    public static List<FinalPaymentDetail> getFinalPaymentDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "call p_get_final_payment_detail(?);";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        ResultSet rst = ps.executeQuery();
        List<FinalPaymentDetail> detailList = new ArrayList<>();
        while (rst.next()) {
            FinalPaymentDetail finalPaymentDetail = new FinalPaymentDetail();
            finalPaymentDetail.setIndexNo(rst.getInt(1));
            finalPaymentDetail.setFinalPayment(rst.getInt(2));
            finalPaymentDetail.setRouteNo(rst.getString(3));
            finalPaymentDetail.setRouteName(rst.getString(4));
            finalPaymentDetail.setSupplierNo(rst.getString(5));
            finalPaymentDetail.setSupplierName(rst.getString(6));
            finalPaymentDetail.setGlValue(rst.getBigDecimal(7));
            finalPaymentDetail.setTransport(rst.getBigDecimal(8));
            finalPaymentDetail.setAdvanceInterest(rst.getBigDecimal(9));
            finalPaymentDetail.setLoanCapital(rst.getBigDecimal(10));
            finalPaymentDetail.setLoanInterest(rst.getBigDecimal(11));
            finalPaymentDetail.setFertilizer(rst.getBigDecimal(12));
            finalPaymentDetail.setOther(rst.getBigDecimal(13));
            finalPaymentDetail.setStamp(rst.getBigDecimal(14));
            finalPaymentDetail.setCash(rst.getBigDecimal(15));
            finalPaymentDetail.setBank(rst.getBigDecimal(16));
            finalPaymentDetail.setCheque(rst.getBigDecimal(17));

            detailList.add(finalPaymentDetail);
        }
        return detailList;
    }

    public static Integer updateFinalPayment(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().updateFinalPayment(indexNo, operaConnection);
    }

    public static List<ChequePrint> getChequePrint(String date, String type, Connection operaConnection) throws SQLException {
        String query = "select cheque_print.`*` from cheque_print \n"
                + "where cheque_print.`type`=? and cheque_print.enter_date<=? and cheque_print.check=false";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setString(1, type);
        ps.setString(2, date);
        ResultSet rst = ps.executeQuery();
        List<ChequePrint> data = new ArrayList<>();
        while (rst.next()) {
            ChequePrint chequePrint = new ChequePrint();
            chequePrint.setIndexNo(rst.getInt(1));
            chequePrint.setRefNo(rst.getString(2));
            chequePrint.setType(rst.getString(3));
            chequePrint.setRouteNo(rst.getString(4));
            chequePrint.setEnterDate(rst.getString(5));
            chequePrint.setChequeDate(rst.getString(6));
            chequePrint.setUpdatedDate(rst.getString(7));
            chequePrint.setUpdatedTime(rst.getString(8));
            chequePrint.setBankAccount(rst.getInt(9));
            chequePrint.setBranch(rst.getInt(10));
            chequePrint.setCheck(rst.getBoolean(11));

            data.add(chequePrint);
        }
        return data;
    }

    public static List<ChequePrintDetail> getChequePrintDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "select cheque_print_detail.`*` from cheque_print_detail \n"
                + "where cheque_print_detail.cheque_print=?";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        ResultSet rst = ps.executeQuery();
        List<ChequePrintDetail> data = new ArrayList<>();
        while (rst.next()) {
            ChequePrintDetail chequePrintDetail = new ChequePrintDetail();
            chequePrintDetail.setIndexNo(rst.getInt(1));
            chequePrintDetail.setChequePrint(rst.getInt(2));
            chequePrintDetail.setSupplierNo(rst.getString(3));
            chequePrintDetail.setChequeNo(rst.getString(4));
            chequePrintDetail.setAmount(rst.getBigDecimal(5));

            data.add(chequePrintDetail);
        }
        return data;
    }

    static Integer updateChequePrint(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().updateChequePrint(indexNo, operaConnection);
    }

    static List<TeaIssue> getTeaIssue(String date, String type, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().getTeaIssue(date, type, operaConnection);
    }

    static List<TeaIssueDetail> getTeaIssueDetail(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "select tea_issue_detail.`*` from tea_issue_detail \n"
                + "where tea_issue_detail.tea_issue_summary=?";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        ResultSet rst = ps.executeQuery();
        List<TeaIssueDetail> data = new ArrayList<>();
        while (rst.next()) {
            TeaIssueDetail detail = new TeaIssueDetail();
            detail.setIndexNo(rst.getInt(1));
            detail.setTeaIssue(rst.getInt(2));
            detail.setRouteNo(rst.getString(3));
            detail.setRouteName(rst.getString(4));
            detail.setSupplierNo(rst.getString(5));
            detail.setSupplierName(rst.getString(6));
            detail.setItemNo(rst.getString(7));
            detail.setItemName(rst.getString(8));
            detail.setItemType(rst.getString(9));
            detail.setItemUnit(rst.getString(10));
            detail.setItemBarcode(rst.getString(11));
            detail.setItemBrand(rst.getString(12));
            detail.setItemCategory(rst.getString(13));
            detail.setCostPrice(rst.getBigDecimal(14));
            detail.setSalesPrice(rst.getBigDecimal(15));
            detail.setQty(rst.getBigDecimal(16));
            detail.setStockRemoveQty(rst.getBigDecimal(17));
            detail.setValue(rst.getBigDecimal(18));

            data.add(detail);
        }
        return data;
    }

    static Integer updateTeaIssue(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "update tea_issue_summary \n"
                + "	set tea_issue_summary.`check`=true ,\n"
                + "	tea_issue_summary.updated_date=(CURRENT_DATE()),\n"
                + "	tea_issue_summary.updated_time=CURRENT_TIME()\n"
                + "	where tea_issue_summary.index_no =?;";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        return ps.executeUpdate();
    }

    public static List<Brocker> getBrocker(String date, String type, Connection operaConnection) throws SQLException {
        String query = "select brocker.`*` from brocker \n"
                + "where brocker.client_type=? and brocker.enter_date<=? and brocker.check=false";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setString(1, type);
        ps.setString(2, date);
        ResultSet rst = ps.executeQuery();
        List<Brocker> data = new ArrayList<>();
        while (rst.next()) {
            Brocker brocker = new Brocker();
            brocker.setIndexNo(rst.getInt(1));
            brocker.setRefNo(rst.getString(2));
            brocker.setClientNo(rst.getString(3));
            brocker.setClientName(rst.getString(4));
            brocker.setClientType(rst.getString(5));
            brocker.setEnterDate(rst.getString(6));
            brocker.setGrossValue(rst.getBigDecimal(7));
            brocker.setVat(rst.getBigDecimal(8));
            brocker.setNbt(rst.getBigDecimal(9));
            brocker.setInsurence(rst.getBigDecimal(10));
            brocker.setCommission(rst.getBigDecimal(11));
            brocker.setLotCharges(rst.getBigDecimal(12));
            brocker.setOther1(rst.getBigDecimal(13));
            brocker.setOther2(rst.getBigDecimal(14));
            brocker.setNetValue(rst.getBigDecimal(15));
            brocker.setBranch(rst.getInt(16));
            brocker.setUpdatedDate(rst.getString(17));
            brocker.setUpdatedTime(rst.getString(18));
            brocker.setCheck(rst.getBoolean(19));

            data.add(brocker);
        }
        return data;
    }

    public static Integer updateBrocker(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "update brocker \n"
                + "	set brocker.`check`=true ,\n"
                + "	brocker.updated_date=(CURRENT_DATE()),\n"
                + "	brocker.updated_time=CURRENT_TIME()\n"
                + "	where brocker.index_no =?;";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        return ps.executeUpdate();
    }

    static List<Delete> getDeleteData(String date, Connection operaConnection) throws SQLException {
        String query = "select t_delete.`*` from t_delete where t_delete.enter_date<=? and t_delete.check=false";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setString(1, date);
        ResultSet rst = ps.executeQuery();
        List<Delete> data = new ArrayList<>();
        while (rst.next()) {
            Delete delete = new Delete();
            delete.setIndexNo(rst.getInt(1));
            delete.setRefNo(rst.getString(2));
            delete.setType(rst.getString(3));
            delete.setEnterDate(rst.getString(4));
            delete.setUpdatedDate(rst.getString(5));
            delete.setUpdatedTime(rst.getString(6));
            delete.setCheck(rst.getBoolean(7));

            data.add(delete);
        }
        return data;
    }

    static Integer updateDelete(Integer indexNo, Connection operaConnection) throws SQLException {
        String query = "update t_delete \n"
                + "	set t_delete.`check`=true ,\n"
                + "	t_delete.updated_date=(CURRENT_DATE()),\n"
                + "	t_delete.updated_time=CURRENT_TIME()\n"
                + "	where t_delete.index_no =?;";
        PreparedStatement ps = operaConnection.prepareStatement(query);
        ps.setInt(1, indexNo);
        return ps.executeUpdate();
    }

    public ArrayList<Grn> getNotCheckGrnList(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckGrnList(date);
    }

    public static Integer updateGrn(Grn grn, Connection connection) throws SQLException {
        return OperationController.getInstance().updateGrn(grn, connection);
    }

    public ArrayList<Invoice> getNotCheckInvoiceList(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckInvoiceList(date);
    }

    public ArrayList<Payment> getNotCheckPaymentList(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckPaymentList(date);
    }

    public static Integer updatePayment(Integer indexNo, Connection operaConnection) throws SQLException {
        return OperationController.getInstance().updatePayment(indexNo, operaConnection);
    }

    public Integer getNotGrnCount(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckGrnCount(date);
    }

    public Integer getNotCheckInvoiceCount(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckInvoiceCount(date);
    }

    public Integer getNotCheckPaymentCount(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckPaymentCount(date);
    }

    public Integer getNotStockAdjustmentCount(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckStockAdjustmentCount(date);
    }

    public ArrayList<StockAdjustment> getNotCheckStockAdjustmentList(String date) throws SQLException {
        return OperationController.getInstance().getNotCheckStockAdjustmentList(date);
    }

    public String getTransactionDate() throws SQLException {
        return OperationController.getInstance().getTransactionDate();
    }

    public String getNextDate(String date) throws SQLException, ParseException {
        return OperationController.getInstance().getNextDate(date);
    }

    public HashMap getAdvanceCount(String date) throws SQLException {
        return OperationController.getInstance().getAdvanceCount(date);
    }

    public HashMap getSupplierIssueCount(String date) throws SQLException {
        return OperationController.getInstance().getSupplierIssueCount(date);
    }

    public HashMap getTeaIssueCount(String date) throws SQLException {
        return OperationController.getInstance().getTeaIssueCount(date);

    }

    public Integer getFinalPaynemtCount(String date) throws SQLException {
        return OperationController.getInstance().getFinalPaymentCount(date);
    }

    public HashMap getChequePrintCount(String date) throws SQLException {
        return OperationController.getInstance().getChequePrintCount(date);
    }

    public HashMap getBrockerCount(String date) throws SQLException {
        return OperationController.getInstance().getBrockerCount(date);
    }

    public Integer getNotCheckDeleteCount(String date) throws SQLException {
        return OperationController.getInstance().getDeleteCount(date);
    }
}
