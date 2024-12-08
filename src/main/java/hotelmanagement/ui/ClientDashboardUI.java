package hotelmanagement.ui;

import hotelmanagement.model.Reservation;
import hotelmanagement.service.ReservationService;
import hotelmanagement.service.ReservationServiceImpl;
import hotelmanagement.util.DatabaseUtil;
import hotelmanagement.util.PrintUtil;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientDashboardUI extends Application {

    private int connectedClientId;
    private ComboBox<String> cmbRoomType;
    private DatePicker dpStartDate;
    private DatePicker dpEndDate;
    private Button btnReserve;
    private Button btnCheckAvailability;
    private Button btnViewReservations;
    private Button btnLogout;
    private TableView<Reservation> tableReservations;
    private ReservationService reservationService;

    public ClientDashboardUI() throws RemoteException {
        this.connectedClientId = 1; // Default client ID
        initializeService();
    }

    public ClientDashboardUI(int clientId) throws RemoteException {
        this.connectedClientId = clientId;
        initializeService();
    }

    private void initializeService() throws RemoteException {
        this.reservationService = new ReservationServiceImpl();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Dashboard");

        // UI Components
        Label lblRoomType = new Label("Room Type:");
        ComboBox<String> cmbRoomType = new ComboBox<>();
        cmbRoomType.setItems(FXCollections.observableArrayList(fetchRoomsFromDatabase()));

        Label lblStartDate = new Label("Start Date:");
        DatePicker dpStartDate = new DatePicker();

        Label lblEndDate = new Label("End Date:");
        DatePicker dpEndDate = new DatePicker();

        Button btnReserve = new Button("Reserve");
        Button btnCheckAvailability = new Button("Check Availability");
        Button btnViewReservations = new Button("View Reservations");
        Button btnLogout = new Button("Logout");

        // TableView and Columns
        TableView<Reservation> tableReservations = new TableView<>();
        TableColumn<Reservation, String> colRoomType = new TableColumn<>("Room Type");
        TableColumn<Reservation, LocalDate> colStartDate = new TableColumn<>("Start Date");
        TableColumn<Reservation, LocalDate> colEndDate = new TableColumn<>("End Date");
        TableColumn<Reservation, Void> colAction = new TableColumn<>("Action");

        // Configure Columns
        colRoomType.setCellValueFactory(cellData -> {
            int roomId = cellData.getValue().getRoomId();
            String roomType = getRoomTypeById(roomId);
            return new SimpleStringProperty(roomType);
        });
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Add action buttons to the table
        colAction.setCellFactory(param -> new TableCell<Reservation, Void>() {
            private final Button btnPrint = new Button("Print");

            {
                btnPrint.setOnAction(event -> {
                    Reservation reservation = (Reservation) getTableView().getItems().get(getIndex());
                    PrintUtil.printReservation(reservation);
                });
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    btnPrint.setDisable(!reservation.isConfirmed());
                    setGraphic(btnPrint);
                }
            }
        });

        // Add Columns to TableView
        tableReservations.getColumns().addAll(colRoomType, colStartDate, colEndDate, colAction);

        // Set the TableView to resize its columns proportionally
        tableReservations.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Button Actions
        btnReserve.setOnAction(event -> {
            String selectedRoomType = cmbRoomType.getValue();
            if (selectedRoomType == null || selectedRoomType.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please select a room type.");
                return;
            }
            int roomId = fetchRoomIdByType(selectedRoomType);
            if (roomId == -1) {
                showAlert(Alert.AlertType.WARNING, "No room found for the selected type.");
                return;
            }

            int clientId = connectedClientId;
            LocalDate startDate = dpStartDate.getValue();
            LocalDate endDate = dpEndDate.getValue();
            if (startDate == null || endDate == null) {
                showAlert(Alert.AlertType.WARNING, "Please select valid dates.");
                return;
            }

            try {
                reservationService.addReservation(new Reservation(0, roomId, clientId, startDate, endDate, true));
                showAlert(Alert.AlertType.INFORMATION, "Reservation added successfully.");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        btnViewReservations.setOnAction(event -> {
            try {
                List<Reservation> allReservations = reservationService.getAllReservations(connectedClientId);
                ObservableList<Reservation> reservations = FXCollections.observableArrayList(allReservations);
                tableReservations.setItems(reservations);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        btnCheckAvailability.setOnAction(event -> {
            String selectedRoomType = cmbRoomType.getValue();
            if (selectedRoomType == null || selectedRoomType.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please select a room type.");
                return;
            }
            int roomId = fetchRoomIdByType(selectedRoomType);
            if (roomId == -1) {
                showAlert(Alert.AlertType.WARNING, "No room found for the selected type.");
                return;
            }

            LocalDate startDate = dpStartDate.getValue();
            LocalDate endDate = dpEndDate.getValue();
            if (startDate == null || endDate == null) {
                showAlert(Alert.AlertType.WARNING, "Please select valid dates.");
                return;
            }

            try {
                boolean isAvailable = checkRoomAvailability(roomId, startDate, endDate);
                if (isAvailable) {
                    showAlert(Alert.AlertType.INFORMATION, "Room is available for the selected dates.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Room is not available for the selected dates.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnLogout.setOnAction(event -> {
            primaryStage.close();
        });

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(lblRoomType, 0, 0);
        gridPane.add(cmbRoomType, 1, 0);
        gridPane.add(lblStartDate, 0, 1);
        gridPane.add(dpStartDate, 1, 1);
        gridPane.add(lblEndDate, 0, 2);
        gridPane.add(dpEndDate, 1, 2);
        gridPane.add(btnReserve, 0, 3);
        gridPane.add(btnCheckAvailability, 1, 3);
        gridPane.add(btnViewReservations, 2, 3);
        gridPane.add(btnLogout, 3, 3);

        VBox vbox = new VBox(gridPane, tableReservations);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<String> fetchRoomsFromDatabase() {
        List<String> roomTypes = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT DISTINCT type FROM rooms";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                roomTypes.add(rs.getString("type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomTypes;
    }

    private String getRoomTypeById(int roomId) {
        String roomType = "";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT type FROM rooms WHERE id = " + roomId;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                roomType = rs.getString("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomType;
    }

    private int fetchRoomIdByType(String roomType) {
        int roomId = -1;
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id FROM rooms WHERE type = '" + roomType + "' LIMIT 1";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                roomId = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomId;
    }

    private boolean checkRoomAvailability(int roomId, LocalDate startDate, LocalDate endDate) {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = " + roomId +
                    " AND (start_date < '" + endDate + "' AND end_date > '" + startDate + "')";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}