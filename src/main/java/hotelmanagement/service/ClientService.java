package hotelmanagement.service;



import hotelmanagement.model.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientService extends Remote {
    void addClient(Client client) throws RemoteException;
    Client authenticate(String username, String password) throws RemoteException;
    void deleteClient(int clientId) throws RemoteException;
    void updateClient(Client client) throws RemoteException;
    List<Client> getAllClients() throws RemoteException;
    Client getClientById(int clientId) throws RemoteException;
    List<Client> searchClientsByName(String name) throws RemoteException;
}