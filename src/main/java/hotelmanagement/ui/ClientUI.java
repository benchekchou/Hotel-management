package hotelmanagement.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management - Client");

        Label lblSearch = new Label("Search Rooms:");
        TextField txtSearch = new TextField();
        Button btnSearchRooms = new Button("Search");
        Button btnMakeReservation = new Button("Make Reservation");

        btnSearchRooms.setOnAction(event -> {
            // Logique pour rechercher des chambres
            String searchQuery = txtSearch.getText();
            System.out.println("Searching for rooms: " + searchQuery);
            // Appeler le service de recherche de chambres ici
        });

        btnMakeReservation.setOnAction(event -> {
            // Logique pour faire une réservation
            System.out.println("Making a reservation");
            // Appeler le service de réservation ici
        });

        VBox vbox = new VBox(lblSearch, txtSearch, btnSearchRooms, btnMakeReservation);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}