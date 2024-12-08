package hotelmanagement.service;

import hotelmanagement.model.Employe;
import hotelmanagement.util.DatabaseUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeServiceImpl extends UnicastRemoteObject implements EmployeService {

    public EmployeServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void addEmploye(Employe employe) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, employe.getEmpUserName());
            statement.setString(2, employe.getEmpPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employe authenticate(String username, String password) throws RemoteException {
        Employe employe = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM users WHERE emp_user_name = ? AND emp_password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employe = new Employe(
                        resultSet.getInt("emp_id"),
                        resultSet.getString("emp_user_name"),
                        resultSet.getString("emp_password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employe;
    }

    @Override
    public void deleteEmploye(int empId) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "DELETE FROM users WHERE emp_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEmploye(Employe employe) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "UPDATE users SET emp_user_name = ?, emp_password = ? WHERE emp_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, employe.getEmpUserName());
            statement.setString(2, employe.getEmpPassword());
            statement.setInt(3, employe.getEmpId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employe> getAllEmployes() throws RemoteException {
        List<Employe> employes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employe employe = new Employe(
                        resultSet.getInt("emp_id"),
                        resultSet.getString("emp_user_name"),
                        resultSet.getString("emp_password")
                );
                employes.add(employe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    @Override
    public Employe getEmployeById(int empId) throws RemoteException {
        Employe employe = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employe = new Employe(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employe;
    }

    @Override
    public List<Employe> searchEmployesByUserName(String userName) throws RemoteException {
        List<Employe> employes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM users WHERE username LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + userName + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employe employe = new Employe(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                employes.add(employe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    // Additional helper method to authenticate by username and password (can be used in login)
    public Employe getEmployeByUsernameAndPassword(String username, String password) throws RemoteException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Employe(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
