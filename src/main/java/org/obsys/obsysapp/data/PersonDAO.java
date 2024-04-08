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

    /**
     * Adds a new person to the Obsys database. Upon successful insertion, it will return a freshly generated personID
     * of the new insertion.
     * @param conn stable Obsys DB connection
     * @param person object containing customer info, null for username and password
     * @return the personID of the newly created customer
     * @throws SQLException possible db failures
     */
    public int insertNewPerson(Connection conn, Person person) throws SQLException {
        int personId = 0;
        try (PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO dbo.Person
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, NULL, NULL);
                SELECT IDENT_CURRENT('Person') AS PersonId;
                    """)) {
            statement.setString(1, person.getLastName());
            statement.setString(2, person.getFirstName());
            statement.setString(3, person.getStreetAddress());
            statement.setString(4, person.getCity());
            statement.setString(5, person.getState());
            statement.setString(6, person.getPostalCode());
            statement.setString(7, person.getPhone());
            statement.setString(8, person.getEmail());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personId = resultSet.getInt("PersonId");
            }
            return personId;
        }
    }
}
