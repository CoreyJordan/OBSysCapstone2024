package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;
import org.obsys.obsysapp.models.PersonModel;

import java.util.ArrayList;
import java.util.Objects;

public class LoginViewBuilder implements Builder<AnchorPane> {

    private PersonModel personModel;

    public LoginViewBuilder(PersonModel model) {
        this.personModel = model;
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
        window.getChildren().addAll(createFields());

        return window;
    }

    private ArrayList<Node> createFields() {
        ArrayList<Node> fields = new ArrayList<>();

        double layoutX = 150;
        double layoutY = 340;
        double width = 300;
        double hintOffsetX = 20;
        double hintOffsetY = 2;
        double fieldSpacing = 50;

        TextField txtUsername = new TextField();
        txtUsername.setLayoutX(layoutX);
        txtUsername.setLayoutY(layoutY);
        txtUsername.setPrefWidth(width);
        fields.add(txtUsername);

        Label lblUserName = new Label("USERNAME");
        lblUserName.getStyleClass().add("hint");
        lblUserName.setLayoutX(txtUsername.getLayoutX() + hintOffsetX);
        lblUserName.setLayoutY(txtUsername.getLayoutY() + hintOffsetY);
        fields.add(lblUserName);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setLayoutX(layoutX);
        txtPassword.setLayoutY(txtUsername.getLayoutY() + fieldSpacing);
        txtPassword.setPrefWidth(width);
        fields.add(txtPassword);

        Label lblPassword = new Label("PASSWORD");
        lblPassword.getStyleClass().add("hint");
        lblPassword.setLayoutX(txtPassword.getLayoutX() + hintOffsetX);
        lblPassword.setLayoutY(txtPassword.getLayoutY() + hintOffsetY);
        fields.add(lblPassword);

        Button btnPrivacy = new Button();
        btnPrivacy.getStyleClass().add("toggle");
        btnPrivacy.setGraphic(new ImageView(new Image("privacyOn.png")));
        btnPrivacy.setLayoutX(txtPassword.getLayoutX() + width - fieldSpacing);
        btnPrivacy.setLayoutY(txtPassword.getLayoutY());
        fields.add(btnPrivacy);

        return fields;
    }

    private ArrayList<Node> createButtons() {
        ArrayList<Node> buttons = new ArrayList<>();

        Button btnLogin = new Button("Login");
        btnLogin.setLayoutX(400);
        btnLogin.setLayoutY(457);
        buttons.add(btnLogin);

        Button btnCreateAcct = new Button("Create Account");
        btnCreateAcct.setLayoutX(730);
        btnCreateAcct.setLayoutY(457);
        buttons.add(btnCreateAcct);

        return buttons;
    }

    private ArrayList<Node> createLabels() {
        ArrayList<Node> labels = new ArrayList<>();

        Label lblBanner = new Label("Welcome");
        lblBanner.getStyleClass().add("banner");
        lblBanner.setLayoutX(28);
        lblBanner.setLayoutY(107);
        labels.add(lblBanner);

        Label lblNeedAccount = new Label("Don't have an account?");
        lblNeedAccount.setLayoutX(516);
        lblNeedAccount.setLayoutY(465);
        labels.add(lblNeedAccount);

        Label lblInstructions = new Label("Sign in to manage your accounts.");
        lblInstructions.setLayoutX(120);
        lblInstructions.setLayoutY(465);
        labels.add(lblInstructions);

        Label lblLoginFail = new Label("Invalid username and/or password.");
        lblLoginFail.getStyleClass().add("warning");
        lblLoginFail.setLayoutX(500);
        lblLoginFail.setLayoutY(350);
        lblLoginFail.setPrefWidth(200);
        lblLoginFail.setVisible(false);
        labels.add(lblLoginFail);

        return labels;
    }

    private ArrayList<Node> loadImages() {
        ArrayList<Node> images = new ArrayList<>();

        ImageView imgBanner = new ImageView("dolphinLogin.png");
        imgBanner.setLayoutX(5);
        imgBanner.setLayoutY(90);
        imgBanner.setFitWidth(910);
        imgBanner.setFitHeight(180);
        images.add(imgBanner);

        ImageView imgLogo = new ImageView("dolphinLogoTan.png");
        imgLogo.setLayoutX(imgBanner.getLayoutX() + imgBanner.getFitWidth() - 220);
        imgLogo.setLayoutY(imgBanner.getLayoutY());
        imgLogo.setFitWidth(220);
        imgLogo.setFitHeight(imgBanner.getFitHeight());
        images.add(imgLogo);

        return images;
    }

    private Rectangle createPanel() {
        Rectangle panel = new Rectangle(109, 306, 379, 200);
        panel.getStyleClass().add("panel");
        return panel;
    }
}
