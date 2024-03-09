package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.ErrorModel;

import java.util.ArrayList;
import java.util.List;

public class ErrorView extends ViewBuilder implements IObsysBuilder {

    private final ErrorModel error;
    private final Runnable exitHandler;
    private final Runnable logoutHandler;
    private final Runnable returnHandler;

    public ErrorView(ErrorModel errorModel, Runnable exitHandler, Runnable logoutHandler, Runnable returnHandler) {
        error = errorModel;
        this.exitHandler = exitHandler;
        this.logoutHandler = logoutHandler;
        this.returnHandler = returnHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = super.build();
        window.getChildren().addAll(createPanels());
        window.getChildren().addAll(loadImages());
        window.getChildren().addAll(createLabels());
        window.getChildren().addAll(createButtons());
        return window;
    }

    @Override
    public ArrayList<Node> createButtons() {
        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Button btnBack = obsysButton("Back", 210, 400, 200, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());

        Button btnClose = obsysButton("Close Program", 210, 450, 200);
        btnClose.setOnAction(evt -> exitHandler.run());

        return new ArrayList<>(List.of(hypLogout, btnBack, btnClose));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        return null;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }

    @Override
    public ArrayList<Node> createLabels() {
        String prompt = "An unexpected error has occurred. Please return to the previous page or exit the application";
        return new ArrayList<>(){{
            add(obsysLabel("Oops! Something went wrong!", 300, 50, 600, "banner"));
            add(obsysLabel(prompt, 190, 285, 270));
            add(obsysLabel(error.ex().getMessage(), 460, 280, 400));
        }};
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>(){{
            add(obsysPanel(450, 250, 420, 250));
        }};
    }

    public ArrayList<Node> loadImages() {
        return new ArrayList<>(){{
            add(obsysImage("dolphinFail.png", 5, 315, 165, 230));
            add(obsysImage("dolphinLogoBlue.png", 5, 5, 200, 160));
        }};
    }

}
