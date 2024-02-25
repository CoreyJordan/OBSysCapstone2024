package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.ErrorModel;

import java.util.ArrayList;
import java.util.Objects;

public class ErrorView extends ViewBuilder {

    ErrorModel error;

    public ErrorView(ErrorModel errorModel) {
        error = errorModel;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = new AnchorPane();
        window.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        window.getChildren().addAll(createPanels());
        window.getChildren().addAll(loadImages());
        window.getChildren().addAll(createLabels());
        window.getChildren().addAll(createButtons());
        return window;
    }

    private ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        return buttons;
    }

    private ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        labels.add(obsysLabel("Oops! Something went wrong!", 300, 50, 600, "banner"));
        labels.add(obsysLabel("An unexpected error has occurred. Please return to the previous page or exit " +
                "the application", 190, 285, 270));
        labels.add(obsysLabel(error.getEx().toString(), 460, 280, 400));

        return labels;
    }

    private ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        images.add(obsysImage("dolphinFail.png", 5, 315, 165, 230));
        images.add(obsysImage("dolphinLogoBlue.png", 5, 5, 200, 160));

        return images;
    }

    private Rectangle createPanels() {
        return obsysPanel(450, 250, 420, 250, "panel");
    }

}
