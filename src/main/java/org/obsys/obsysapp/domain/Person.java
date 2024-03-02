package org.obsys.obsysapp.domain;

public class Person {
    private final int personId;
    private final String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person(int personId, String username, String password) {
        this.personId = personId;
        this.username = username;
        this.password = password;
    }

    public int getPersonId() {
        return personId;
    }

}
