package org.obsys.obsysapp.controllers;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.AdminHomeModel;
import org.obsys.obsysapp.views.AdminHomeView;
import org.obsys.obsysapp.views.ViewBuilder;

public class AdminHomeController {
    Stage stage;
    ViewBuilder viewBuilder;
    AdminHomeModel adminModel;

    public AdminHomeController(Stage stage, AdminHomeModel adminModel) {
        this.stage = stage;
        this.adminModel = adminModel;
        viewBuilder = new AdminHomeView(adminModel);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
