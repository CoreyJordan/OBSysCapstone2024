package org.obsys.obsysapp.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ObsysDb {

    /**
     * Creates a connection to the Azure database with login credentials. NOT production worthy
     * @return An open database connection.
     * @throws SQLException
     */
    public static Connection openDBConn() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:sqlserver://coacapstone2024.database.windows.net:1433;" +
                        "database=OBSysAccounts;user=c_jordan06@coacapstone2024;" +
                        "password=Capstone2024!;encrypt=true;trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
    }
}
