package hotelmanagement.ui;

import hotelmanagement.model.Employe;
import hotelmanagement.service.EmployeServiceImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class AddEmployeeUI extends Application {

    private final EmployeServiceImpl employeService;

    {
        try {
            employeService = new EmployeServiceImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Employee Form");

        // Create Labels for the form
        Label lblUsername = new Label("Username:");
        Label lblPassword = new Label("Password:");

        // Create TextFields for user input
        TextField txtUsername = new TextField();
        TextField txtPassword = new TextField();

        // Create a button to submit the form
        Button btnSubmit = new Button("Submit");

        // Handle button action to add employee to database
        btnSubmit.setOnAction(event -> {
            // Get the input values from the form
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            // Create an Employe object with the form data
            Employe employe = new Employe(0, username, password); // EmpId will be auto-generated in the DB

            try {
                // Use EmployeService to add employee to the database
                employeService.addEmploye(employe);

                // Clear the form after submission
                txtUsername.clear();
                txtPassword.clear();

                System.out.println("Employee Added: " + username); // Optionally log the successful addition
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("Error while adding employee.");
            }
        });

        // Arrange the components in a GridPane for a form-like layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows
        gridPane.add(lblUsername, 0, 0); // Label in row 0, column 0
        gridPane.add(txtUsername, 1, 0); // TextField in row 0, column 1
        gridPane.add(lblPassword, 0, 1); // Label in row 1, column 0
        gridPane.add(txtPassword, 1, 1); // TextField in row 1, column 1
        gridPane.add(btnSubmit, 1, 2);    // Button in row 2, column 1

        // Add the GridPane layout to a VBox container
        VBox vbox = new VBox(10, gridPane); // 10px spacing between elements in VBox

        // Set up the scene and show the stage
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
