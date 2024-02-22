module org.obsys.obsysapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.obsys.obsysapp to javafx.fxml;
    exports org.obsys.obsysapp;
}