package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.ErrorModel;

import java.util.ArrayList;

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
        ArrayList<Node> buttons = new ArrayList<>();

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());
        buttons.add(hypLogout);

        Button btnBack = obsysButton("Back", 210, 400, 200, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());
        buttons.add(btnBack);

        Button btnClose = obsysButton("Close Program", 210, 450, 200);
        btnClose.setOnAction(evt -> exitHandler.run());
        buttons.add(btnClose);

        return buttons;
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
        ArrayList<Node> labels = new ArrayList<>();

        labels.add(obsysLabel("Oops! Something went wrong!", 300, 50, 600, "banner"));
        String prompt = "An unexpected error has occurred. Please return to the previous page or exit the application";
        labels.add(obsysLabel(prompt, 190, 285, 270));
        labels.add(obsysLabel(error.ex().getMessage(), 460, 280, 400));

        return labels;
    }

    @Override
    public Rectangle createPanels() {
        return obsysPanel(450, 250, 420, 250);
    }

    public ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        images.add(obsysImage("dolphinFail.png", 5, 315, 165, 230));
        images.add(obsysImage("dolphinLogoBlue.png", 5, 5, 200, 160));

        return images;
    }

}
