package org.obsys.obsysapp.data;

import org.obsys.obsysapp.domain.Person;

import java.sql.Connection;

public class PersonDAO {
    public Person readPersonByPersonId(Connection conn, int personId) {
        String lastName = "";
        String firstName = "";
        String address = "";
        String city = "";
        String state = "";
        String postal = "";



        return new Person(lastName, firstName, address, city, state, postal);
    }
}
