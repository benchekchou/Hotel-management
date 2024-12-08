package hotelmanagement.ui;

import hotelmanagement.util.DatabaseUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // Labels
        Label lblUsername = new Label("Username:");
        lblUsername.setFont(new Font("Arial", 14));
        lblUsername.setTextFill(Color.DARKBLUE);

        Label lblPassword = new Label("Password:");
        lblPassword.setFont(new Font("Arial", 14));
        lblPassword.setTextFill(Color.DARKBLUE);

        // Text Fields
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Enter your username");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your password");

        // Login Button
        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        btnLogin.setOnAction(event -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            if (authenticate(username, password)) {
                DashboardUI dashboardUI = new DashboardUI();
                try {
                    dashboardUI.start(new Stage());
                    primaryStage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Invalid credentials", "The username or password you entered is incorrect.");
            }
        });

        // Register Button
        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");

        btnRegister.setOnAction(event -> {
            RegisterUI registerUI = new RegisterUI();
            try {
                registerUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // HBox Layout for buttons
        HBox hboxButtons = new HBox(10, btnLogin, btnRegister);
        hboxButtons.setAlignment(Pos.CENTER);

        // VBox Layout
        VBox vbox = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, hboxButtons);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}