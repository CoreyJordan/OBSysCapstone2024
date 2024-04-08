package org.obsys.obsysapp.domain;

public class Person {
    private int personId;
    private final String lastName;
    private final String firstName;
    private final String streetAddress;
    private final String city;
    private final String state;
    private final String postalCode;
    private String phone;
    private String email;
    private Login login;

    public Person() {
        personId = 0;
        firstName = "";
        lastName = "";
        state = "";
        streetAddress = "";
        city = "";
        postalCode = "";
        phone = "";
        email = "";
    }

    public Person(String lastName, String firstName, String streetAddress, String city, String state, String postalCode) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public Person(int personId, String lastName, String firstName, String streetAddress, String city, String state,
                  String postalCode, Login login) {
        this(lastName, firstName, streetAddress, city, state, postalCode);
        this.personId = personId;
        this.login = login;
    }

    public Person(int personId, String lastName, String firstName, String streetAddress, String city, String state,
                  String postalCode, String phone, String email) {
        this(lastName, firstName, streetAddress, city, state, postalCode);
        this.personId = personId;
        this.phone = phone;
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
