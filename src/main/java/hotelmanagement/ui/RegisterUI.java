package hotelmanagement.ui;

import hotelmanagement.util.DatabaseUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Register");

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

        // Register Button
        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        btnRegister.setOnAction(event -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            if (register(username, password)) {
                showAlert("Registration Successful", "You have been registered successfully.");
                primaryStage.close();
            } else {
                showAlert("Registration Failed", "An error occurred during registration.");
            }
        });

        // VBox Layout
        VBox vbox = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, btnRegister);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}