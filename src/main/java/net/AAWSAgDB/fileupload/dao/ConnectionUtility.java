package net.AAWSAgDB.fileupload.dao;

import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.ds.PGConnectionPoolDataSource;

public class ConnectionUtility {
    public static final String serverName = "localhost";
    public static final int portNumber = 5432;
    public static final String databaseName = "AAWSA-gDB";
    public static final String Uname = "postgres";
    public static final String Pword = "MmumDdostyab";
    
    // 1. Keep a single, persistent reference to the DataSource
    private static final PGConnectionPoolDataSource dataSource;

    // 2. Initialize the pool exactly ONCE when the class loads
    static {
        dataSource = new PGConnectionPoolDataSource();
        dataSource.setServerNames(new String[] { serverName });
        dataSource.setPortNumbers(new int[] { portNumber });
        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(Uname);
        dataSource.setPassword(Pword);
    }

    // 3. Return a clean connection from the pool without using static 'conn' variables
    public static Connection dbconnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dataSource.getConnection(); 
    }
}