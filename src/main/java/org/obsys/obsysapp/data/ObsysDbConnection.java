package org.obsys.obsysapp.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ObsysDbConnection {

    /**
     * Creates a connection to the Azure database with login credentials. NOT
     * production worthy (Password in string)
     * @return an open database connection.
     * @throws SQLException potential database errors
     */
    public static Connection openDBConn() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:sqlserver://coacapstone2024.database.windows.net:1433;" +
                        "database=OBSysAccounts_2024-03-24T04-34Z;" +
                        "user=c_jordan06@coacapstone2024;" +
                        "password=Capstone2024!;" +
                        "Pooling=True;" +
                        "encrypt=true;" +
                        "trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;" +
                        "loginTimeout=600;");
    }
}
