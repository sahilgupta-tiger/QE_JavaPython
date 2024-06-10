package com.ta.api.framework.helperfunctions;

import com.ta.api.framework.reports.ReportManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.log4testng.Logger;

import java.sql.*;
import java.util.*;

/**
 * This class is to Connect to the Data Base and returns the specific values to the User
 *
 * @author gayathri
 */
public class DataBaseUtils extends ReportManager {

    private static final Logger log = Logger.getLogger(DataBaseUtils.class);

    /**
     * To connect to the DataBase
     *
     * @return returns the SQL statement
     */
    public Statement dbConnectionStatement() {

        Properties props = new Properties();
        props.setProperty("user", getProperty("DBUserName"));
        props.setProperty("password", getProperty("DBPassword"));

        Statement stmt = null;

        try {
            Class.forName(getProperty("DBCONNECTIONCLASS"));
        } catch (ClassNotFoundException e1) {
            log.error(ExceptionUtils.getStackTrace(e1));
        }

        try  {
            Connection con = DriverManager.getConnection(getProperty("DBConnectionURL"), props);
            stmt = con.createStatement();

        } catch (SQLException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return stmt;
    }

    /**
     * This function will connect to the Data Base and returns the entire record specified by the User
     *
     * @param query       the SQL query in string format
     * @param columnLabel the column Label for which value should return
     * @return returns the SQL result in List
     */
    public List<String> connecttoDBList(String query, String columnLabel) {

        List<String> resultArray = new ArrayList<>();
        Statement stmt = dbConnectionStatement();
        try (ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                resultArray.add(rs.getString(columnLabel));

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return resultArray;
    }

    public Map<String,String> connecttoDBList(String query, List<String> columnLabel) {

        Statement stmt = dbConnectionStatement();
        Map<String,String> recordsMap = new HashMap<>();
        try (ResultSet rs = stmt.executeQuery(query)) {
            columnLabel.forEach(e->{
                try {
                    rs.next();
                    recordsMap.put(e,rs.getString(e));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return recordsMap;
    }

    /**
     * This function will connect to the Data Base and returns the Particular Column Value specified by the User
     *
     * @param query       the SQL query in string format
     * @param columnLabel the column Label for which value should return
     * @return returns the SQL result in String
     */
    public String connecttoDBString(String query, String columnLabel) {

        String resultArray = "";
        Statement stmt = dbConnectionStatement();
        try (ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                resultArray = rs.getString(columnLabel);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return resultArray;
    }

    /**
     * This function will update the records in the Data Base specified by the User
     *
     * @param query the SQL query in string format
     */
    public void updateRecordsInDB(String query) {

        try {
            Statement stmt = dbConnectionStatement();

            int rowsUpdated = stmt.executeUpdate(query);
            log.info("No.of Rows Updated: " + rowsUpdated);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
