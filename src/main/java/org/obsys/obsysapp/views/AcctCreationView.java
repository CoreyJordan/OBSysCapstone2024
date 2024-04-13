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
import java.util.List;

public class AcctCreationView extends ViewBuilder implements IObsysBuilder {
    private final CreationModel creationModel;
    private final LoginModel loginModel;
    private final Runnable returnHandler;
    private final Runnable lookupHandler;
    private final Runnable createLoginHandler;

    public AcctCreationView(CreationModel creationModel,
                            LoginModel loginModel,
                            Runnable returnHandler,
                            Runnable lookupHandler,
                            Runnable createLoginHandler) {
        this.creationModel = creationModel;
        this.loginModel = loginModel;
        this.returnHandler = returnHandler;
        this.lookupHandler = lookupHandler;
        this.createLoginHandler = createLoginHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = super.build();
        window.getChildren().addAll(createPanels());
        window.getChildren().addAll(loadImages());
        window.getChildren().addAll(createLabels());
        window.getChildren().addAll(createButtons());
        window.getChildren().addAll(createTextFields());
        window.getChildren().addAll(buildPasswordField());

        return window;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>(){{
            add(obsysPanel(50, 140, 310, 310));
        }};
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>(){{
            add(obsysImage("dolphinCreate.png", 625, 5, 235, 540));
            add(obsysImage("dolphinLogoTan.png", 625, 340, 235, 210));
        }};
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblAcctNumInvalid = obsysLabel("", 365, 145, "warning");
        lblAcctNumInvalid.textProperty().bindBidirectional(
                creationModel.acctNumWarningProperty());

        Label lblFirstNameInvalid = obsysLabel("", 365, 190, "warning");
        lblFirstNameInvalid.textProperty().bindBidirectional(
                creationModel.firstNameWarningProperty());

        Label lblLastNameInvalid = obsysLabel("", 365, 235, "warning");
        lblLastNameInvalid.textProperty().bindBidirectional(
                creationModel.lastNameWarningProperty());

        Label lblNotFound = obsysLabel("", 365, 280, 255, "warning");
        lblNotFound.textProperty().bindBidirectional(
                creationModel.notFoundProperty());

        Label lblUsernameInvalid = obsysLabel("", 365, 319, 255, "warning");
        lblUsernameInvalid.textProperty().bindBidirectional(
                loginModel.invalidUsernameProperty());

        Label lblPasswordInvalid = obsysLabel("", 365, 369, 255, "warning");
        lblPasswordInvalid.textProperty().bindBidirectional(
                loginModel.invalidPasswordProperty());

        String message = "Confirm your information, then create a username & " +
                "password. Passwords must be at least 6 characters, contain " +
                "at least 1 upper, 1 lower, 1 symbol, 1 number.";
        return new ArrayList<>(List.of(
                lblAcctNumInvalid,
                lblFirstNameInvalid,
                lblLastNameInvalid,
                lblNotFound,
                lblUsernameInvalid,
                lblPasswordInvalid)){{
            add(obsysLabel("Link your account", 25, 50, "banner"));
            add(obsysLabel(message, 50, 470, 540));
        }};
    }

    @Override
    public ArrayList<Node> createButtons() {
        Image imgBack = new Image("back.png");
        Button btnBack = obsysButton("Return to Login", 10, 10, 180, imgBack);
        btnBack.setOnAction(evt -> returnHandler.run());

        Button btnLookUp = obsysButton("Find account", 225, 280);
        btnLookUp.setOnAction(evt -> lookupHandler.run());
        btnLookUp.setDefaultButton(true);

        Button btnRegister = obsysButton("Register", 260, 409);
        btnRegister.disableProperty().bindBidirectional(
                creationModel.registrationDisabledProperty());
        btnRegister.setOnAction(evt -> createLoginHandler.run());

        return new ArrayList<>(List.of(btnBack,btnLookUp, btnRegister));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        TextField txtAcctNum = obsysTextField(55, 145, 300);
        txtAcctNum.textProperty().bindBidirectional(
                creationModel.acctNumProperty());

        TextField txtFirstName = obsysTextField(55, 190, 300);
        txtFirstName.textProperty().bindBidirectional(
                creationModel.firstNameProperty());

        TextField txtLastName = obsysTextField(55, 235, 300);
        txtLastName.textProperty().bindBidirectional(
                creationModel.lastNameProperty());

        TextField txtUsername = obsysTextField(55, 319, 300);
        txtUsername.textProperty().bindBidirectional(
                loginModel.usernameProperty());
        txtUsername.disableProperty().bindBidirectional(
                creationModel.registrationDisabledProperty());

        return new ArrayList<>(List.of(
                txtAcctNum,
                txtFirstName,
                txtLastName,
                txtUsername)){{
            add(obsysLabel("ACCOUNT NUMBER", 75, 147, "hint"));
            add(obsysLabel("FIRST NAME", 75, 192, 300, "hint"));
            add(obsysLabel("LAST NAME", 75, 237, "hint"));
            add(obsysLabel("USERNAME", 75, 321, "hint"));
        }};
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        PasswordField txtPassword = obsysPassword(55, 364);
        txtPassword.textProperty().bindBidirectional(
                loginModel.passwordProperty());
        txtPassword.disableProperty().bind(
                creationModel.passwordDisableProperty());

        TextField txtUnmasked = obsysTextField(txtPassword.getLayoutX(),
                txtPassword.getLayoutY(), txtPassword.getPrefWidth());
        txtUnmasked.textProperty().bind(txtPassword.textProperty());
        txtUnmasked.visibleProperty().bind(txtPassword.visibleProperty().not());

        Image imgPrivacy = new Image("privacyOn.png");
        Button btnPrivacy = obsysButton(imgPrivacy, 300, 364, "toggle");
        btnPrivacy.disableProperty().bindBidirectional(
                creationModel.registrationDisabledProperty());
        setPrivacyEvent(btnPrivacy, txtPassword);

        return new ArrayList<>(List.of(txtPassword, txtUnmasked, btnPrivacy)){{
            add(obsysLabel("PASSWORD", 75, 364, "hint"));
        }};
    }

    private void setPrivacyEvent(Button btnPrivacy, PasswordField txtPassword) {
        btnPrivacy.setOnMousePressed(evt -> {
            creationModel.setPasswordDisable(true);
            txtPassword.setVisible(false);
        });
        btnPrivacy.setOnMouseReleased(ext -> {
            creationModel.setPasswordDisable(false);
            txtPassword.setVisible(true);
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getLength());
        });
    }
}
