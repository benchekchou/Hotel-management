package hotelmanagement.service;

import hotelmanagement.model.Employe;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EmployeService extends Remote {
    // Method to add an employee to the system
    void addEmploye(Employe employe) throws RemoteException;

    // Method to authenticate an employee by username and password
    Employe authenticate(String username, String password) throws RemoteException;

    // Method to delete an employee by their ID
    void deleteEmploye(int empId) throws RemoteException;

    // Method to update the details of an employee
    void updateEmploye(Employe employe) throws RemoteException;

    // Method to get a list of all employees
    List<Employe> getAllEmployes() throws RemoteException;

    // Method to fetch an employee by their ID
    Employe getEmployeById(int empId) throws RemoteException;

    // Method to search for employees by their username (or name if you prefer)
    List<Employe> searchEmployesByUserName(String userName) throws RemoteException;
}
