package org.obsys.obsysapp.models;

public class ErrorModel {
    private Exception ex;

    public ErrorModel(Exception ex) {
        this.ex = ex;
    }

    public Exception getEx() {
        return ex;
    }
}
