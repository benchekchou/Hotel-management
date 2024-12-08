package hotelmanagement.ui;

import hotelmanagement.model.Room;
import hotelmanagement.service.RoomServiceImpl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.List;

public class ManageRoomsUI extends Application {
    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private RoomServiceImpl roomService;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Room Management");

        try {
            roomService = new RoomServiceImpl();
            loadRoomsFromDatabase();
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        // Create MenuBar
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");
        MenuItem dashboardItem = new MenuItem("Dashboard");
        dashboardItem.setOnAction(e -> {
            try {
                new DashboardUI().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            try {
                new LoginUI().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        menu.getItems().addAll(dashboardItem, logoutItem);
        menuBar.getMenus().add(menu);

        Label lblManageRooms = new Label("Room Management");
        lblManageRooms.styleProperty().set("-fx-font-size: 24px; -fx-font-weight: bold; text-align: center;");
        TextField txtType = new TextField();
        txtType.setPromptText("Type");
        TextField txtPrice = new TextField();
        txtPrice.setPromptText("Price");
        CheckBox chkAvailability = new CheckBox("Available");
        Button btnAdd = new Button("Add Room");

        TableView<Room> tableView = new TableView<>(rooms);
        tableView.setEditable(true);

        TableColumn<Room, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setType(event.getNewValue());
            updateRoomInDatabase(room);
        });

        TableColumn<Room, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setPrice(event.getNewValue());
            updateRoomInDatabase(room);
        });

        TableColumn<Room, Boolean> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(data -> data.getValue().availabilityProperty());
        availabilityColumn.setCellFactory(ComboBoxTableCell.forTableColumn(true, false));
        availabilityColumn.setOnEditCommit(event -> {
            Room room = event.getRowValue();
            room.setAvailability(event.getNewValue());
            updateRoomInDatabase(room);
        });

        TableColumn<Room, Void> deleteColumn = new TableColumn<>("Action");
        deleteColumn.setCellFactory(param -> new TableCell<Room, Void>() {
            private final Button btnDelete = new Button("Delete");

            {
                btnDelete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnDelete.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this room?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            deleteRoomFromDatabase(room.getId());
                            getTableView().getItems().remove(room);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });

        tableView.getColumns().addAll(typeColumn, priceColumn, availabilityColumn, deleteColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        btnAdd.setOnAction(e -> {
            String type = txtType.getText();
            String priceText = txtPrice.getText();
            boolean availability = chkAvailability.isSelected();

            if (type.isEmpty() || priceText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.");
                alert.showAndWait();
                return;
            }

            try {
                int price = Integer.parseInt(priceText);
                Room room = new Room(0, type, price, availability);
                try {
                    roomService.addRoom(room);
                    rooms.add(room);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                txtType.clear();
                txtPrice.clear();
                chkAvailability.setSelected(false);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Price must be a valid number.");
                alert.showAndWait();
            }
        });

        VBox vbox = new VBox(10, lblManageRooms, txtType, txtPrice, chkAvailability, btnAdd, tableView);
        vbox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(vbox);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadRoomsFromDatabase() {
        try {
            List<Room> roomList = roomService.getAllRooms();
            rooms.addAll(roomList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateRoomInDatabase(Room room) {
        try {
            roomService.updateRoom(room);

            // Log the room details for debugging
            System.out.println("Room updated: " + room);
            System.out.println("Availability: " + room.isAvailability());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void deleteRoomFromDatabase(int roomId) {
        try {
            roomService.deleteRoom(roomId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}