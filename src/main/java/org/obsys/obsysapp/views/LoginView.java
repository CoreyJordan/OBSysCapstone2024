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

public class LoginView extends ViewBuilder implements IObsysBuilder {

    private final LoginModel loginModel;
    private final Runnable loginHandler;
    private final Runnable openCreationHandler;
    private final String bannerImageSource;
    private final String bannerText;

    public LoginView(LoginModel model, Runnable loginHandler, Runnable openCreationHandler,
                     String imgSource, String bannerText) {
        this.loginModel = model;
        this.loginHandler = loginHandler;
        this.openCreationHandler = openCreationHandler;
        bannerImageSource = imgSource;
        this.bannerText = bannerText;
    }

    @Override
    public AnchorPane build() {
        AnchorPane loginWindow = super.build();
        loginWindow.getChildren().addAll(createPanels());
        loginWindow.getChildren().addAll(loadImages());
        loginWindow.getChildren().addAll(createLabels());
        loginWindow.getChildren().addAll(createButtons());
        loginWindow.getChildren().addAll(createTextFields());
        loginWindow.getChildren().addAll(buildPasswordField());

        return loginWindow;
    }

    @Override
    public ArrayList<Node> createTextFields() {
        ArrayList<Node> fields = new ArrayList<>();

        TextField txtUsername = obsysTextField(150, 340, 300);
        txtUsername.textProperty().bindBidirectional(loginModel.usernameProperty());
        fields.add(txtUsername);

        fields.add(obsysLabel("USERNAME", 170, 342, "hint"));

        return fields;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        ArrayList<Node> passwordNodes = new ArrayList<>();

        PasswordField txtPassword = obsysPassword(150, 390);
        txtPassword.textProperty().bindBidirectional(loginModel.passwordProperty());
        passwordNodes.add(txtPassword);

        TextField txtUnmasked = obsysTextField(txtPassword.getLayoutX(),
                txtPassword.getLayoutY(), txtPassword.getPrefWidth());
        txtUnmasked.setVisible(false);
        passwordNodes.add(txtUnmasked);

        passwordNodes.add(obsysLabel("PASSWORD", 170, 392, "hint"));

        Button btnPrivacy = obsysButton(new Image("privacyOn.png"), 400, 390, "toggle");
        btnPrivacy.setOnMousePressed(evt -> showPassword(txtPassword, txtUnmasked));
        btnPrivacy.setOnMouseReleased(ext -> hidePassword(txtPassword, txtUnmasked));
        passwordNodes.add(btnPrivacy);

        return passwordNodes;
    }

    @Override
    public ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnLogin = obsysButton("Login", 400, 457);
        btnLogin.setOnAction(evt -> loginHandler.run());
        buttons.add(btnLogin);

        Button btnCreateAcct = obsysButton("Create Account", 730, 457);
        btnCreateAcct.setOnAction(evt -> openCreationHandler.run());
        buttons.add(btnCreateAcct);

        return buttons;
    }

    @Override
    public ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        labels.add(obsysLabel(bannerText, 28, 107, "banner"));
        labels.add(obsysLabel("Don't have an account?", 516, 465));
        labels.add(obsysLabel("Sign in to manage your accounts.", 120, 465));

        Label lblInvalidLogin = obsysLabel("", 500, 350, 200, "warning");
        lblInvalidLogin.textProperty().bindBidirectional(loginModel.invalidLoginProperty());
        labels.add(lblInvalidLogin);

        return labels;
    }

    @Override
    public ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        images.add(obsysImage(bannerImageSource, 5, 90, 910, 180));
        images.add(obsysImage("dolphinLogoTan.png", 695, 90, 220, 180));

        return images;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        ArrayList<Rectangle> panels = new ArrayList<>();
        panels.add(obsysPanel(109, 306, 379, 200));
        return panels;
    }
}
