package org.obsys.obsysapp.controllers;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.views.AccountView;
import org.obsys.obsysapp.views.ViewBuilder;

public class AccountController {
    private Stage stage;
    private ViewBuilder viewBuilder;
    private AccountDAO acctDao;
    private AccountModel acctModel;

    public AccountController(Stage stage, AccountModel acctModel) {
        this.stage = stage;
        acctDao = new AccountDAO();
        this.acctModel = acctModel;
        viewBuilder = new AccountView();
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
