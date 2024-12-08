package hotelmanagement.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management - Employee");

        Button btnManageRooms = new Button("Manage Rooms");
        Button btnManageReservations = new Button("Manage Reservations");
        Button btnManageClients = new Button("Manage Clients");
        Button btnManagePayments = new Button("Manage Payments");

        VBox vbox = new VBox(btnManageRooms, btnManageReservations, btnManageClients, btnManagePayments);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}