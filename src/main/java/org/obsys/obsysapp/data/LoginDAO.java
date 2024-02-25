package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    /**
     * Queries the database for user login data by username. Values are defaulted to account for null username results.
     *
     * @param conn     A valid connection to the database.
     * @param username The username to be queried.
     * @return The Person ID(PK), password, and Admin status of the queried username.
     * @throws SQLException Indicates a failed query attempt.
     */
    public Login readPasswordByUsername(Connection conn, String username) throws SQLException {
        String password = "Not Found";
        boolean isAdmin = false;
        int personId = 0;

        try (PreparedStatement statement = conn.prepareStatement("""
                    SELECT UserPassword, IsAdmin, PersonId FROM dbo.Person WHERE Username = ?
                """)) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                password = resultSet.getString(1);
                isAdmin = resultSet.getBoolean(2);
                personId = resultSet.getInt(3);
            }
        }
        return new Login(username, password, isAdmin, personId);
    }
}
