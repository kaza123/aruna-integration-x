/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import db_connections.DataSourceWrapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author chama
 */
public class ConnectionService {

    private static ConnectionService instance;
    private final DataSourceWrapper operationDataSourceWrapper;
    private final DataSourceWrapper accountDataSourceWrapper;

    public ConnectionService() throws SQLException {
        
        
        Properties prop = new Properties();
        InputStream input = null;
        String accUrl = null;
        String accUser = null;
        String accPswd = null;
        
        String operaUrl = null;
        String operaUser = null;
        String operaPswd = null;
        
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            
            accUrl = prop.getProperty("account_url");
            accUser = prop.getProperty("account_user");
            accPswd = prop.getProperty("account_password");
            
            operaUrl = prop.getProperty("opera_url");
            operaUser = prop.getProperty("opera_user");
            operaPswd = prop.getProperty("opera_password");

        } catch (IOException ex) {
            System.out.println("Can't find database Connection !");
        }
        this.operationDataSourceWrapper = new DataSourceWrapper(operaUrl, operaUser, operaPswd);
        this.accountDataSourceWrapper = new DataSourceWrapper(accUrl, accUser, accPswd);
    }
    
     public static ConnectionService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionService();
        }
        return instance;
    }

    public DataSourceWrapper getOperationDataSourceWrapper() {
        return operationDataSourceWrapper;
    }

    public DataSourceWrapper getAccuntDataSourceWrapper() {
        return accountDataSourceWrapper;
    }

}
