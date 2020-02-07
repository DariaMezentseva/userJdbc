package daoTests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;

public class DBUnitConfig {
    protected static final String TABLES = "resources/tables.sql";


    public static Connection createConnection() {
        Connection connection = null;
        try {
            Properties properties = new Properties();
            FileInputStream in = new FileInputStream("test/resources/db.properties");
            properties.load(in);
            in.close();

            String url = properties.getProperty("url");
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");

            return DriverManager.getConnection(url, username, password);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public void createTable() throws Exception {
        try (Connection connection = DBUnitConfig.createConnection()) {
            RunScript.execute(connection, new InputStreamReader(getClass().getResourceAsStream("/" + TABLES), StandardCharsets.UTF_8));
        } catch (SQLException e) {
            DBUnitConfig.printSQLException(e);
        }
    }

    public void fillTable(String dataSetPath) {
        try (Connection connection = DBUnitConfig.createConnection()) {
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(dataSetPath));
            DatabaseOperation.CLEAN_INSERT.execute(getIDBConnection(connection), dataSet);
        } catch (DatabaseUnitException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ITable getExpectedTable(String tableName, String xmlFilePath) {
        ITable dataSetTable = null;
        try {
            FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFilePath));
            dataSetTable = flatXmlDataSet.getTable(tableName);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
        return dataSetTable;
    }

    public ITable getActualTable(String tableName) {
        ITable dataSetTable = null;
        try (Connection connection = DBUnitConfig.createConnection()) {
            IDataSet dataSet = getIDBConnection(connection).createDataSet();
            dataSetTable = dataSet.getTable(tableName);
        } catch (DataSetException | SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSetTable;

    }

    private IDatabaseConnection getIDBConnection(Connection connection) {
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = new DatabaseConnection(connection);
        } catch (DatabaseUnitException e) {
            throw new RuntimeException(e);
        }
        DatabaseConfig databaseConfig = databaseConnection.getConfig();
        databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
        return databaseConnection;
    }


}
