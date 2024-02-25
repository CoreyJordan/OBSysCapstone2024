package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.LoginModel;

import java.util.ArrayList;
import java.util.Objects;

public class LoginView extends ViewBuilder {

    private LoginModel loginModel;
    private Runnable loginHandler;

    public LoginView(LoginModel model, Runnable loginHandler) {
        this.loginModel = model;
        this.loginHandler = loginHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = new AnchorPane();
        window.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        window.getChildren().add(createPanel());
        window.getChildren().addAll(loadImages());
        window.getChildren().addAll(createLabels());
        window.getChildren().addAll(createButtons());
        window.getChildren().addAll(createTextFields());
        window.getChildren().addAll(buildPasswordField());

        return window;
    }

    private ArrayList<Node> createTextFields() {
        ArrayList<Node> fields = new ArrayList<>();

        TextField txtUsername = obsysTextField(150, 340, 300);
        txtUsername.textProperty().bindBidirectional(loginModel.usernameProperty());
        fields.add(txtUsername);

        fields.add(obsysLabel("USERNAME", 170, 342, "hint"));

        return fields;
    }

    private ArrayList<Node> buildPasswordField() {
        ArrayList<Node> passwordNodes = new ArrayList<>();

        PasswordField txtPassword = obsysPassword(150, 390, 300);
        txtPassword.textProperty().bindBidirectional(loginModel.passwordProperty());
        passwordNodes.add(txtPassword);

        TextField txtUnmasked = obsysTextField(txtPassword.getLayoutX(), txtPassword.getLayoutY(), txtPassword.getPrefWidth());
        txtUnmasked.setVisible(false);
        passwordNodes.add(txtUnmasked);

        passwordNodes.add(obsysLabel("PASSWORD", 170, 392, "hint"));

        Button btnPrivacy = obsysButton(new Image("privacyOn.png"), 400, 390, "toggle");
        btnPrivacy.setOnMousePressed(evt -> showPassword(txtPassword, txtUnmasked));
        btnPrivacy.setOnMouseReleased(ext -> hidePassword(txtPassword, txtUnmasked));
        passwordNodes.add(btnPrivacy);

        return passwordNodes;
    }

    private ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnLogin = obsysButton("Login", 400, 457);
        btnLogin.setOnAction(evt -> loginHandler.run());
        buttons.add(btnLogin);

        Button btnCreateAcct = obsysButton("Create Account", 730, 457);
        // TODO add action event
        buttons.add(btnCreateAcct);

        return buttons;
    }

    private ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        labels.add(obsysLabel("Welcome", 28, 107, "banner"));
        labels.add(obsysLabel("Don't have an account?", 516, 465));
        labels.add(obsysLabel("Sign in to manage your accounts.", 120, 465));

        Label lblInvalidLogin = obsysLabel("", 500, 350, 200, "warning");
        lblInvalidLogin.textProperty().bindBidirectional(loginModel.invalidLoginProperty());
        labels.add(lblInvalidLogin);

        return labels;
    }

    private ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        images.add(obsysImage("dolphinLogin.png", 5, 90, 910, 180));
        images.add(obsysImage("dolphinLogoTan.png", 695, 90, 220, 180));

        return images;
    }

    private Rectangle createPanel() {
        return obsysPanel(109,306,379,200, "panel");
    }



}
