module org.obsys.obsysapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires java.sql;
    requires org.junit.jupiter.api;


    opens org.obsys.obsysapp to javafx.fxml;
    exports org.obsys.obsysapp;
    exports org.obsys.obsysapp.testing;
}