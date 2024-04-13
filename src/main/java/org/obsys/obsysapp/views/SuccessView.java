package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.SuccessModel;

import java.util.ArrayList;
import java.util.List;

public class SuccessView extends ViewBuilder implements IObsysBuilder {
    private SuccessModel successModel;
    private final Runnable logoutHandler;
    private final Runnable returnHandler;

    public SuccessView(SuccessModel successModel,
                       Runnable logoutHandler,
                       Runnable returnHandler) {
        this.successModel = successModel;
        this.logoutHandler = logoutHandler;
        this.returnHandler = returnHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane successWindow = super.build();
        successWindow.getChildren().addAll(loadImages());
        successWindow.getChildren().addAll(createPanels());
        successWindow.getChildren().addAll(createLabels());
        successWindow.getChildren().addAll(createButtons());

        return successWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>(List.of(obsysPanel(480, 230, 400, 280)));
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>() {{
            add(obsysImage("dolphinSuccess.png", 5, 5, 620, 540));
            add(obsysImage("dolphinLogoBlue.png", 5, 5, 190, 160));
        }};
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblFields = obsysLabel("", 490, 260, 200, "right-aligned");
        lblFields.textProperty().bind(successModel.fieldsProperty());

        Label lblDetails = obsysLabel("", 650, 260, 200, "right-aligned");
        lblDetails.textProperty().bind(successModel.detailsProperty());
        return new ArrayList<>(List.of(lblFields, lblDetails)){{
            add(obsysLabel("Success!", 600, 55, "banner"));
            add(obsysLabel("Transaction Details", 480, 200));
        }};
    }

    @Override
    public ArrayList<Node> createButtons() {
        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Button btnBack = obsysButton(
                "Return to Account", 650, 450, 200, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());

        return new ArrayList<>(List.of(hypLogout, btnBack));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        return null;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }
}
