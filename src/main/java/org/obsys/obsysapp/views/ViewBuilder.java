package org.obsys.obsysapp.views;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

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

    protected TextArea obsysTextArea (double x, double y, double width) {
        TextArea textField = new TextArea();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(width);

        return textField;
    }

    protected PasswordField obsysPassword(double x, double y) {
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(x);
        passwordField.setLayoutY(y);
        passwordField.setPrefWidth(300);

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

    /**
     * Creates a blank button.
     * @param x The horizontal position of the upper left corner of the button.
     * @param y The vertical position of the upper left corner of the button.
     * @return A blank button at the specified location.
     */
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

    /**
     * Creates a button displaying text at a specified location with a specified width.
     * @param content The text to be displayed in the button.
     * @param x The horizontal position of the upper left corner of the button.
     * @param y The vertical position of the upper left corner of the button.
     * @param width The horizontal size of the button, regardless of content.
     * @return The created button.
     */
    protected Button obsysButton(String content, double x, double y, double width) {
        Button button = obsysButton(content, x, y);
        button.setPrefWidth(width);

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

    /**
     * Creates a button with both textual content and graphical content. The default setting is that the graphic is
     * displayed left of the text.
     * @param content The text to be displayed in the button.
     * @param x The horizontal position of the upper left corner of the button.
     * @param y The vertical position of the upper left corner of the button.
     * @param width The preferred width of the button, regardless of content.
     * @param graphic The icon or image to be displayed in the button.
     * @return A graphical/textual button at a specified location within the window.
     */
    protected Button obsysButton(String content, double x, double y, double width, Image graphic) {
        Button button = obsysButton(content, x, y, width);
        ImageView icon = new ImageView(graphic);
        icon.setFitHeight(25);
        icon.setFitWidth(15);
        button.setGraphic(icon);

        return button;
    }


    /**
     * Creates a rounded node panel background for app controls. CSS styling is default set to 'panel'.
     * @param x the horizontal position of the upper left corner
     * @param y the vertical position of the upper left corner
     * @param width width of the panel
     * @param height height of the panel
     * @return a formatted and positioned rectangle
     */
    protected Rectangle obsysPanel(double x, double y, double width, double height) {
        Rectangle panel = new Rectangle(x, y, width, height);
        panel.getStyleClass().add("panel");

        return panel;
    }

    /**
     * Creates a hyperlink at a specified location within the window.
     * @param content The text to be displayed in the link.
     * @param x The horizontal position of the upper left corner of the link.
     * @param y The vertical position of the upper left corner of the link.
     * @return A hyperlink at a specified location within the window.
     */
    protected Hyperlink obsysLink(String content, double x, double y) {
        Hyperlink link = new Hyperlink(content);
        link.setLayoutX(x);
        link.setLayoutY(y);

        return link;
    }

    /**
     * Creates a hyperlink at a specified location within the window with specific css class.
     * @param content The text to be displayed in the link.
     * @param x The horizontal position of the upper left corner of the link.
     * @param y The vertical position of the upper left corner of the link.
     * @param css The style class to be applied to the link.
     * @return A hyperlink at a specified location within the window.
     */
    protected Hyperlink obsysLink(String content, double x, double y, String css) {

        Hyperlink link = obsysLink(content, x, y);
        link.getStyleClass().add(css);

        return link;
    }

    @Override
    public AnchorPane build() {
        AnchorPane window = new AnchorPane();
        window.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        return window;
    }

    protected ScrollPane obsysScrollPane(double x, double y, double width, double height) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(x);
        scrollPane.setLayoutY(y);
        scrollPane.setPrefWidth(width);
        scrollPane.setPrefHeight(height);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        return scrollPane;
    }

    protected ComboBox<LocalDate> obsysDateCombo(ArrayList<LocalDate> choices, double x, double y, double width) {
        ComboBox<LocalDate> comboBox = new ComboBox<>();
        comboBox.setLayoutX(x);
        comboBox.setLayoutY(y);
        comboBox.setPrefWidth(width);
        comboBox.getItems().addAll(choices);

        return comboBox;
    }


    protected ComboBox<String> obsysStringCombo(ArrayList<String> choices, double x, double y, double width) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setLayoutX(x);
        comboBox.setLayoutY(y);
        comboBox.setPrefWidth(width);
        comboBox.getItems().addAll(choices);

        return comboBox;
    }

    protected DatePicker obsysDatePicker(double x, double y, double width) {
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setLayoutX(x);
        datePicker.setLayoutY(y);
        datePicker.setPrefWidth(width);

        return datePicker;
    }

}
