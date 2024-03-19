package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAO {
    public Person readPersonByPersonId(Connection conn, int personId) throws SQLException {
        String lastName = "";
        String firstName = "";
        String address = "";
        String city = "";
        String state = "";
        String postal = "";

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM dbo.Person WHERE PersonId = ?;
                """)){
            statement.setInt(1, personId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lastName = resultSet.getString("LastName");
                firstName = resultSet.getString("FirstName");
                address = resultSet.getString("PersonAddress");
                city = resultSet.getString("City");
                state = resultSet.getString("AddressState");
                postal = resultSet.getString("Postal");
            }
        }

        return new Person(lastName, firstName, address, city, state, postal);
    }
}
