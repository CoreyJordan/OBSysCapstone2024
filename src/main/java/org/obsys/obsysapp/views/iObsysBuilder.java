package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public interface iObsysBuilder {
    Rectangle createPanels();

    ArrayList<Node> loadImages();

    ArrayList<Node> createLabels();

    ArrayList<Node> createButtons();

    ArrayList<Node> createTextFields();

    ArrayList<Node> buildPasswordField();
}
