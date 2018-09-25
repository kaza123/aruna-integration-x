/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import common.Constant;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import model.account_model.MAccAccount;
import model.account_model.MBranch;
import model.account_model.MClient;
import model.account_model.MSupplier;
import model.account_model.TStockLedger;
import model.operation_model.Grn;
import model.operation_model.GrnDetail;
import model.account_model.TTypeIndexDetail;
import model.operation_model.Invoice;
import model.operation_model.InvoiceDetail;
import model.operation_model.Payment;
import model.operation_model.PaymentDetail;
import model.operation_model.PaymentInformation;
import model.operation_model.StockAdjustment;
import model.operation_model.StockAdjustmentDetail;

/**
 *
 * @author 'Kasun Chamara'
 */
public class AccountController {

    private static AccountController instance;

    public static HashMap<Integer, Object> getAccLedgerNumber(Integer branch, String type, Connection accConnection) throws SQLException {
//        get branch
        MBranch branchModel = getBranch(branch, accConnection);
        if (branchModel == null) {
            throw new RuntimeException("Can't find Branch !");
        }
//        getNumber
        Integer nextNumber = getNextNumber(branch, type, accConnection);
        if (nextNumber < 0) {
            throw new RuntimeException("Next number generate fail !");
        }
//        getDeleteFrfNumber
        Integer deleteRefNumber = getDeleteRefNumber(accConnection);
        if (deleteRefNumber < 0) {
            throw new RuntimeException("Delete Ref number generate fail !");
        }
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, branchModel.getBranchCode());
        map.put(2, nextNumber);
        map.put(3, deleteRefNumber);
        return map;
    }

    public static int saveStockAdjustment(StockAdjustment adjustment, Connection connection) throws SQLException {
        String insertSql = "insert into t_stock_adjustment (enter_date,enter_time,updated_date,updated_time,branch,ref_no,form_type)\n"
                + " values (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, adjustment.getEnterDate());
        preparedStatement.setString(2, adjustment.getEnterTime());
        preparedStatement.setString(3, adjustment.getUpdatedDate());
        preparedStatement.setString(4, adjustment.getUpdatedTime());
        preparedStatement.setInt(5, adjustment.getBranch());
        preparedStatement.setString(6, adjustment.getRefNo());
        preparedStatement.setString(7, adjustment.getFormType());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;

    }

    public static String getCompantName(Connection connection) throws SQLException {
        String query = "select m_company.name from m_company\n"
                + "where m_company.index_no=1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }

    public AccountController() throws SQLException {
    }

    public Integer saveNbtForLedger(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, HashMap<Integer, Object> ledgerMap, Integer user, Connection accConnection) throws SQLException {
        Integer vatAccount = getSubAccountOf(Constant.VAT_ACCOUNT_OUT, accConnection);
        return saveAccLedgerVatAndNbt(grn, supplierMap, grnIndex, "System Integration GRN save VAT !", vatAccount, ledgerMap, user, accConnection);
    }

    public Integer saveVatForLedger(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, HashMap<Integer, Object> ledgerMap, Integer user, Connection accConnection) throws SQLException {
        Integer vatAccount = getSubAccountOf(Constant.NBT_ACCOUNT_OUT, accConnection);
        return saveAccLedgerVatAndNbt(grn, supplierMap, grnIndex, "System Integration GRN save NBT !", vatAccount, ledgerMap, user, accConnection);
    }

    public static AccountController getInstance() throws SQLException {
        if (instance == null) {
            instance = new AccountController();
        }
        return instance;
    }

    public Integer getSubAccountOf(String name, Connection connection) throws SQLException {
        String query = "select m_acc_setting.acc_account\n"
                + "from m_acc_setting where m_acc_setting.name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public List<MAccAccount> getSubAccountOfList(Integer subAccOf, Connection connection) throws SQLException {
        String query = "select m_acc_account.*\n"
                + "from m_acc_account\n"
                + "where m_acc_account.sub_account_of=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, subAccOf);
        ResultSet rst = preparedStatement.executeQuery();
        ArrayList<MAccAccount> list = new ArrayList<>();
        while (rst.next()) {
            MAccAccount mAccAccount = new MAccAccount();
            mAccAccount.setIndexNo(rst.getInt(1));
            mAccAccount.setName(rst.getString(2));
            mAccAccount.setLevel(rst.getString(3));
            mAccAccount.setAccCode(rst.getString(4));
            mAccAccount.setCop(rst.getBoolean(5));
            mAccAccount.setAccMain(rst.getInt(6));
            mAccAccount.setUser(rst.getInt(7));
            mAccAccount.setAccType(rst.getString(8));
            mAccAccount.setSubAccountOf(rst.getInt(9));
            mAccAccount.setIsAccAccount(rst.getBoolean(10));
            mAccAccount.setDescription(rst.getString(11));
            mAccAccount.setSubAccountCount(rst.getInt(12));
            list.add(mAccAccount);
        }
        return list;
    }

    public MAccAccount getSubAccount(Integer subAccountOf, Connection connection) throws SQLException {
        String query = "select m_acc_account.*\n"
                + "from m_acc_account\n"
                + "where m_acc_account.index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, subAccountOf);
        ResultSet rst = preparedStatement.executeQuery();
        MAccAccount mAccAccount = new MAccAccount();
        if (rst.next()) {
            mAccAccount.setIndexNo(rst.getInt(1));
            mAccAccount.setName(rst.getString(2));
            mAccAccount.setLevel(rst.getString(3));
            mAccAccount.setAccCode(rst.getString(4));
            mAccAccount.setCop(rst.getBoolean(5));
            mAccAccount.setAccMain(rst.getInt(6));
            mAccAccount.setUser(rst.getInt(7));
            mAccAccount.setAccType(rst.getString(8));
            mAccAccount.setSubAccountOf(rst.getInt(9));
            mAccAccount.setIsAccAccount(rst.getBoolean(10));
            mAccAccount.setDescription(rst.getString(11));
            mAccAccount.setSubAccountCount(rst.getInt(12));
            return mAccAccount;
        }
        return null;
    }

    public int updateAccAccount(MAccAccount mAccAccount, Connection connection) throws SQLException {
        String insertSql = "UPDATE m_acc_account set name=? , `level`=? , acc_code=? , cop=? , acc_main=? , `user`=?\n"
                + ", acc_type=? , sub_account_of=? , is_acc_account=? , description=? , sub_account_count=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, mAccAccount.getName());
        preparedStatement.setString(2, mAccAccount.getLevel());
        preparedStatement.setString(3, mAccAccount.getAccCode());
        preparedStatement.setBoolean(4, mAccAccount.getCop());
        preparedStatement.setInt(5, mAccAccount.getAccMain());
        preparedStatement.setInt(6, mAccAccount.getUser());
        preparedStatement.setString(7, mAccAccount.getAccType());
        preparedStatement.setInt(8, mAccAccount.getSubAccountOf());
        preparedStatement.setBoolean(9, mAccAccount.getIsAccAccount());
        preparedStatement.setString(10, mAccAccount.getDescription());
        preparedStatement.setInt(11, mAccAccount.getSubAccountCount());
        preparedStatement.setInt(12, mAccAccount.getIndexNo());

        return preparedStatement.executeUpdate();

    }

    public int saveAccAccount(MAccAccount mAccAccount, Connection connection) throws SQLException {
        String insertSql = "insert into m_acc_account (name,`level`,acc_code,cop,acc_main,`user`\n"
                + ",acc_type,sub_account_of,is_acc_account,description,sub_account_count)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, mAccAccount.getName());
        preparedStatement.setString(2, mAccAccount.getLevel());
        preparedStatement.setString(3, mAccAccount.getAccCode());
        preparedStatement.setBoolean(4, mAccAccount.getCop());
        preparedStatement.setInt(5, mAccAccount.getAccMain());
        preparedStatement.setInt(6, mAccAccount.getUser());
        preparedStatement.setString(7, mAccAccount.getAccType());
        preparedStatement.setInt(8, mAccAccount.getSubAccountOf());
        preparedStatement.setBoolean(9, mAccAccount.getIsAccAccount());
        preparedStatement.setString(10, mAccAccount.getDescription());
        preparedStatement.setInt(11, mAccAccount.getSubAccountCount());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveClientMaster(MClient client, Connection connection) throws SQLException {
        String insertSql = "insert into m_client (acc_account,branch,date,is_new,mobile,name,resident)\n"
                + " values (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, client.getAccAccount());
        preparedStatement.setInt(2, client.getBranch());
        preparedStatement.setString(3, client.getDate());
        preparedStatement.setBoolean(4, client.isIsNew());
        preparedStatement.setString(5, client.getMobile());
        preparedStatement.setString(6, client.getName());
        preparedStatement.setString(7, client.getResident());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveSupplierMaster(MSupplier supplier, Connection connection) throws SQLException {
        String insertSql = "insert into m_supplier (acc_account,name,contact_name,contact_no,type)\n"
                + " values (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, supplier.getAccAccount());
        preparedStatement.setString(2, supplier.getName());
        preparedStatement.setString(3, supplier.getContactName());
        preparedStatement.setString(4, supplier.getContactNo());
        preparedStatement.setString(5, supplier.getType());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveGrn(Grn grn, HashMap<Integer, Integer> map, Connection connection) throws SQLException {
        String insertSql = "insert into t_grn (supplier,number,date,amount,ref_number,branch,nbt,nbt_value,vat,vat_value,\n"
                + "grand_amount,status,type,is_nbt,is_vat,credit_period)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, map.get(2));
        preparedStatement.setString(2, grn.getGrnNo());
        preparedStatement.setString(3, grn.getEnterDate());
        preparedStatement.setBigDecimal(4, grn.getTotalValue());
        preparedStatement.setString(5, grn.getRefNo());
        preparedStatement.setInt(6, grn.getBranch());
        preparedStatement.setBigDecimal(7, grn.getNbt());
        preparedStatement.setBigDecimal(8, grn.getNbtValue());
        preparedStatement.setBigDecimal(9, grn.getVat());
        preparedStatement.setBigDecimal(10, grn.getVatValue());
        preparedStatement.setBigDecimal(11, grn.getFinalValue());
        preparedStatement.setString(12, Constant.STATUS_APPROVED);
        preparedStatement.setString(13, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setBoolean(14, (grn.getNbtValue().doubleValue() > 0));
        preparedStatement.setBoolean(15, (grn.getVatValue().doubleValue() > 0));
        preparedStatement.setInt(16, grn.getCreditPeriod());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public int saveGrnDetail(GrnDetail detail, Connection connection) throws SQLException {
        String insertSql = "insert into t_grn_item (grn,item,price,qty,value,discount,discount_value,net_value)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, detail.getGrn());
        preparedStatement.setInt(2, Integer.parseInt(detail.getItemNo()));
        preparedStatement.setBigDecimal(3, detail.getCost());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, detail.getValue());
        preparedStatement.setBigDecimal(6, detail.getDiscount());
        preparedStatement.setBigDecimal(7, detail.getDiscountValue());
        preparedStatement.setBigDecimal(8, detail.getNetValue());

        return preparedStatement.executeUpdate();

    }

    public Integer saveItemMaster(GrnDetail detail, Integer accAccountIndex, Connection connection) throws SQLException {
        String insertSql = "insert into m_item (name,barcode,print_description,cost_price,\n"
                + "type,re_order_max,re_order_min,account,sale_price_normal,sale_price_register,unit,category,sub_category,brand)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getItemBarcode());
        preparedStatement.setString(3, detail.getItemName());
        preparedStatement.setBigDecimal(4, detail.getCost());
        preparedStatement.setString(5, detail.getItemType());
        preparedStatement.setBigDecimal(6, detail.getReorderMax());
        preparedStatement.setBigDecimal(7, detail.getReorderMin());
        preparedStatement.setInt(8, accAccountIndex);
        preparedStatement.setBigDecimal(9, detail.getSalesPrice());
        preparedStatement.setBigDecimal(10, detail.getSalesPrice());
        preparedStatement.setString(11, detail.getItemUnit());
        preparedStatement.setInt(12, Integer.parseInt(detail.getItemCategory()));
        preparedStatement.setInt(13, Integer.parseInt(detail.getItemSubCategory()));
        preparedStatement.setInt(14, Integer.parseInt(detail.getItemBrand()));

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveItemUnitMaster(GrnDetail detail, Integer item, Connection connection) throws SQLException {

        String insertSql = "insert into m_item_units (item,name,unit,qty,sale_price_normal,sale_price_register,cost_price,item_unit_type)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, item);
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setString(3, detail.getItemUnit());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, detail.getSalesPrice());
        preparedStatement.setBigDecimal(6, detail.getSalesPrice());
        preparedStatement.setBigDecimal(7, detail.getCost());
        preparedStatement.setString(8, Constant.ITEM_UNIT_MAIN);

        return preparedStatement.executeUpdate();

    }

    public ArrayList<MBranch> getBranchList(Connection connection) throws SQLException {
        String query = "select m_branch.* from m_branch";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rst = preparedStatement.executeQuery();
        ArrayList<MBranch> list = new ArrayList<>();
        while (rst.next()) {
            MBranch branch = new MBranch();
            branch.setIndexNo(rst.getInt(1));
            branch.setBranchCode(rst.getString(2));
            branch.setRegNumber(rst.getString(3));
            branch.setName(rst.getString(4));
            branch.setAddressLine1(rst.getString(5));
            branch.setAddressLine2(rst.getString(6));
            branch.setAddressLine3(rst.getString(7));
            branch.setTelephoneNumber(rst.getString(8));
            branch.setColor(rst.getString(9));
            branch.setType(rst.getString(10));
            list.add(branch);
        }
        return list;
    }

    public int saveReOrderLevel(Integer item, Integer branch, BigDecimal reorderMax, BigDecimal reorderMin, Connection connection) throws SQLException {
        String insertSql = "insert into m_re_order_level (item,branch,re_order_max,re_order_min)\n"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, item);
        preparedStatement.setInt(2, branch);
        preparedStatement.setBigDecimal(3, reorderMax);
        preparedStatement.setBigDecimal(4, reorderMin);
        return preparedStatement.executeUpdate();

    }

    public Integer findStock(Integer branch, String type, Connection connection) throws SQLException {
        String query = "select m_store.index_no from m_store\n"
                + "where m_store.branch =? and m_store.`type`=? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        preparedStatement.setString(2, type);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer saveStock(Integer branch, String type, Connection connection) throws SQLException {
        String insertSql = "insert into m_store (name,number,type,branch)\n"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, type);
        preparedStatement.setInt(2, 1);
        preparedStatement.setString(3, type);
        preparedStatement.setInt(4, branch);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public int saveStockLedger(GrnDetail detail, Grn grn, Integer saveStock, Connection accConnection) throws SQLException {
        Integer nextNo = nextStockLedgerGroupNumber(accConnection);
        if (nextNo <= 0) {
            throw new RuntimeException("Stock ledger next group number genarate fail !");
        }
        String insertSql = "insert into t_stock_ledger (item,store,date,in_qty,out_qty,avarage_price_in,avarage_price_out,\n"
                + "form_index_no,form,branch,type,group_number)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(detail.getItemNo()));
        preparedStatement.setInt(2, saveStock);
        preparedStatement.setString(3, grn.getEnterDate());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, new BigDecimal(0));
        preparedStatement.setBigDecimal(6, detail.getCost());
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, detail.getGrn());
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(10, grn.getBranch());
        preparedStatement.setString(11, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(12, nextNo);

        return preparedStatement.executeUpdate();
    }

    public void updateSupplierMaster(Grn grn, HashMap<Integer, Integer> supplierMap, Connection connection) throws SQLException {
        String insertSql = "UPDATE m_supplier set name=? , contact_name=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, grn.getSupName());
        preparedStatement.setString(2, grn.getSupName());
        preparedStatement.setInt(3, supplierMap.get(2));

        preparedStatement.executeUpdate();
    }

    public void updateSupplierAccount(Grn grn, HashMap<Integer, Integer> supplierMap, Connection connection) throws SQLException {
        String insertSql = "UPDATE m_acc_account set name=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, grn.getSupName());
        preparedStatement.setInt(2, supplierMap.get(1));

        preparedStatement.executeUpdate();
    }

    public void updateItemMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_item set name=? , print_description=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setInt(3, map.get(2));

        preparedStatement.executeUpdate();
    }

    public void updateItemUnitMaster(GrnDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_item_units set name=?\n"
                + "WHERE item_unit_type=? and item=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, Constant.ITEM_UNIT_MAIN);
        preparedStatement.setInt(3, map.get(2));

        preparedStatement.executeUpdate();
    }

    public Integer saveSupplierLedger(Grn grn, Integer grnIndex, Integer supplierIndex, Connection connection) throws SQLException {
        String insertSql = "insert into t_supplier_ledger (branch,date,form_name,grn,supplier,credit_amount,debit_amount,\n"
                + "ref_number,is_delete)\n"
                + " values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, grn.getBranch());
        preparedStatement.setString(2, grn.getEnterDate());
        preparedStatement.setString(3, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(4, grnIndex);
        preparedStatement.setInt(5, supplierIndex);
        preparedStatement.setBigDecimal(6, grn.getFinalValue());
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, grnIndex);
        preparedStatement.setBoolean(9, false);

        return preparedStatement.executeUpdate();
    }

    public HashMap<Integer, Object> saveSupplierAccountLedger(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, Integer user, Connection accConnection) throws SQLException {
//        get branch
        MBranch branch = getBranch(grn.getBranch(), accConnection);
        if (branch.getName() == null) {
            throw new RuntimeException("Can't find Branch ! Branch code is " + grn.getBranch());
        }

//        getNumber
        Integer nextNumber = getNextNumber(grn.getBranch(), Constant.SYSTEM_INTEGRATION_GRN, accConnection);
        if (nextNumber < 0) {
            throw new RuntimeException("Next number generate fail !");
        }

//        getDeleteFrfNumber
        Integer deleteRefNumber = getDeleteRefNumber(accConnection);
        if (deleteRefNumber < 0) {
            throw new RuntimeException("Delete Ref number generate fail !");
        }

        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, nextNumber);
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_GRN + "/" + branch.getBranchCode() + "/" + nextNumber);
        preparedStatement.setString(3, grn.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, grn.getBranch());
        preparedStatement.setInt(7, grn.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, new BigDecimal(0));
        preparedStatement.setBigDecimal(10, grn.getFinalValue());
        preparedStatement.setInt(11, supplierMap.get(1));
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setString(13, grnIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(15, grnIndex);
        preparedStatement.setInt(16, deleteRefNumber);
        preparedStatement.setString(17, "System Integration GRN");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, true);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            updateReConcileGroup(resultSet.getInt(1), accConnection);
            HashMap<Integer, Object> map = new HashMap<>();
            map.put(1, nextNumber);
            map.put(2, deleteRefNumber);
            map.put(3, resultSet.getInt(1));
            map.put(4, branch.getBranchCode());
            map.put(5, grn.getBranch());
            map.put(6, grn.getEnterDate());
            return map;
        }
        throw new RuntimeException("Supplier Ledger Account Save Fail !");
    }

    public static MBranch getBranch(int branch, Connection connection) throws SQLException {
        String query = "select m_branch.* from m_branch where m_branch.index_no=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        ResultSet rst = preparedStatement.executeQuery();
        MBranch branchModel = new MBranch();
        if (rst.next()) {
            branchModel.setIndexNo(rst.getInt(1));
            branchModel.setBranchCode(rst.getString(2));
            branchModel.setRegNumber(rst.getString(3));
            branchModel.setName(rst.getString(4));
            branchModel.setAddressLine1(rst.getString(5));
            branchModel.setAddressLine2(rst.getString(6));
            branchModel.setAddressLine3(rst.getString(7));
            branchModel.setTelephoneNumber(rst.getString(8));
            branchModel.setColor(rst.getString(9));
            branchModel.setType(rst.getString(10));
        }
        return branchModel;
    }

    private static Integer getNextNumber(int branch, String type, Connection connection) throws SQLException {
        String query = "select ifnull( max(t_acc_ledger.number)+1,1) as number\n"
                + "from t_acc_ledger\n"
                + "where t_acc_ledger.current_branch=? and t_acc_ledger.`type` =?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        preparedStatement.setString(2, type);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private static Integer getDeleteRefNumber(Connection connection) throws SQLException {
        String query = "select ifnull( max(t_acc_ledger.delete_ref_no)+1,1) as number\n"
                + "from t_acc_ledger";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private void updateReConcileGroup(int reconcileGroup, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE t_acc_ledger set reconcile_group=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, reconcileGroup);
        preparedStatement.setInt(2, reconcileGroup);

        preparedStatement.executeUpdate();
    }

    public Integer saveAccLedgerItem(GrnDetail detail, int branch, HashMap<Integer, Integer> map, HashMap<Integer, Object> ledgerMap, Integer user, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_GRN + "/" + ledgerMap.get(4).toString() + "/" + Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setString(3, ledgerMap.get(6).toString());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, Integer.parseInt(ledgerMap.get(5).toString()));
        preparedStatement.setInt(7, Integer.parseInt(ledgerMap.get(5).toString()));
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, detail.getNetValue());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, map.get(1));
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setString(13, map.get(2) + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(15, Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setInt(16, Integer.parseInt(ledgerMap.get(2).toString()));
        preparedStatement.setString(17, "System Integration GRN save Item ");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            updateReConcileGroup(resultSet.getInt(1), accConnection);
            return resultSet.getInt(1);
        }
        throw new RuntimeException("Supplier Ledger Account Save Fail !");
    }

    private Integer saveAccLedgerVatAndNbt(Grn grn, HashMap<Integer, Integer> supplierMap, Integer grnIndex, String description, Integer vatAccount, HashMap<Integer, Object> ledgerMap, Integer user, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_GRN + "/" + ledgerMap.get(4).toString() + "/" + Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setString(3, grn.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, Integer.parseInt(ledgerMap.get(5).toString()));
        preparedStatement.setInt(7, Integer.parseInt(ledgerMap.get(5).toString()));
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, grn.getNbtValue());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, vatAccount);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setString(13, grnIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_GRN);
        preparedStatement.setInt(15, Integer.parseInt(ledgerMap.get(1).toString()));
        preparedStatement.setInt(16, Integer.parseInt(ledgerMap.get(2).toString()));
        preparedStatement.setString(17, description);
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            updateReConcileGroup(resultSet.getInt(1), accConnection);
            return resultSet.getInt(1);
        }
        throw new RuntimeException("Supplier Ledger Account Save Fail !");
    }

    public TTypeIndexDetail CheckTypeIndexDetail(String type, String typeIndex, Connection operaConnection) throws SQLException {
        String query = "select t_type_index_detail.*\n"
                + "from t_type_index_detail\n"
                + "WHERE t_type_index_detail.master_ref_id = ? and t_type_index_detail.`type` = ?";
        PreparedStatement preparedStatement = operaConnection.prepareStatement(query);
        preparedStatement.setString(1, typeIndex);
        preparedStatement.setString(2, type);
        ResultSet resultSet = preparedStatement.executeQuery();
        TTypeIndexDetail typeIndexDetail = new TTypeIndexDetail();
        if (resultSet.next()) {
            typeIndexDetail.setAccountIndex(resultSet.getInt(1));
            typeIndexDetail.setMasterRefId(resultSet.getString(2));
            typeIndexDetail.setType(resultSet.getString(3));
            typeIndexDetail.setAccountRefId(resultSet.getInt(4));
            typeIndexDetail.setAccountIndex(resultSet.getInt(5));
        }
        return typeIndexDetail;
    }

    public Integer saveTypeIndexDetail(TTypeIndexDetail tTypeIndexDetail, Connection operaConnection) throws SQLException {

        String sql = "insert into t_type_index_detail (master_ref_id,type,account_ref_id,account_index)"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = operaConnection.prepareStatement(sql);
        preparedStatement.setString(1, tTypeIndexDetail.getMasterRefId());
        preparedStatement.setString(2, tTypeIndexDetail.getType());
        preparedStatement.setInt(3, tTypeIndexDetail.getAccountRefId());
        preparedStatement.setInt(4, tTypeIndexDetail.getAccountIndex());
        int save = preparedStatement.executeUpdate();

        return save;
    }

    public Integer saveCustomerMaster(MClient client, Connection accConnection) throws SQLException {
        String insertSql = "insert into m_client (acc_account,name,branch,is_new,date,resident)\n"
                + " values (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, client.getAccAccount());
        preparedStatement.setString(2, client.getName());
        preparedStatement.setInt(3, client.getBranch());
        preparedStatement.setBoolean(4, true);
        preparedStatement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(6, client.getResident());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public void updateCustomerMaster(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_client set name=?  \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, invoice.getClientName());
        preparedStatement.setInt(2, customerMap.get(2));

        preparedStatement.executeUpdate();
    }

    public void updateCustomerAccount(Invoice invoice, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_acc_account set name=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, invoice.getClientName());
        preparedStatement.setInt(2, customerMap.get(1));

        preparedStatement.executeUpdate();
    }

    public HashMap<Integer, Integer> saveInvoice(Invoice invoice, List<InvoiceDetail> invDetailList, HashMap<Integer, Integer> customerMap, Integer vehicle, Integer user, Connection accConnection) throws SQLException {
        HashMap<Integer, Integer> map = new HashMap<>();
        Integer jobIndex = saveJobCard(invoice, customerMap, vehicle, accConnection);
        if (jobIndex <= 0) {
            throw new RuntimeException("Job Card Save fail !");
        }
        Integer invIndex = saveInvoiceMaster(invoice, jobIndex, customerMap, accConnection);
        if (invIndex <= 0) {
            throw new RuntimeException("Inoice Save fail !");
        }
        Integer customerLedgerIndex = saveCustomerLedger(invoice, invIndex, customerMap, accConnection);
        if (customerLedgerIndex <= 0) {
            throw new RuntimeException("Customer Ledger Save fail !");
        }
        Integer customerAccIndex = saveInvoiceToAccount(invoice, invDetailList, jobIndex, invIndex, customerMap, user, accConnection);
        map.put(1, invIndex);
        map.put(2, customerAccIndex);
        map.put(3, jobIndex);
        return map;
    }

    public Integer saveVehicleMaster(Invoice invoice, Integer customer, Connection accConnection) throws SQLException {
        String insertSql = "insert into m_vehicle (client,vehicle_no,type,date)\n"
                + " values (?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, customer);
        preparedStatement.setString(2, invoice.getVehicleNo());
        preparedStatement.setString(3, invoice.getVehicleType());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    private Integer saveJobCard(Invoice invoice, HashMap<Integer, Integer> customerMap, Integer vehicle, Connection accConnection) throws SQLException {
        Integer nextJobNo = nextJobNumber(invoice.getBranch(), accConnection);
        if (nextJobNo <= 0) {
            throw new RuntimeException("Job Number Genarate Fail !");
        }
        String insertSql = "insert into t_job_card (vehicle,number,branch,client,date)\n"
                + " values (?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, vehicle == null ? null : vehicle + "");
        preparedStatement.setInt(2, nextJobNo);
        preparedStatement.setInt(3, invoice.getBranch());
        preparedStatement.setInt(4, customerMap.get(2));
        preparedStatement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    private Integer nextJobNumber(Integer branch, Connection accConnection) throws SQLException {
        String query = "select ifnull( max(t_job_card.number)+1,1) as number\n"
                + "from t_job_card\n"
                + "where t_job_card.branch =? ";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private Integer saveInvoiceMaster(Invoice invoice, Integer jobIndex, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        Integer nextInvoiceNo = nextInvoiceNumber(invoice.getBranch(), accConnection);
        String insertSql = "insert into t_invoice (job_card,date,number,amount,discount_rate,discount_amount,net_amount,\n"
                + "nbt_rate,nbt_value,vat_rate,vat_value,final_value,branch,status)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, jobIndex);
        preparedStatement.setString(2, invoice.getEnterDate());
        preparedStatement.setInt(3, nextInvoiceNo);
        preparedStatement.setBigDecimal(4, invoice.getAmount());
        preparedStatement.setBigDecimal(5, invoice.getDiscountRate());
        preparedStatement.setBigDecimal(6, invoice.getDiscountAmount());
        preparedStatement.setBigDecimal(7, invoice.getNetAmount());
        preparedStatement.setBigDecimal(8, invoice.getNbtRate());
        preparedStatement.setBigDecimal(9, invoice.getNbtValue());
        preparedStatement.setBigDecimal(10, invoice.getVatRate());
        preparedStatement.setBigDecimal(11, invoice.getVatValue());
        preparedStatement.setBigDecimal(12, invoice.getFinalValue());
        preparedStatement.setInt(13, invoice.getBranch());
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_INVOICE);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    private Integer nextInvoiceNumber(Integer branch, Connection accConnection) throws SQLException {
        String query = "select ifnull( max(t_invoice.number)+1,1) as number\n"
                + "from t_invoice\n"
                + "where t_invoice.branch =? ";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setInt(1, branch);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private Integer saveCustomerLedger(Invoice invoice, Integer invIndex, HashMap<Integer, Integer> customerMap, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_customer_ledger (invoice,date,debit_amount,credit_amount,type,client,ref_number,form_name)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, invIndex);
        preparedStatement.setString(2, invoice.getEnterDate());
        preparedStatement.setBigDecimal(3, invoice.getFinalValue());
        preparedStatement.setBigDecimal(4, new BigDecimal(0));
        preparedStatement.setString(5, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(6, customerMap.get(2));
        preparedStatement.setInt(7, invIndex);
        preparedStatement.setString(8, Constant.SYSTEM_INTEGRATION);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    private Integer saveInvoiceToAccount(Invoice invoice, List<InvoiceDetail> invDetailList, Integer jobIndex, Integer invIndex, HashMap<Integer, Integer> customerMap, Integer user, Connection accConnection) throws SQLException {
//        get branch
        MBranch branch = getBranch(invoice.getBranch(), accConnection);
        if (branch == null || branch.getBranchCode() == null) {
            throw new RuntimeException("Can't find Branch ! Branch Code is" + invoice.getBranch());
        }

//        getNumber
        Integer nextNumber = getNextNumber(invoice.getBranch(), Constant.SYSTEM_INTEGRATION_INVOICE, accConnection);
        if (nextNumber < 0) {
            throw new RuntimeException("Next number generate fail !");
        }

//        getDeleteFrfNumber
        Integer deleteRefNumber = getDeleteRefNumber(accConnection);
        if (deleteRefNumber < 0) {
            throw new RuntimeException("Delete Ref number generate fail !");
        }

        //save customer account
        Integer customerAccIndex = saveInvoiceToAccountWithCustomer(invoice, invIndex, customerMap, branch, nextNumber, deleteRefNumber, user, accConnection);
        if (customerAccIndex < 0) {
            throw new RuntimeException("Invoice To Account With Customer Save fail !");
        }

        //save discount
        if (invoice.getDiscountAmount().doubleValue() > 0) {
            Integer discountAccIndex = saveInvoiceToAccountWithDiscount(invoice, invIndex, customerMap, branch, nextNumber, deleteRefNumber, user, accConnection);
            if (discountAccIndex < 0) {
                throw new RuntimeException("Invoice To Account With Discount Save fail !");
            }
        }
        //save NBT
        if (invoice.getNbtValue().doubleValue() > 0) {
            Integer NBTIndex = saveInvoiceToAccountWithNBT(invoice, invIndex, customerMap, branch, nextNumber, deleteRefNumber, user, accConnection);
            if (NBTIndex < 0) {
                throw new RuntimeException("NBT Save fail !");
            }
        }
        //save VAT
        if (invoice.getVatValue().doubleValue() > 0) {
            Integer VATIndex = saveInvoiceToAccountWithVAT(invoice, invIndex, customerMap, branch, nextNumber, deleteRefNumber, user, accConnection);
            if (VATIndex < 0) {
                throw new RuntimeException("VAT Save fail !");
            }
        }

        //save sales income
        Integer salesAccIndex = saveInvoiceToAccountWithSales(invoice, invDetailList, invIndex, customerMap, branch, nextNumber, deleteRefNumber, user, accConnection);
        if (salesAccIndex < 0) {
            throw new RuntimeException("Invoice To Account With Item Sales Save fail !");
        }
        return customerAccIndex;
    }

    private Integer saveInvoiceToAccountWithCustomer(Invoice invoice, Integer invIndex, HashMap<Integer, Integer> customerMap, MBranch branch, Integer nextNumber, Integer deleteRefNumber, Integer user, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, nextNumber);
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_INVOICE + "/" + branch.getBranchCode() + "/" + nextNumber);
        preparedStatement.setString(3, invoice.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, invoice.getBranch());
        preparedStatement.setInt(7, invoice.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, invoice.getFinalValue());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, customerMap.get(1));
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setString(13, invIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(15, invIndex);
        preparedStatement.setInt(16, deleteRefNumber);
        preparedStatement.setString(17, "System Integration Invoice save Customer Account");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            updateReConcileGroup(resultSet.getInt(1), accConnection);
            return resultSet.getInt(1);
        }
        throw new RuntimeException("Invoice To Account With Customer Save fail !");
    }

    private Integer saveInvoiceToAccountWithDiscount(Invoice invoice, Integer invIndex, HashMap<Integer, Integer> customerMap, MBranch branch, Integer nextNumber, Integer deleteRefNumber, Integer user, Connection accConnection) throws SQLException {
        Integer discountAccount = getSubAccountOf(Constant.ITEM_DISCOUNT_OUT, accConnection);
        if (discountAccount <= 0) {
            throw new RuntimeException("Item Discount Out Account not found !");
        }
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, nextNumber);
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_INVOICE + "/" + branch.getBranchCode() + "/" + nextNumber);
        preparedStatement.setString(3, invoice.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, invoice.getBranch());
        preparedStatement.setInt(7, invoice.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, invoice.getDiscountAmount());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, discountAccount);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setString(13, invIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(15, invIndex);
        preparedStatement.setInt(16, deleteRefNumber);
        preparedStatement.setString(17, "System Integration Invoice save Discount");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        return preparedStatement.executeUpdate();
    }

    private Integer saveInvoiceToAccountWithSales(Invoice invoice, List<InvoiceDetail> invDetailList, Integer invIndex, HashMap<Integer, Integer> customerMap, MBranch branch, Integer nextNumber, Integer deleteRefNumber, Integer user, Connection accConnection) throws SQLException {
        for (InvoiceDetail invoiceDetail : invDetailList) {
            Integer itemSalesIncome = -1;
            if ("SERVICE".equals(invoiceDetail.getItemType())) {
                itemSalesIncome = getSubAccountOf(Constant.SERVICE_SALES_INCOME, accConnection);
            }
            if ("STOCK".equals(invoiceDetail.getItemType()) || "NON_STOCK".equals(invoiceDetail.getItemType())) {
                itemSalesIncome = getSubAccountOf(Constant.ITEM_SALES_INCOME, accConnection);
            }
            if (itemSalesIncome <= 0) {
                throw new RuntimeException("Item Sales Income Account not found !");
            }
            String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                    + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                    + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
            preparedStatement.setInt(1, nextNumber);
            preparedStatement.setString(2, Constant.CODE_INTEGRATION_INVOICE + "/" + branch.getBranchCode() + "/" + nextNumber);
            preparedStatement.setString(3, invoice.getEnterDate());
            preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
            preparedStatement.setInt(6, invoice.getBranch());
            preparedStatement.setInt(7, invoice.getBranch());
            preparedStatement.setInt(8, user);
            preparedStatement.setBigDecimal(9, new BigDecimal(0));
            preparedStatement.setBigDecimal(10, invoiceDetail.getValue());
            preparedStatement.setInt(11, itemSalesIncome);
            preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_INVOICE);
            preparedStatement.setString(13, invIndex + "");
            preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_INVOICE);
            preparedStatement.setInt(15, invIndex);
            preparedStatement.setInt(16, deleteRefNumber);
            preparedStatement.setString(17, "System Integration Invoice save Item Sales ");
            preparedStatement.setString(18, null);
            preparedStatement.setBoolean(19, false);
            preparedStatement.setBoolean(20, false);
            preparedStatement.setBoolean(21, false);

            preparedStatement.executeUpdate();
        }
        return 1;

    }

    private Integer saveInvoiceToAccountWithNBT(Invoice invoice, Integer invIndex, HashMap<Integer, Integer> customerMap, MBranch branch, Integer nextNumber, Integer deleteRefNumber, Integer user, Connection accConnection) throws SQLException {
        Integer nbtAccountIn = getSubAccountOf(Constant.NBT_ACCOUNT_IN, accConnection);
        if (nbtAccountIn <= 0) {
            throw new RuntimeException("NBT Account setting not found !");
        }
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, nextNumber);
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_INVOICE + "/" + branch.getBranchCode() + "/" + nextNumber);
        preparedStatement.setString(3, invoice.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, invoice.getBranch());
        preparedStatement.setInt(7, invoice.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, new BigDecimal(0));
        preparedStatement.setBigDecimal(10, invoice.getNbtValue());
        preparedStatement.setInt(11, nbtAccountIn);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setString(13, invIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(15, invIndex);
        preparedStatement.setInt(16, deleteRefNumber);
        preparedStatement.setString(17, "System Integration Invoice save NBT ");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        return preparedStatement.executeUpdate();
    }

    private Integer saveInvoiceToAccountWithVAT(Invoice invoice, Integer invIndex, HashMap<Integer, Integer> customerMap, MBranch branch, Integer nextNumber, Integer deleteRefNumber, Integer user, Connection accConnection) throws SQLException {
        Integer vatAccountIn = getSubAccountOf(Constant.VAT_ACCOUNT_IN, accConnection);
        if (vatAccountIn <= 0) {
            throw new RuntimeException("VAT Account setting not found !");
        }
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, nextNumber);
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_INVOICE + "/" + branch.getBranchCode() + "/" + nextNumber);
        preparedStatement.setString(3, invoice.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, invoice.getBranch());
        preparedStatement.setInt(7, invoice.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, new BigDecimal(0));
        preparedStatement.setBigDecimal(10, invoice.getVatValue());
        preparedStatement.setInt(11, vatAccountIn);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setString(13, invIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(15, invIndex);
        preparedStatement.setInt(16, deleteRefNumber);
        preparedStatement.setString(17, "System Integration Invoice save VAT");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        return preparedStatement.executeUpdate();
    }

    public void updateItemMaster(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_item set name=? , print_description=? \n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setInt(3, map.get(2));

        preparedStatement.executeUpdate();
    }

    public void updateItemUnitMaster(InvoiceDetail detail, HashMap<Integer, Integer> map, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE m_item_units set name=?\n"
                + "WHERE item_unit_type=? and item=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, Constant.ITEM_UNIT_MAIN);
        preparedStatement.setInt(3, map.get(2));

        preparedStatement.executeUpdate();
    }

    public Integer saveItemMaster(InvoiceDetail detail, Integer accAccountIndex, Connection connection) throws SQLException {
        String insertSql = "insert into m_item (name,barcode,print_description,cost_price,\n"
                + "type,re_order_max,re_order_min,account,sale_price_normal,sale_price_register,unit)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getItemBarcode());
        preparedStatement.setString(3, detail.getItemName());
        preparedStatement.setBigDecimal(4, detail.getCost());
        preparedStatement.setString(5, detail.getItemType());
        preparedStatement.setBigDecimal(6, new BigDecimal(0));
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, accAccountIndex);
        preparedStatement.setBigDecimal(9, detail.getSalesPrice());
        preparedStatement.setBigDecimal(10, detail.getSalesPrice());
        preparedStatement.setString(11, detail.getItemUnit());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveItemUnitMaster(InvoiceDetail detail, Integer saveItemMaster, Connection connection) throws SQLException {
        String insertSql = "insert into m_item_units (item,name,unit,qty,sale_price_normal,sale_price_register,cost_price,item_unit_type)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, saveItemMaster);
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setString(3, detail.getItemUnit());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, detail.getSalesPrice());
        preparedStatement.setBigDecimal(6, detail.getSalesPrice());
        preparedStatement.setBigDecimal(7, detail.getCost());
        preparedStatement.setString(8, Constant.ITEM_UNIT_MAIN);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public void saveInvoiceDetail(InvoiceDetail detail, Invoice invoice, HashMap<Integer, Integer> itemMap, HashMap<Integer, Integer> invoiceMap, Integer store, Connection accConnection) throws SQLException {
        Integer detailIndex = saveJobDetail(detail, invoice, itemMap, invoiceMap, accConnection);
        if (detailIndex <= 0) {
            throw new RuntimeException("Invoice Detail Save Fail !");
        }
        if (!detail.getItemType().equals(Constant.ITEM_SERVICE)) {
            Integer stockLedgerIndex = saveStockLedger(detail, invoice, itemMap, invoiceMap, store, accConnection);
            if (stockLedgerIndex <= 0) {
                throw new RuntimeException("Invoice Detail Save Fail !");
            }
        }

    }

    private Integer saveJobDetail(InvoiceDetail detail, Invoice invoice, HashMap<Integer, Integer> itemMap, HashMap<Integer, Integer> invoiceMap, Connection accConnection) throws SQLException {
        Integer itemUnit = -1;
        if (itemMap.get(3) == null) {
            itemUnit = getItemUnit(itemMap.get(2), accConnection);
        } else {
            itemUnit = itemMap.get(3);
        }
        if (itemUnit <= 0 || itemUnit == null) {
            throw new RuntimeException("Item Unit Can't find !");
        }
        String insertSql = "insert into t_job_item (job_card,item,item_unit,item_name,\n"
                + "item_type,quantity,stock_remove_qty,price,value,cost_price)\n"
                + " values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, invoiceMap.get(3));
        preparedStatement.setInt(2, itemMap.get(2));
        preparedStatement.setInt(3, itemUnit);
        preparedStatement.setString(4, detail.getItemName());
        preparedStatement.setString(5, detail.getItemType());
        preparedStatement.setBigDecimal(6, detail.getQty());
        preparedStatement.setBigDecimal(7, detail.getStockRemoveQty());
        preparedStatement.setBigDecimal(8, detail.getSalesPrice());
        preparedStatement.setBigDecimal(9, detail.getValue());
        preparedStatement.setBigDecimal(10, detail.getCost());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    private Integer saveStockLedger(InvoiceDetail detail, Invoice invoice, HashMap<Integer, Integer> itemMap, HashMap<Integer, Integer> invoiceMap, Integer store, Connection accConnection) throws SQLException {
        List<TStockLedger> stockLedgerList = getFifoList(itemMap.get(2), invoice.getEnterDate(), invoice.getBranch(), store, accConnection);
        if (stockLedgerList.size() <= 0) {
            throw new RuntimeException("Empty stock Qty for this item (" + detail.getItemNo() + " - " + itemMap.get(2) + " - " + detail.getItemName() + ")and " + invoice.getEnterDate() + " !");
        }
        Double invQty = detail.getStockRemoveQty().doubleValue();
        for (TStockLedger tStockLedger : stockLedgerList) {
            if (invQty > tStockLedger.getInQty().doubleValue()) {
                //save tStockLedger total
                Integer saveT = saveStockLedgerFromInvoice(invoice, itemMap, invoiceMap, tStockLedger.getInQty(), tStockLedger.getAvaragePriceIn(), tStockLedger.getGroupNumber(), store, accConnection);
                if (saveT >= 0) {
                    invQty -= tStockLedger.getInQty().doubleValue();
                }
            } else if (invQty <= tStockLedger.getInQty().doubleValue()) {
                //save inv qty total
                Integer saveQ = saveStockLedgerFromInvoice(invoice, itemMap, invoiceMap, new BigDecimal(invQty), tStockLedger.getAvaragePriceIn(), tStockLedger.getGroupNumber(), store, accConnection);

                return saveQ;
            }
        }
        throw new RuntimeException("Not enough stock qty for this item (" + itemMap.get(2) + " - " + detail.getItemName() + ") !");
    }

    private Integer getItemUnit(Integer item, Connection accConnection) throws SQLException {
        String query = "select m_item_units.index_no\n"
                + "from m_item_units\n"
                + "WHERE m_item_units.item=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setInt(1, item);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private Integer nextStockLedgerGroupNumber(Connection accConnection) throws SQLException {
        String query = "select ifnull( max(t_stock_ledger.group_number)+1,1) as number\n"
                + "from t_stock_ledger";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    private List<TStockLedger> getFifoList(Integer item, String enterDate, Integer branch, Integer store, Connection accConnection) throws SQLException {
        String query = "select \n"
                + "   (sum(t_stock_ledger.in_qty)-\n"
                + "   sum(t_stock_ledger.out_qty)) as qty,\n"
                + "   t_stock_ledger.avarage_price_in as unit_cost,\n"
                + "   t_stock_ledger.group_number\n"
                + "from t_stock_ledger\n"
                + "where t_stock_ledger.item=? and t_stock_ledger.date<=?\n"
                + "   and t_stock_ledger.branch =? and t_stock_ledger.store=?\n"
                + "group by t_stock_ledger.group_number\n"
                + "HAVING qty>0";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setInt(1, item);
        preparedStatement.setString(2, enterDate);
        preparedStatement.setInt(3, branch);
        preparedStatement.setInt(4, store);
        ResultSet rst = preparedStatement.executeQuery();
        List<TStockLedger> stockLedgerList = new ArrayList<>();
        while (rst.next()) {
            TStockLedger stockLedger = new TStockLedger(rst.getBigDecimal(1), rst.getBigDecimal(2), rst.getInt(3));
            stockLedgerList.add(stockLedger);
        }
        return stockLedgerList;
    }

    private Integer saveStockLedgerFromInvoice(Invoice invoice, HashMap<Integer, Integer> itemMap, HashMap<Integer, Integer> invoiceMap, BigDecimal qty, BigDecimal cost, int groupNumber, Integer store, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_stock_ledger (item,store,date,in_qty,out_qty,avarage_price_in,avarage_price_out,\n"
                + "form_index_no,form,branch,type,group_number)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, itemMap.get(2));
        preparedStatement.setInt(2, store);
        preparedStatement.setString(3, invoice.getEnterDate());
        preparedStatement.setBigDecimal(4, new BigDecimal(0));
        preparedStatement.setBigDecimal(5, qty);
        preparedStatement.setBigDecimal(6, new BigDecimal(0));
        preparedStatement.setBigDecimal(7, cost);
        preparedStatement.setInt(8, invoiceMap.get(1));
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(10, invoice.getBranch());
        preparedStatement.setString(11, Constant.SYSTEM_INTEGRATION_INVOICE);
        preparedStatement.setInt(12, groupNumber);

        return preparedStatement.executeUpdate();
    }

    public Integer savePayment(Payment payment, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_payment (number,total_amount,cash_amount,cheque_amount,card_amount,over_payment_amount,is_down_payment)\n"
                + " values (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, payment.getNumber());
        preparedStatement.setBigDecimal(2, payment.getTotalAmount());
        preparedStatement.setBigDecimal(3, payment.getCashAmount());
        preparedStatement.setBigDecimal(4, payment.getChequeAmount());
        preparedStatement.setBigDecimal(5, payment.getCardAmount());
        preparedStatement.setBigDecimal(6, payment.getOverPaymentAmount());
        preparedStatement.setBoolean(7, payment.getIsDownPayment());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveCustomerLedger(PaymentDetail paymentDetail1, Integer paymentIndex, Payment payment, TTypeIndexDetail invTypeIndexDetail, TTypeIndexDetail customerTypeIndexDetail, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_customer_ledger (invoice,payment,`date`,debit_amount,credit_amount,`type`,\n"
                + "`client`,ref_number,form_name)\n"
                + " values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, invTypeIndexDetail.getAccountRefId());
        preparedStatement.setInt(2, paymentIndex);
        preparedStatement.setString(3, payment.getEnterDate());
        preparedStatement.setBigDecimal(4, new BigDecimal(0));
        preparedStatement.setBigDecimal(5, paymentDetail1.getAmount());
        preparedStatement.setString(6, Constant.SYSTEM_INTEGRATION_PAYMENT);
        preparedStatement.setInt(7, customerTypeIndexDetail.getAccountIndex());
        preparedStatement.setInt(8, invTypeIndexDetail.getAccountRefId());
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION);

        return preparedStatement.executeUpdate();
    }

    public Integer saveAccountLedgerCustomer(PaymentDetail paymentDetail1, Integer paymentIndex, Payment payment, TTypeIndexDetail invTypeIndexDetail, TTypeIndexDetail customerTypeIndexDetail, HashMap<Integer, Object> numberMap, Integer user, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque,reconcile_group)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_PAYMENT + "/" + numberMap.get(1).toString() + "/" + Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(3, payment.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, payment.getBranch());
        preparedStatement.setInt(7, payment.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, new BigDecimal(0));
        preparedStatement.setBigDecimal(10, paymentDetail1.getAmount());
        preparedStatement.setInt(11, customerTypeIndexDetail.getAccountRefId());
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_PAYMENT);
        preparedStatement.setString(13, paymentIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_PAYMENT);
        preparedStatement.setInt(15, paymentIndex);
        preparedStatement.setInt(16, Integer.parseInt(numberMap.get(3).toString()));
        preparedStatement.setString(17, "System Integration Payment save to customer");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);
        preparedStatement.setInt(22, invTypeIndexDetail.getAccountRefId());

        return preparedStatement.executeUpdate();
    }

    public Integer savePaymentInformation(PaymentInformation paymentInformation, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_payment_information (payment,number,cheque_date,amount,type,\n"
                + "form_name,bank,bank_branch,card_type,card_reader)\n"
                + " values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, paymentInformation.getPayment());
        preparedStatement.setString(2, paymentInformation.getNumber());
        preparedStatement.setString(3, paymentInformation.getChequeDate());
        preparedStatement.setBigDecimal(4, paymentInformation.getAmount());
        preparedStatement.setString(5, paymentInformation.getType());
        preparedStatement.setString(6, Constant.SYSTEM_INTEGRATION_PAYMENT);
        preparedStatement.setString(7, paymentInformation.getBank());
        preparedStatement.setString(8, paymentInformation.getBankBranch());
        preparedStatement.setString(9, paymentInformation.getCardType());
        preparedStatement.setString(10, paymentInformation.getCardReader());

        return preparedStatement.executeUpdate();
    }

    public Integer savePaymentAccLedger(PaymentInformation paymentInformation, Integer paymentIndex, Payment payment, TTypeIndexDetail customerTypeIndexDetail, HashMap<Integer, Object> numberMap, Integer user, Connection accConnection) throws SQLException {
        Integer account = -1;

        if (paymentInformation.getType().equals(Constant.PAYMENT_CASH)) {
            account = getSubAccountOf(Constant.ITEM_SALES_CASH_IN, accConnection);

        } else if (paymentInformation.getType().equals(Constant.PAYMENT_CHEQUE)) {
            account = getSubAccountOf(Constant.CHEQUE_IN_HAND, accConnection);

        } else if (paymentInformation.getType().equals(Constant.PAYMENT_CARD)) {
            if (paymentInformation.getCardReader() == null) {
                throw new RuntimeException("Card Reader Number is empty !");
            }
            account = getCardReaderAccont(paymentInformation.getCardReader(), payment.getBranch(), accConnection);

        } else {
            throw new RuntimeException("Payment type is Invalided. Available types are CASH,CHEQUE,CARD !");
        }
        if (account <= 0) {
            throw new RuntimeException("Can't find Account setting for type '" + paymentInformation.getType() + "'");
        }

        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_PAYMENT + "/" + numberMap.get(1).toString() + "/" + Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(3, payment.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, payment.getBranch());
        preparedStatement.setInt(7, payment.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, paymentInformation.getAmount());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, account);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_PAYMENT);
        preparedStatement.setString(13, paymentIndex + "");
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_PAYMENT);
        preparedStatement.setInt(15, paymentIndex);
        preparedStatement.setInt(16, Integer.parseInt(numberMap.get(3).toString()));
        preparedStatement.setString(17, "System Integration Payment Information save !");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        return preparedStatement.executeUpdate();
    }

    private Integer getCardReaderAccont(String cardReader, Integer branch, Connection accConnection) throws SQLException {
        String query = "select m_card_reader.acc_account\n"
                + "from m_card_reader\n"
                + "WHERE m_card_reader.number=? and m_card_reader.branch=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setString(1, cardReader);
        preparedStatement.setInt(2, branch);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public HashMap<Integer, Integer> checkBankAndBankBranch(PaymentInformation paymentInformation, Connection accConnection) throws SQLException {
        String query = "select m_bank.index_no as bank_index,\n"
                + "	m_bank_branch.index_no as bank_branch_index\n"
                + "from m_bank\n"
                + "LEFT JOIN m_bank_branch on m_bank_branch.bank=m_bank.index_no\n"
                + "where m_bank.name=? and m_bank_branch.name=?\n"
                + "LIMIT 1";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setString(1, paymentInformation.getBank());
        preparedStatement.setString(2, paymentInformation.getBankBranch());
        ResultSet rst = preparedStatement.executeQuery();
        HashMap<Integer, Integer> map = new HashMap<>();
        if (rst.next()) {
            map.put(1, rst.getInt(1));
            map.put(2, rst.getInt(2));
            return map;
        }
        return null;
    }

    public Integer checkBank(String bank, Connection accConnection) throws SQLException {
        String query = "select m_bank.index_no as bank_index\n"
                + "from m_bank\n"
                + "where m_bank.name=?\n"
                + "LIMIT 1";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setString(1, bank);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public HashMap<Integer, Integer> saveBankAndBankBranch(String bank, String bankBranch, Connection accConnection) throws SQLException {
        Integer bankIndex = saveBank(bank, accConnection);
        if (bankIndex <= 0) {
            throw new RuntimeException("Bank Save Fail !");
        }
        Integer bankBranchIndex = saveBankBranch(bankBranch, bankIndex, accConnection);
        if (bankBranchIndex <= 0) {
            throw new RuntimeException("Bank Branch Save Fail !");
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, bankIndex);
        map.put(2, bankBranchIndex);
        return map;

    }

    private Integer saveBank(String bank, Connection accConnection) throws SQLException {
        String insertSql = "insert into m_bank (name)\n"
                + " values (?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, bank);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveBankBranch(String bankBranch, Integer bankIndex, Connection accConnection) throws SQLException {
        String insertSql = "insert into m_bank_branch (name,bank)\n"
                + " values (?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, bankBranch);
        preparedStatement.setInt(2, bankIndex);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveCardType(String cardType, Connection accConnection) throws SQLException {
        String insertSql = "insert into m_card_type (name)\n"
                + " values (?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, cardType);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer checkCardReader(String cardReader, Integer branch, Connection accConnection) throws SQLException {
        String query = "select m_card_reader.index_no\n"
                + "from m_card_reader\n"
                + "WHERE m_card_reader.number=? and m_card_reader.branch=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setString(1, cardReader);
        preparedStatement.setInt(2, branch);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer checkCardType(String cardType, Connection accConnection) throws SQLException {
        String query = "select m_card_type.index_no \n"
                + "from m_card_type\n"
                + "where m_card_type.name=?\n"
                + "limit 1";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setString(1, cardType);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer checkLoginUser(String name, String pswd, Connection accConnection) throws SQLException {
        String query = "select  m_user.index_no as login_user\n"
                + "from m_user where m_user.username=? and m_user.password=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, pswd);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer saveItemMaster(StockAdjustmentDetail detail, Integer itemSubAccountOf, Connection connection) throws SQLException {
        String insertSql = "insert into m_item (name,barcode,print_description,cost_price,\n"
                + "type,account,unit)\n"
                + " values (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, detail.getItemName());
        preparedStatement.setString(2, detail.getBarcode());
        preparedStatement.setString(3, detail.getItemName());
        preparedStatement.setBigDecimal(4, detail.getCostPrice());
        preparedStatement.setString(5, Constant.ITEM_STOCK);
        preparedStatement.setInt(6, itemSubAccountOf);
        preparedStatement.setString(7, detail.getItemUnit());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveItemUnitMaster(StockAdjustmentDetail detail, Integer item, Connection connection) throws SQLException {
        String insertSql = "insert into m_item_units (item,name,unit,qty,sale_price_normal,sale_price_register,cost_price,item_unit_type)\n"
                + " values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, item);
        preparedStatement.setString(2, detail.getItemName());
        preparedStatement.setString(3, detail.getItemUnit());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, new BigDecimal(0));
        preparedStatement.setBigDecimal(6, new BigDecimal(0));
        preparedStatement.setBigDecimal(7, detail.getCostPrice());
        preparedStatement.setString(8, Constant.ITEM_UNIT_MAIN);

        return preparedStatement.executeUpdate();
    }

    public Integer saveStockAdjustmentToAccountPlus(StockAdjustment adjustment, StockAdjustmentDetail detail, Integer stockAccount, Integer stockAdjustmentAccount, Integer user, Integer formIndexNo, HashMap<Integer, Integer> itemMap, Connection accConnection) throws SQLException {
        HashMap<Integer, Object> numberMap = getAccLedgerNumber(adjustment.getBranch(), Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT, accConnection);

        //stock account
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_STOCK_ADJUSTMENT + "/" + numberMap.get(1).toString() + "/" + Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(3, adjustment.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, adjustment.getBranch());
        preparedStatement.setInt(7, adjustment.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, detail.getCostPrice());
        preparedStatement.setBigDecimal(10, new BigDecimal(0));
        preparedStatement.setInt(11, stockAccount);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setString(13, adjustment.getRefNo());
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(15, formIndexNo);
        preparedStatement.setInt(16, Integer.parseInt(numberMap.get(3).toString()));
        preparedStatement.setString(17, "System Integration Stock Adjustment save");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int reconsileGroupNo = -1;
        while (resultSet.next()) {
            reconsileGroupNo = resultSet.getInt(1);
        }
        if (reconsileGroupNo <= 0) {
            throw new RuntimeException("save Stock Adjustment To Account Plus Qty stock Account save Fail !");
        }
        //set reconcile_group number
        updateAccLedgerReconcileGroupNo(reconsileGroupNo, accConnection);

        //save stock Adjustment Account
        String sqlStockAdjusment = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement1 = accConnection.prepareStatement(sqlStockAdjusment);
        preparedStatement1.setInt(1, Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement1.setString(2, Constant.CODE_INTEGRATION_STOCK_ADJUSTMENT + "/" + numberMap.get(1).toString() + "/" + Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement1.setString(3, adjustment.getEnterDate());
        preparedStatement1.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement1.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement1.setInt(6, adjustment.getBranch());
        preparedStatement1.setInt(7, adjustment.getBranch());
        preparedStatement1.setInt(8, user);
        preparedStatement1.setBigDecimal(9, new BigDecimal(0));
        preparedStatement1.setBigDecimal(10, detail.getCostPrice());
        preparedStatement1.setInt(11, stockAdjustmentAccount);
        preparedStatement1.setString(12, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement1.setString(13, adjustment.getRefNo());
        preparedStatement1.setString(14, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement1.setInt(15, formIndexNo);
        preparedStatement1.setInt(16, Integer.parseInt(numberMap.get(3).toString()));
        preparedStatement1.setString(17, "System Integration Stock Adjustment (+ Qty)  save");
        preparedStatement1.setString(18, null);
        preparedStatement1.setBoolean(19, false);
        preparedStatement1.setBoolean(20, false);
        preparedStatement1.setBoolean(21, false);

        return preparedStatement1.executeUpdate();

    }

    public Integer saveStockAdjustmentToAccountMinus(StockAdjustment adjustment, StockAdjustmentDetail detail, Integer stockAccount, Integer stockAdjustmentAccount, Integer user, Integer formIndexNo, Connection accConnection) throws SQLException {
        HashMap<Integer, Object> numberMap = getAccLedgerNumber(adjustment.getBranch(), Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT, accConnection);

        //stock account
        String insertSql = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(2, Constant.CODE_INTEGRATION_STOCK_ADJUSTMENT + "/" + numberMap.get(1).toString() + "/" + Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement.setString(3, adjustment.getEnterDate());
        preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement.setInt(6, adjustment.getBranch());
        preparedStatement.setInt(7, adjustment.getBranch());
        preparedStatement.setInt(8, user);
        preparedStatement.setBigDecimal(9, new BigDecimal(0));
        preparedStatement.setBigDecimal(10, detail.getCostPrice());
        preparedStatement.setInt(11, stockAccount);
        preparedStatement.setString(12, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setString(13, adjustment.getRefNo());
        preparedStatement.setString(14, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(15, formIndexNo);
        preparedStatement.setInt(16, Integer.parseInt(numberMap.get(3).toString()));
        preparedStatement.setString(17, "System Integration Stock Adjustment save");
        preparedStatement.setString(18, null);
        preparedStatement.setBoolean(19, false);
        preparedStatement.setBoolean(20, false);
        preparedStatement.setBoolean(21, false);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int reconsileGroupNo = -1;
        while (resultSet.next()) {
            reconsileGroupNo = resultSet.getInt(1);
        }
        if (reconsileGroupNo <= 0) {
            throw new RuntimeException("save Stock Adjustment To Account Plus Qty stock Account save Fail !");
        }
        //set reconcile_group number
        updateAccLedgerReconcileGroupNo(reconsileGroupNo, accConnection);

        //save stock Adjustment Account
        String sqlStockAdjusment = "insert into t_acc_ledger (number,search_code,transaction_date,`current_date`,`time`,`branch`,\n"
                + "current_branch,`user`,debit,credit,acc_account,form_name,ref_number,`type`,type_index_no,delete_ref_no,description,\n"
                + "cheque_date,bank_reconciliation,is_main,is_cheque)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement1 = accConnection.prepareStatement(sqlStockAdjusment);
        preparedStatement1.setInt(1, Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement1.setString(2, Constant.CODE_INTEGRATION_STOCK_ADJUSTMENT + "/" + numberMap.get(1).toString() + "/" + Integer.parseInt(numberMap.get(2).toString()));
        preparedStatement1.setString(3, adjustment.getEnterDate());
        preparedStatement1.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement1.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
        preparedStatement1.setInt(6, adjustment.getBranch());
        preparedStatement1.setInt(7, adjustment.getBranch());
        preparedStatement1.setInt(8, user);
        preparedStatement1.setBigDecimal(9, detail.getCostPrice());
        preparedStatement1.setBigDecimal(10, new BigDecimal(0));
        preparedStatement1.setInt(11, stockAdjustmentAccount);
        preparedStatement1.setString(12, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement1.setString(13, adjustment.getRefNo());
        preparedStatement1.setString(14, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement1.setInt(15, formIndexNo);
        preparedStatement1.setInt(16, Integer.parseInt(numberMap.get(3).toString()));
        preparedStatement1.setString(17, "System Integration Stock Adjustment (- Qty)  save");
        preparedStatement1.setString(18, null);
        preparedStatement1.setBoolean(19, false);
        preparedStatement1.setBoolean(20, false);
        preparedStatement1.setBoolean(21, false);

        return preparedStatement1.executeUpdate();

    }

    private void updateAccLedgerReconcileGroupNo(int reconsileGroupNo, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE t_acc_ledger set reconcile_group=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, reconsileGroupNo);
        preparedStatement.setInt(2, reconsileGroupNo);

        preparedStatement.executeUpdate();
    }

    public void saveStockAdjustmentDetail(StockAdjustmentDetail detail, int saveStockAdjustment, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_stock_adjustment_detail (stock_adjustment,item_no,item_name,item_unit,barcode,cost_price,qty)\n"
                + " values (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, saveStockAdjustment);
        preparedStatement.setString(2, detail.getItemNo());
        preparedStatement.setString(3, detail.getItemName());
        preparedStatement.setString(4, detail.getItemUnit());
        preparedStatement.setString(5, detail.getBarcode());
        preparedStatement.setBigDecimal(6, detail.getCostPrice());
        preparedStatement.setBigDecimal(7, detail.getQty());

        preparedStatement.executeUpdate();
    }

    public Double saveStockLedger(StockAdjustment adjustment, StockAdjustmentDetail detail, Integer user, Integer formIndexNo, HashMap<Integer, Integer> itemMap, Connection accConnection) throws SQLException {
        Integer mainStock = findStock(adjustment.getBranch(), Constant.STOCK_MAIN, accConnection);
        double totalCost = 0.00;
        if (detail.getQty().doubleValue() < 0) {
            List<TStockLedger> fifoList = getFifoList(itemMap.get(2), adjustment.getEnterDate(), adjustment.getBranch(), mainStock, accConnection);
            double removeValue = detail.getQty().doubleValue() * -1;
            if (fifoList.size() <= 0) {
                throw new RuntimeException("Stock is empty for " + itemMap.get(2) + " - " + adjustment.getBranch() + " - " + adjustment.getEnterDate());
            }
            for (TStockLedger tStockLedger : fifoList) {
                if (removeValue > tStockLedger.getInQty().doubleValue()) {
                    //save tStockLedger total
                    Integer saveT = saveStockLedgerFromAdjustment(adjustment, itemMap, tStockLedger.getInQty(), tStockLedger.getAvaragePriceIn(), tStockLedger.getGroupNumber(), mainStock, formIndexNo, accConnection);
                    if (saveT > 0) {
                        removeValue -= tStockLedger.getInQty().doubleValue();
                        totalCost += tStockLedger.getInQty().doubleValue() * tStockLedger.getAvaragePriceIn().doubleValue();
                    } else {
                        throw new RuntimeException("Stock Ledger Save Fail !");
                    }
                    System.out.println(detail.getQty() + " " + tStockLedger.getInQty());
                    if (detail.getQty().doubleValue() == tStockLedger.getInQty().doubleValue()) {
                        return totalCost;
                    }
                } else if (removeValue <= tStockLedger.getInQty().doubleValue()) {
                    //save inv qty total
                    Integer saveQ = saveStockLedgerFromAdjustment(adjustment, itemMap, new BigDecimal(removeValue), tStockLedger.getAvaragePriceIn(), tStockLedger.getGroupNumber(), mainStock, formIndexNo, accConnection);
                    if (saveQ <= 0) {
                        throw new RuntimeException("Stock Ledger Save Fail !");
                    }
                    totalCost += removeValue * tStockLedger.getAvaragePriceIn().doubleValue();
                    return totalCost;
                }
            }
        }
        return -1.0;

    }

    private Integer saveStockLedgerFromAdjustment(StockAdjustment adjustment, HashMap<Integer, Integer> itemMap, BigDecimal inQty, BigDecimal avaragePriceIn, int groupNumber, Integer mainStock, Integer formIndex, Connection accConnection) throws SQLException {
        String insertSql = "insert into t_stock_ledger (item,store,date,in_qty,out_qty,avarage_price_in,avarage_price_out,\n"
                + "form_index_no,form,branch,type,group_number)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(itemMap.get(2).toString()));
        preparedStatement.setInt(2, mainStock);
        preparedStatement.setString(3, adjustment.getEnterDate());
        preparedStatement.setBigDecimal(4, new BigDecimal(0));
        preparedStatement.setBigDecimal(5, inQty);
        preparedStatement.setBigDecimal(6, new BigDecimal(0));
        preparedStatement.setBigDecimal(7, avaragePriceIn);
        preparedStatement.setInt(8, formIndex);
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(10, adjustment.getBranch());
        preparedStatement.setString(11, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(12, groupNumber);

        return preparedStatement.executeUpdate();
    }

    public Integer saveStockLedger(StockAdjustment adjustment, StockAdjustmentDetail detail, Double totalCost, Integer user, Integer formIndexNo, HashMap<Integer, Integer> itemMap, Connection accConnection) throws SQLException {
        Integer mainStock = findStock(adjustment.getBranch(), Constant.STOCK_MAIN, accConnection);
        Integer groupNo = nextStockLedgerGroupNumber(accConnection);
        String insertSql = "insert into t_stock_ledger (item,store,date,in_qty,out_qty,avarage_price_in,avarage_price_out,\n"
                + "form_index_no,form,branch,type,group_number)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(itemMap.get(2).toString()));
        preparedStatement.setInt(2, mainStock);
        preparedStatement.setString(3, adjustment.getEnterDate());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, new BigDecimal(0));
        preparedStatement.setBigDecimal(6, new BigDecimal(totalCost / detail.getQty().doubleValue()));
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, formIndexNo);
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(10, adjustment.getBranch());
        preparedStatement.setString(11, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(12, groupNo);

        return preparedStatement.executeUpdate();

    }

    public Integer saveStockLedgerFromAdjustmentPlusQty(StockAdjustment adjustment, StockAdjustmentDetail detail, HashMap<Integer, Integer> itemMap, Integer user, Integer formIndexNo, Connection accConnection) throws SQLException {
        Integer mainStock = findStock(adjustment.getBranch(), Constant.STOCK_MAIN, accConnection);
        Integer groupNo = nextStockLedgerGroupNumber(accConnection);
        String insertSql = "insert into t_stock_ledger (item,store,date,in_qty,out_qty,avarage_price_in,avarage_price_out,\n"
                + "form_index_no,form,branch,type,group_number)\n"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, Integer.parseInt(itemMap.get(2).toString()));
        preparedStatement.setInt(2, mainStock);
        preparedStatement.setString(3, adjustment.getEnterDate());
        preparedStatement.setBigDecimal(4, detail.getQty());
        preparedStatement.setBigDecimal(5, new BigDecimal(0));
        preparedStatement.setBigDecimal(6, detail.getCostPrice());
        preparedStatement.setBigDecimal(7, new BigDecimal(0));
        preparedStatement.setInt(8, formIndexNo);
        preparedStatement.setString(9, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(10, adjustment.getBranch());
        preparedStatement.setString(11, Constant.SYSTEM_INTEGRATION_STOCK_ADJUSTMENT);
        preparedStatement.setInt(12, groupNo);

        return preparedStatement.executeUpdate();
    }

    public Double saveStockLedgerFromAdjustmentMinusQty(StockAdjustment adjustment, StockAdjustmentDetail detail, HashMap<Integer, Integer> itemMap, Integer user, Integer formIndexNo, Connection accConnection) throws SQLException {
        return saveStockLedger(adjustment, detail, user, formIndexNo, itemMap, accConnection);

    }

    public Integer tAccLedgerByCustomer(TTypeIndexDetail typeDetail, Integer account, Connection accConnection) throws SQLException {
        String insertSql = "UPDATE t_acc_ledger set acc_account=?\n"
                + "WHERE index_no=?";
        PreparedStatement preparedStatement = accConnection.prepareStatement(insertSql);
        preparedStatement.setInt(1, account);
        preparedStatement.setInt(2, typeDetail.getAccountIndex());

        return preparedStatement.executeUpdate();
    }

    public Integer getCategory(String name, Connection connection) throws SQLException {
        String query = "select m_category.index_no \n"
                + "from m_category where m_category.name=? limit 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer getBrand(String name, Connection connection) throws SQLException {
        String query = "select m_brand.index_no\n"
                + "from m_brand where m_brand.name=? limit 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer getSubCategory(String name, Connection connection) throws SQLException {
        String query = "select m_sub_category.index_no\n"
                + "from m_sub_category where m_sub_category.name=? limit 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public Integer saveCategory(String name, Connection connection) throws SQLException {
        String insertSql = "insert into m_category (name)\n"
                + " values (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, name);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveSubCategory(String name, Connection connection) throws SQLException {
        String insertSql = "insert into m_sub_category (name)\n"
                + " values (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, name);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer saveBrand(String name, Connection connection) throws SQLException {
        String insertSql = "insert into m_brand (name)\n"
                + " values (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, name);

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

}
