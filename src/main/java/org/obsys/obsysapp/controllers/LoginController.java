package org.obsys.obsysapp.controllers;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import org.obsys.obsysapp.models.PersonModel;
import org.obsys.obsysapp.views.LoginViewBuilder;

public class LoginController {
    private Builder<AnchorPane> viewBuilder;
    private Interactor interactor;


    public LoginController() {
        PersonModel personModel = new PersonModel();
        interactor = new Interactor(personModel);
        viewBuilder = new LoginViewBuilder(personModel, interactor::lookupLogin);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}

