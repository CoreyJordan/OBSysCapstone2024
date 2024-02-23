package org.obsys.obsysapp.controllers;

import org.obsys.obsysapp.models.PersonModel;

public class Interactor {
    private PersonModel personModel;

    public Interactor(PersonModel personModel) {
        this.personModel = personModel;
    }

    public void lookupLogin() {
        System.out.println("Looking up login User: " + personModel.getUsername() + " Pass: " + personModel.getPassword());
    }
}
