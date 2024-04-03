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
        String phone = "";
        String email = "";

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM dbo.Person WHERE PersonId = ?;
                """)) {
            statement.setInt(1, personId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lastName = resultSet.getString("LastName");
                firstName = resultSet.getString("FirstName");
                address = resultSet.getString("PersonAddress");
                city = resultSet.getString("City");
                state = resultSet.getString("AddressState");
                postal = resultSet.getString("Postal");
                phone = resultSet.getString("Phone");
                email = resultSet.getString("Email");
            }
        }

        return new Person(personId, lastName, firstName, address, city, state, postal, phone, email);
    }

    /**
     * Searches the database for a match based on both first and last name. If multiple found will return only one.
     *
     * @param conn      stable Obsys DB connection
     * @param firstName first name
     * @param lastName  last name
     * @return a person's detailed information
     * @throws SQLException possible db failures
     */
    public Person readPersonByFullName(Connection conn, String firstName, String lastName) throws SQLException {
        int personId = 0;
        String address = "";
        String city = "";
        String state = "";
        String postal = "";
        String phone = "";
        String email = "";

        try (PreparedStatement statement = conn.prepareStatement("""
                SELECT PersonId, PersonAddress, City, AddressState, Postal, Phone, Email
                FROM dbo.Person
                WHERE FirstName = ?
                AND LastName = ?
                    """)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personId = resultSet.getInt("PersonId");
                address = resultSet.getString("PersonAddress");
                city = resultSet.getString("City");
                state = resultSet.getString("AddressState");
                postal = resultSet.getString("Postal");
                phone = resultSet.getString("Phone");
                email = resultSet.getString("Email");
            }

            return new Person(personId, lastName, firstName, address, city, state, postal, phone, email);
        }
    }
}
