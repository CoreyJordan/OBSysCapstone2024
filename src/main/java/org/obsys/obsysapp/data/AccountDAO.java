package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public Person readPersonByNameAndAccount(
            Connection conn, String acctNum, String firstName, String lastName) throws SQLException {
        int personId = 0;
        int acctNumber = Integer.parseInt(acctNum);

        try (PreparedStatement statement = conn.prepareStatement("""
                    SELECT Person.PersonId FROM dbo.Account
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
            }

        }
        return new Person(personId, firstName, lastName);

    }
}
