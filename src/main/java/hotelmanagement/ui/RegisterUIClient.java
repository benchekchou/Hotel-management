package hotelmanagement.ui;

import hotelmanagement.model.Client;
import hotelmanagement.service.ClientServiceImpl;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class RegisterUIClient extends Application {
    private ClientServiceImpl clientService;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Registration");

        try {
            clientService = new ClientServiceImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        Label lblTitle = new Label("Client Registration");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label lblName = new Label("Name:");
        TextField txtName = new TextField();
        txtName.setPromptText("Enter your name");

        Label lblContactInfo = new Label("Contact Info:");
        TextField txtContactInfo = new TextField();
        txtContactInfo.setPromptText("Enter your contact info");

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your password");

        Label lblPhoneNumber = new Label("Phone Number:");
        TextField txtPhoneNumber = new TextField();
        txtPhoneNumber.setPromptText("Enter your phone number");

        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-size: 14px;");
        btnBack.setOnAction(e -> {
            LoginClientUI loginClientUI = new LoginClientUI();
            loginClientUI.start(new Stage());
            primaryStage.close();
        });

        btnRegister.setOnAction(e -> {
            String name = txtName.getText();
            String contactInfo = txtContactInfo.getText();
            String password = txtPassword.getText();
            String phoneNumber = txtPhoneNumber.getText();

            if (name.isEmpty() || contactInfo.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.");
                alert.showAndWait();
                return;
            }

            Client client = new Client(0, name, contactInfo, password, phoneNumber);
            try {
                clientService.addClient(client);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Client registered successfully!");
                alert.showAndWait();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        });

        HBox hboxButtons = new HBox(10, btnRegister, btnBack);
        hboxButtons.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, lblTitle, lblName, txtName, lblContactInfo, txtContactInfo, lblPassword, txtPassword, lblPhoneNumber, txtPhoneNumber, hboxButtons);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20px; -fx-background-color: #f2f2f2; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}