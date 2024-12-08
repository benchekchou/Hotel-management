package hotelmanagement;


import hotelmanagement.ui.LoginClientUI;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        String userType = getUserType();


            Application.launch(LoginClientUI.class, args);

    }

    private static String getUserType() {
        // Implémentez la logique pour déterminer le type d'utilisateur
        // Par exemple, lire à partir d'un fichier de configuration ou d'une base de données
        return "admin"; // Remplacez par la logique réelle
    }
}