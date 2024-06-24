module org.example.java_multithreading_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.java_multithreading_project to javafx.fxml;
    exports org.example.java_multithreading_project;
}