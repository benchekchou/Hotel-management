package hotelmanagement.service;

import hotelmanagement.model.Client;
import hotelmanagement.util.DatabaseUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientServiceImpl extends UnicastRemoteObject implements ClientService {
    public ClientServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void addClient(Client client) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO clients (name, contact_info, password, phone_number) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, client.getName());
            statement.setString(2, client.getContactInfo());
            statement.setString(3, client.getPassword());
            statement.setString(4, client.getPhoneNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client authenticate(String username, String password) throws RemoteException {
        Client client = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM clients WHERE name = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_info"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void deleteClient(int clientId) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "DELETE FROM clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, clientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateClient(Client client) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "UPDATE clients SET name = ?, contact_info = ?, password = ?, phone_number = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, client.getName());
            statement.setString(2, client.getContactInfo());
            statement.setString(3, client.getPassword());
            statement.setString(4, client.getPhoneNumber());
            statement.setInt(5, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Client> getAllClients() throws RemoteException {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM clients";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_info"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client getClientById(int clientId) throws RemoteException {
        Client client = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_info"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public List<Client> searchClientsByName(String name) throws RemoteException {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM clients WHERE name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_info"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public Client getClientByUsernameAndPassword(String username, String password) throws RemoteException {
        String query = "SELECT * FROM clients WHERE name = ? AND password = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_info"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
