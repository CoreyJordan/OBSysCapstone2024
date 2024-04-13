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
import java.util.List;

public class LoginView extends ViewBuilder implements IObsysBuilder {

    private final LoginModel loginModel;
    private final Runnable loginHandler;
    private final Runnable openCreationHandler;
    private final String bannerImageSource;
    private final String bannerText;

    public LoginView(LoginModel model,
                     Runnable loginHandler,
                     Runnable openCreationHandler,
                     String imgSource,
                     String bannerText) {
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
        TextField txtUsername = obsysTextField(150, 340, 300);
        txtUsername.textProperty().bindBidirectional(
                loginModel.usernameProperty());
        return new ArrayList<>(List.of(
                txtUsername,
                obsysLabel("USERNAME", 170, 342, "hint")));
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        PasswordField txtPassword = obsysPassword(150, 390);
        txtPassword.textProperty().bindBidirectional(
                loginModel.passwordProperty());

        TextField txtUnmasked = obsysTextField(txtPassword.getLayoutX(),
                txtPassword.getLayoutY(), txtPassword.getPrefWidth());
        txtUnmasked.setVisible(false);

        Button btnPrivacy = obsysButton(
                new Image("privacyOn.png"), 400, 390, "toggle");
        btnPrivacy.setOnMousePressed(evt ->
                showPassword(txtPassword, txtUnmasked));
        btnPrivacy.setOnMouseReleased(ext ->
                hidePassword(txtPassword, txtUnmasked));

        return new ArrayList<>(List.of(
                txtPassword,
                txtUnmasked,
                btnPrivacy,
                obsysLabel("PASSWORD", 170, 392, "hint")));
    }

    @Override
    public ArrayList<Node> createButtons() {
        Button btnLogin = obsysButton("Login", 400, 457);
        btnLogin.setOnAction(evt -> loginHandler.run());

        Button btnCreateAcct = obsysButton("Create Account", 730, 457);
        btnCreateAcct.setOnAction(evt -> openCreationHandler.run());

        return new ArrayList<>(List.of(btnLogin, btnCreateAcct));
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblInvalidLogin = obsysLabel("", 500, 350, 200, "warning");
        lblInvalidLogin.textProperty().bindBidirectional(
                loginModel.invalidLoginProperty());

        return new ArrayList<>(List.of(
                lblInvalidLogin,
                obsysLabel(bannerText, 28, 107, "banner"),
                obsysLabel("Don't have an account?", 516, 465),
                obsysLabel("Sign in to manage your accounts.", 120, 465)));
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>(List.of(obsysImage(
                bannerImageSource, 5, 90, 910, 180),
                obsysImage("dolphinLogoTan.png", 695, 90, 220, 180)));
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>(List.of(obsysPanel(109, 306, 379, 200)));
    }
}
