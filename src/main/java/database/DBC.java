package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBC {

    private static Properties loadDBConfig(String fileName) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream reader = new
                FileInputStream("src/test/resources/" + fileName)) {
            properties.load(reader);
            reader.close();
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getValueFromDatabase(String sqlQuery) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Properties dbConfig = loadDBConfig("DBConfing.properties");

            String user = dbConfig.getProperty("DBUserName");
            String password = dbConfig.getProperty("DBPassword");
            String url = dbConfig.getProperty("DBConnectionURL");
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            if (resultSet.next()) {
                String value = resultSet.getString(1);
                System.out.println("Value retrieved from database: " + value);
                return value;
            } else {
                System.out.println("No results found in the database for query: " + sqlQuery);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("MySQL connection is closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}