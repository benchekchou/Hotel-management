package hotelmanagement.ui;

import hotelmanagement.model.Reservation;
import hotelmanagement.service.ReservationService;
import hotelmanagement.service.ReservationServiceImpl;
import hotelmanagement.util.DatabaseUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageReservationsUI extends Application {

    private ReservationService reservationService;
    private TableView<Reservation> table;
    private ObservableList<Reservation> reservationList;
    private TextField searchField;

    @Override
    public void start(Stage primaryStage) {
        try {
            reservationService = new ReservationServiceImpl(); // Initialize the reservationService with the concrete implementation
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        primaryStage.setTitle("Manage Reservations");

        table = new TableView<>();
        reservationList = FXCollections.observableArrayList();
        table.setItems(reservationList);
        table.setEditable(true);

        // Define table columns
        TableColumn<Reservation, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        TableColumn<Reservation, Integer> roomIdColumn = new TableColumn<>("Room ID");
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roomIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        TableColumn<Reservation, Integer> clientIdColumn = new TableColumn<>("Client ID");
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        TableColumn<Reservation, LocalDate> checkInDateColumn = new TableColumn<>("Check-In Date");
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkInDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        TableColumn<Reservation, LocalDate> checkOutDateColumn = new TableColumn<>("Check-Out Date");
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        checkOutDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        TableColumn<Reservation, Boolean> confirmedColumn = new TableColumn<>("Confirmed");
        confirmedColumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        confirmedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(confirmedColumn));

        TableColumn<Reservation, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<Reservation, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    deleteReservation(reservation);
                    showNotification("Reservation deleted successfully!");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionButtons = new HBox(deleteButton);
                    setGraphic(actionButtons);
                }
            }
        });

        table.getColumns().addAll(idColumn, roomIdColumn, clientIdColumn, checkInDateColumn, checkOutDateColumn, confirmedColumn, actionColumn);

        Button addButton = new Button("Add Reservation");
        addButton.setOnAction(e -> {
            showAddReservationDialog();
            showNotification("Reservation added successfully!");
        });

        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterReservations(newValue));

        VBox vbox = new VBox(searchField, table, addButton);
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Fetch and display all reservations
        loadAllReservations();
    }

    private void loadAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations(0); // Fetch all reservations
            reservationList.clear();
            reservationList.addAll(reservations);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showAddReservationDialog() {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Add Reservation");

        Label roomIdLabel = new Label("Room:");
        ComboBox<String> roomComboBox = new ComboBox<>();
        roomComboBox.setItems(FXCollections.observableArrayList(fetchRoomsFromDatabase()));

        Label clientIdLabel = new Label("Client:");
        ComboBox<String> clientComboBox = new ComboBox<>();
        clientComboBox.setItems(FXCollections.observableArrayList(fetchClientsFromDatabase()));

        Label checkInDateLabel = new Label("Check-In Date:");
        DatePicker checkInDatePicker = new DatePicker();
        Label checkOutDateLabel = new Label("Check-Out Date:");
        DatePicker checkOutDatePicker = new DatePicker();
        Label confirmedLabel = new Label("Confirmed:");
        CheckBox confirmedCheckBox = new CheckBox();

        VBox vbox = new VBox(roomIdLabel, roomComboBox, clientIdLabel, clientComboBox, checkInDateLabel, checkInDatePicker, checkOutDateLabel, checkOutDatePicker, confirmedLabel, confirmedCheckBox);
        dialog.getDialogPane().setContent(vbox);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String selectedRoom = roomComboBox.getValue();
                String selectedClient = clientComboBox.getValue();
                int roomId = Integer.parseInt(selectedRoom.split(" ")[1]);
                int clientId = Integer.parseInt(selectedClient.split(" ")[1]);
                LocalDate checkInDate = checkInDatePicker.getValue();
                LocalDate checkOutDate = checkOutDatePicker.getValue();
                boolean confirmed = confirmedCheckBox.isSelected();
                return new Reservation(0, roomId, clientId, checkInDate, checkOutDate, confirmed);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(this::addReservation);
    }

    private List<String> fetchRoomsFromDatabase() {
        List<String> rooms = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id, type FROM rooms";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int roomId = rs.getInt("id");
                String roomType = rs.getString("type");
                rooms.add("Room " + roomId + " - " + roomType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    private List<String> fetchClientsFromDatabase() {
        List<String> clients = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id, name FROM clients";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int clientId = rs.getInt("id");
                String clientName = rs.getString("name");
                clients.add("Client " + clientId + " - " + clientName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    private void addReservation(Reservation reservation) {
        reservationList.add(reservation);
        try {
            reservationService.addReservation(reservation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteReservation(Reservation reservation) {
        reservationList.remove(reservation);
        try {
            reservationService.deleteReservation(reservation.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void filterReservations(String searchText) {
        List<Reservation> filteredList = reservationList.stream()
                .filter(reservation -> String.valueOf(reservation.getClientId()).contains(searchText) ||
                        String.valueOf(reservation.getRoomId()).contains(searchText) ||
                        reservation.getCheckInDate().toString().contains(searchText) ||
                        reservation.getCheckOutDate().toString().contains(searchText))
                .collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(filteredList));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
