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

public class LoginClientUI extends Application {
    private ClientServiceImpl clientService;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Login");
        try {
            clientService = new ClientServiceImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        Label lblTitle = new Label("Client Login");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Button btnAdminLogin = new Button("Admin Login");
        btnAdminLogin.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        btnAdminLogin.setOnAction(e -> {
            LoginUI loginUI = new LoginUI();
            loginUI.start(new Stage());
            primaryStage.close();
        });

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Enter your username");

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your password");

        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-size: 14px;");
        btnBack.setOnAction(e -> primaryStage.close());

        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.");
                alert.showAndWait();
                return;
            }
            try {
                Client client = clientService.getClientByUsernameAndPassword(username, password);
                if (client != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful!");
                    alert.showAndWait();
                    ClientDashboardUI clientDashboardUI = new ClientDashboardUI(client.getId());
                    clientDashboardUI.start(new Stage());
                    primaryStage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials.");
                    alert.showAndWait();
                }
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        });

        btnRegister.setOnAction(e -> {
            RegisterUIClient registerUIClient = new RegisterUIClient();
            registerUIClient.start(new Stage());
            primaryStage.close();
        });

        HBox hboxButtons = new HBox(10, btnLogin, btnRegister, btnAdminLogin, btnBack);
        hboxButtons.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(10, lblTitle, lblUsername, txtUsername, lblPassword, txtPassword, hboxButtons);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20px; -fx-background-color: #f2f2f2; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}