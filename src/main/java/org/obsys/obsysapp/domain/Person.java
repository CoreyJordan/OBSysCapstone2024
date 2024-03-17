package org.obsys.obsysapp.domain;

public class Person {
    private int personId;
    private String lastName;
    private String firstName;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private Login login;

    public Person(int personId, String lastName, String firstName, String streetAddress, String city, String state,
                  String postalCode, Login login) {
        this.personId = personId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.login = login;
    }

    public int getPersonId() {
        return personId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Login getLogin() {
        return login;
    }
}
