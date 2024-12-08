package hotelmanagement.ui;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard");



        JFXButton btnAddClient = new JFXButton("Add Client");
        JFXButton btnAddEmployee = new JFXButton("Add Employee");
        JFXButton btnManageRooms = new JFXButton("Manage Rooms");
        JFXButton btnManageReservations = new JFXButton("Manage Reservations");
        JFXButton btnLogout = new JFXButton("Logout");

        btnAddClient.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        btnAddEmployee.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        btnManageRooms.setStyle("-fx-background-color: #ffc107; -fx-text-fill: white;");
        btnManageReservations.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
        btnLogout.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");



        btnAddEmployee.setOnAction(event -> {
            System.out.println("Adding an employee...");
            AddEmployeeUI addEmployeeUI = new AddEmployeeUI();
            try {
                addEmployeeUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnManageRooms.setOnAction(event -> {
            System.out.println("Managing rooms...");
            ManageRoomsUI manageRoomsUI = new ManageRoomsUI();
            try {
                manageRoomsUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnManageReservations.setOnAction(event -> {
            System.out.println("Managing reservations...");
            ManageReservationsUI manageReservationsUI = new ManageReservationsUI();
            try {
                manageReservationsUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnLogout.setOnAction(event -> {
            System.out.println("Logging out...");
            primaryStage.close();
            LoginUI loginUI = new LoginUI();
            try {
                loginUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        HBox hboxButtons = new HBox(10, btnLogout, btnAddClient, btnAddEmployee, btnManageRooms, btnManageReservations);
        hboxButtons.setPadding(new Insets(10));
        hboxButtons.setAlignment(Pos.CENTER); // Center the buttons
        VBox vbox = new VBox(10, hboxButtons);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}