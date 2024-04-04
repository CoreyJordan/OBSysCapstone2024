package org.obsys.obsysapp.controllers;

import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.NewAccountModel;
import org.obsys.obsysapp.views.NewAccountView;
import org.obsys.obsysapp.views.ViewBuilder;

public class NewAccountController {
    private Stage stage;
    private ViewBuilder viewBuilder;
    private NewAccountModel newAccountModel;

    public NewAccountController(Stage stage, NewAccountModel newAccountModel) {
        this.stage = stage;
        this.newAccountModel = newAccountModel;
        viewBuilder = new NewAccountView(newAccountModel, this::logout, this::clearForm, this::goHome,
                this::toggleNewCustomerPanel, this::registerCustomer);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void logout() {
        newAccountModel = null;

        stage.setScene(new Scene(new LoginController(stage, "dolphinExit.png", "Thank you!").getView()));
    }

    private void clearForm() {
        for (StringProperty prop : newAccountModel.getTextfields()) {
            prop.set("");
        }
    }

    private void goHome() {
        // TODO wire up return button
    }

    private void toggleNewCustomerPanel() {
        newAccountModel.setNewCustomerFieldsDisabled(!newAccountModel.isNewCustomerFieldsDisabled());
    }

    private void registerCustomer() {
        // TODO wire up registration
    }
}
