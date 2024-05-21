module com.bkgroup.worm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.bkgroup.worm to javafx.fxml;
    opens com.bkgroup.worm.controllers to javafx.fxml;
    exports com.bkgroup.worm;
    exports com.bkgroup.worm.utils;
    exports com.bkgroup.worm.controllers;
    opens com.bkgroup.worm.utils to javafx.fxml;
}