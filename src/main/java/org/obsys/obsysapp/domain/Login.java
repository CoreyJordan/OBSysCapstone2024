package org.obsys.obsysapp.domain;

public class Login {
    private final String username;
    private String password;
    boolean isAdmin;
    private final int personId;

    public Login(String username, String password, boolean isAdmin, int personId) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.personId = personId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getPersonId() {
        return personId;
    }

}
