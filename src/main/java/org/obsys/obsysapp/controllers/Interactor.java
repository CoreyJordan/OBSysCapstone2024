package org.obsys.obsysapp.controllers;

import org.obsys.obsysapp.models.PersonModel;

public class Interactor {
    private PersonModel personModel;

    public Interactor(PersonModel personModel) {
        this.personModel = personModel;
    }
}
