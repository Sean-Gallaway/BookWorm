module com.example.worm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.bkgroup.worm to javafx.fxml;
    exports com.bkgroup.worm;
    exports com.bkgroup.worm.utils;
    opens com.bkgroup.worm.utils to javafx.fxml;
}