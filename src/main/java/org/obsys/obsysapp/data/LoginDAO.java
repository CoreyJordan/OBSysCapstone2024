package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.Person;

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

    public Person readPersonByNameAndAccount(
            Connection conn, String acctNum, String firstName, String lastName) throws SQLException {
        int personId = 0;
        String username = null;
        String password = null;
        int acctNumber = Integer.parseInt(acctNum);

        try (PreparedStatement statement = conn.prepareStatement("""
                    SELECT Person.PersonId, Person.Username, Person.UserPassword
                    FROM dbo.Account
                    JOIN dbo.Person ON dbo.Account.PersonId = dbo.Person.PersonId
                    WHERE dbo.Account.AccountId = ?
                    AND dbo.Person.FirstName = ?
                    AND dbo.Person.LastName = ?;
                    """)) {
            statement.setInt(1,acctNumber);
            statement.setString(2, firstName);
            statement.setString(3, lastName);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personId = resultSet.getInt(1);
                username = resultSet.getString(2);
                password = resultSet.getString(3);
            }

        }
        return new Person(personId, username, password);

    }

    public int insertLogin(Connection conn, int personId, String username, String password) throws SQLException {
        int rowsUpdated;
        try (PreparedStatement statement = conn.prepareStatement("""
                    UPDATE dbo.Person
                    SET Username = ?, UserPassword = ?
                    WHERE PersonId = ?
                    """)){
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3, personId);
            rowsUpdated = statement.executeUpdate();

        }

        return rowsUpdated;
    }

    /**
     * Queries the database for a username and returns teh number of that username found. The database cannot support
     * uniqueness and NULL values. This statement is used to check for existing username to enforce uniqueness at the
     * application level.
     * @param conn Established connection to the database.
     * @param username The username to be queried against.
     * @return The number of username occurrences. Ideally 1 or 0.
     * @throws SQLException Database error
     */
    public int findUsername(Connection conn, String username) throws SQLException {
        int rowsFound = 0;
        try (PreparedStatement statement = conn.prepareStatement("""
                    SELECT COUNT(*)
                    FROM dbo.Person
                    WHERE Username = ?;
                    """)){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                rowsFound = resultSet.getInt(1);
            }
        }
        return rowsFound;
    }
}
