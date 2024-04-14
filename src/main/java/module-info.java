module com.example.worm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bkgroup.worm to javafx.fxml;
    exports com.bkgroup.worm;
}