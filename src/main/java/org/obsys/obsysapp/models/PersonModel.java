package org.obsys.obsysapp.models;

import java.util.ArrayList;

public class PersonModel {
    private int personId = 0;
    private String firstName = "";
    private String lastName = "";
    private String streetAddress = "";
    private String city = "";
    private String state = "";
    private String postalCode = "";
    private String phone = "";
    private String emailAddress = "";
    private boolean isAdmin = false;
    private String username = "";
    private String password = "";
    private ArrayList<Integer> accountsHeld = new ArrayList<>();

    public PersonModel() {
    }

    public PersonModel(String firstName, String lastName, String streetAddress, String city, String state, String postalCode,
                       String phone, String emailAddress, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.phone = phone;
        this.emailAddress = emailAddress;
        this.isAdmin = isAdmin;
    }

    public PersonModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PersonModel(int personId, String firstName, String lastName, String streetAddress, String city, String state,
                       String postalCode, String phone, String emailAddress, boolean isAdmin, String username,
                       String password) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.phone = phone;
        this.emailAddress = emailAddress;
        this.isAdmin = isAdmin;
        this.username = username;
        this.password = password;
        accountsHeld = new ArrayList<>();
    }

    public int getPersonId() {
        return personId;
    }

    public ArrayList<Integer> getAccountsHeld() {
        return accountsHeld;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
