package org.obsys.obsysapp.domain;

public class Login {
    private String username;
    private String password;
    boolean isAdmin;
    private int personId;

    public Login(String username, String password, boolean isAdmin, int personId) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.personId = personId;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
