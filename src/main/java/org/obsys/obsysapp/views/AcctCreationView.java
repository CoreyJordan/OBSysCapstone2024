package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.CreationModel;
import org.obsys.obsysapp.models.LoginModel;

import java.util.ArrayList;
import java.util.Objects;

public class AcctCreationView extends ViewBuilder implements IObsysBuilder {
    private CreationModel creationModel;
    private LoginModel loginModel;
    private Runnable returnHandler;

    public AcctCreationView(CreationModel creationModel, LoginModel loginModel, Runnable returnHandler) {
        this.creationModel = creationModel;
        this.loginModel = loginModel;
        this.returnHandler = returnHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = new AnchorPane();
        window.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        window.getChildren().add(createPanels());
        window.getChildren().addAll(loadImages());
        window.getChildren().addAll(createLabels());
        window.getChildren().addAll(createButtons());
        window.getChildren().addAll(createTextFields());
        window.getChildren().addAll(buildPasswordField());

        return window;
    }

    @Override
    public Rectangle createPanels() {
        return obsysPanel(50, 140, 310, 310, "panel");
    }

    @Override
    public ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();
        images.add(obsysImage("dolphinCreate.png", 625, 5, 235, 540));
        images.add(obsysImage("dolphinLogoTan.png", 625, 340, 235, 210));

        return images;
    }

    @Override
    public ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        Label lblNotFound = obsysLabel("", 365, 280, 255, "warning");
        labels.add(lblNotFound);

        Label lblUsernameInvalid = obsysLabel("", 365, 319, 255, "warning");
        labels.add(lblUsernameInvalid);

        Label lblPasswordInvalid = obsysLabel("", 365, 364, 255, "warning");
        labels.add(lblPasswordInvalid);

        labels.add(obsysLabel("Link your account", 25, 50, "banner"));

        String message = "Confirm your information, then create a username & password.";
        message += " Passwords must be at least 6 characters, contain at least 1 upper, 1 lower, 1 symbol, 1 number.";

        labels.add(obsysLabel(message, 50, 470, 540));

        return labels;
    }

    @Override
    public ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnBack = obsysButton("Return to Login", 10, 10, 180, new Image("back.png"));
        btnBack.setOnAction(evt -> returnHandler.run());
        buttons.add(btnBack);

        Button btnLookUp = obsysButton("Find account", 225, 280);
        // TODO btnLookUp.setOnAction(evt -> lookupHandler.run();
        buttons.add(btnLookUp);

        // The register button is disabled until a valid account is found.
        Button btnRegister = obsysButton("Register", 260, 409);
        // TODO btnRegister.setOnAction(evt -> createLoginHandler.run());
        btnRegister.setDisable(true);
        buttons.add(btnRegister);

        return buttons;
    }

    @Override
    public ArrayList<Node> createTextFields() {
        ArrayList<Node> fields = new ArrayList<>();

        double posX = 55;
        double hintPosX = 75;
        double posY = 145;
        double hintPosY = 147;
        double width = 300;
        double translateY = 45;

        TextField txtAcctNum = obsysTextField(posX, posY, width);
        txtAcctNum.textProperty().bindBidirectional(creationModel.acctNumProperty());
        fields.add(txtAcctNum);
        fields.add(obsysLabel("ACCOUNT NUMBER", hintPosX, hintPosY, "hint"));
        posY += translateY;
        hintPosY += translateY;

        TextField txtFirstName = obsysTextField(posX, posY, width);
        txtFirstName.textProperty().bindBidirectional(creationModel.firstNameProperty());
        fields.add(txtFirstName);
        fields.add(obsysLabel("FIRST NAME", hintPosX, hintPosY, width, "hint"));
        posY += translateY;
        hintPosY += translateY;

        TextField txtLastName = obsysTextField(posX, posY, width);
        txtLastName.textProperty().bindBidirectional(creationModel.lastNameProperty());
        fields.add(txtLastName);
        fields.add(obsysLabel("LAST NAME", hintPosX, hintPosY, "hint"));
        posY = 319;
        hintPosY = 321;

        // Disable the username field until a valid account is found.
        TextField txtUsername = obsysTextField(posX, posY, width);
        txtUsername.textProperty().bindBidirectional(loginModel.usernameProperty());
        txtUsername.setDisable(true);
        fields.add(txtUsername);
        fields.add(obsysLabel("USERNAME", hintPosX, hintPosY, "hint"));

        return fields;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        ArrayList<Node> passwordNodes = new ArrayList<>();

        PasswordField txtPassword = obsysPassword(55, 364, 300);
        txtPassword.textProperty().bindBidirectional(loginModel.passwordProperty());
        passwordNodes.add(txtPassword);

        TextField txtUnmasked = obsysTextField(txtPassword.getLayoutX(),
                txtPassword.getLayoutY(), txtPassword.getPrefWidth());
        txtUnmasked.setVisible(false);
        passwordNodes.add(txtUnmasked);

        passwordNodes.add(obsysLabel("PASSWORD", 75, 364, "hint"));

        Button btnPrivacy = obsysButton(new Image("privacyOn.png"), 300, 364, "toggle");
        btnPrivacy.setOnMousePressed(evt -> showPassword(txtPassword, txtUnmasked));
        btnPrivacy.setOnMouseReleased(ext -> hidePassword(txtPassword, txtUnmasked));
        passwordNodes.add(btnPrivacy);

        // Disable the password node until a valid account is found.
        for (Node n : passwordNodes) {
            n.setDisable(true);
        }

        return passwordNodes;
    }
}
