package org.obsys.obsysapp.controllers;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.models.ErrorModel;
import org.obsys.obsysapp.views.ErrorView;
import org.obsys.obsysapp.views.ViewBuilder;

public class ErrorController {
    private Stage stage;
    private ErrorModel errorModel;
    private ViewBuilder viewBuilder;


    public ErrorController(Stage stage, Exception ex) {
        this.stage = stage;
        errorModel = new ErrorModel(ex);
        viewBuilder = new ErrorView(errorModel);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
