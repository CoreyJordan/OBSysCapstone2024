package org.obsys.obsysapp.views;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Builder;

public class ViewBuilder implements Builder<AnchorPane> {

    /**
     * Creates a label node displaying a message at an x,y coordinate in the window.
     * Normal label css styles are applied.
     * @param contents The message to be displayed in the label.
     * @param x The horizontal position of the upper left corner of the label.
     * @param y The vertical axis position of the upper left corner of the label.
     * @return A string label at x,y.
     */
    protected Label obsysLabel(String contents, double x, double y) {
        Label label = new Label(contents);
        label.setLayoutX(x);
        label.setLayoutY(y);

        return label;
    }

    /**
     * Creates a label node displaying a message at an x,y coordinate in the window.
     * Sets a fixed field width for the label.
     * Normal label css styles are applied.
     * @param contents The message to be displayed in the label.
     * @param x The horizontal position of the upper left corner of the label.
     * @param y The vertical axis position of the upper left corner of the label.
     * @param width The horizontal field width to contain the text.
     * @return A string label at x,y.
     */
    protected Label obsysLabel(String contents, double x, double y, double width) {
        Label label = obsysLabel(contents, x, y);
        label.setPrefWidth(width);

        return label;
    }

    /**
     * Creates a label node displaying a message at an x,y coordinate in the window.
     * Css styles are applied according to the parameter.
     * @param contents The message to be displayed in the label.
     * @param x The horizontal position of the upper left corner of the label.
     * @param y The vertical axis position of the upper left corner of the label.
     * @param css The style class name assigned to the label.
     * @return A string label at x,y.
     */
    protected Label obsysLabel(String contents, double x, double y, String css) {
        Label label = obsysLabel(contents, x, y);
        label.getStyleClass().add(css);

        return label;
    }

    /**
     * Creates a label node displaying a message at an x,y coordinate in the window.
     * Sets a fixed field width for the label.
     * Css styles are applied according to the parameter.
     * @param contents The message to be displayed in the label.
     * @param x The horizontal position of the upper left corner of the label.
     * @param y The vertical axis position of the upper left corner of the label.
     * @param width The horizontal field width to contain the text.
     * @param css The style class name assigned to the label.
     * @return A string label at x,y.
     */
    protected Label obsysLabel(String contents, double x, double y, double width, String css) {
        Label label = obsysLabel(contents, x, y, width);
        label.getStyleClass().add(css);

        return label;
    }

    protected ImageView obsysImage(String source, double x, double y, double width, double height) {
        ImageView imageView = new ImageView(source);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        return imageView;
    }

    protected TextField obsysTextField (double x, double y, double width) {
        TextField textField = new TextField();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(width);

        return textField;
    }

    protected PasswordField obsysPassword(double x, double y, double width) {
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(x);
        passwordField.setLayoutY(y);
        passwordField.setPrefWidth(width);

        return passwordField;
    }

    protected void showPassword(PasswordField passwordField, TextField textfield) {
        passwordField.setDisable(true);
        passwordField.setVisible(false);
        textfield.setText(passwordField.getText());
        textfield.setVisible(true);
        textfield.setDisable(true);
    }

    protected void hidePassword(PasswordField passwordField, TextField textField) {
        passwordField.setDisable(false);
        passwordField.setVisible(true);
        textField.setText("");
        textField.setVisible(false);
        passwordField.requestFocus();
        passwordField.positionCaret(passwordField.getLength());
    }

    protected Button obsysButton(double x, double y) {
        Button button = new Button();
        button.setLayoutX(x);
        button.setLayoutY(y);

        return button;
    }

    protected Button obsysButton(String content, double x, double y) {
        Button button = obsysButton(x, y);
        button.setText(content);

        return button;
    }

    protected Button obsysButton(Image content, double x, double y) {
        Button button = obsysButton(x, y);
        button.setGraphic(new ImageView(content));

        return button;
    }

    protected Button obsysButton(Image content, double x, double y, String css) {
        Button button = obsysButton(content, x, y);
        button.getStyleClass().add(css);

        return button;
    }

    protected Rectangle obsysPanel(double x, double y, double width, double height, String css) {
        Rectangle panel = new Rectangle(x, y, width, height);
        panel.getStyleClass().add(css);

        return panel;
    }


    protected Hyperlink obsysLink(String content, double x, double y) {
        Hyperlink link = new Hyperlink(content);
        link.setLayoutX(x);
        link.setLayoutY(y);

        return link;
    }

    @Override
    public AnchorPane build() {
        return null;
    }
}
