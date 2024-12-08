package hotelmanagement.model;

public class Employe {
    private int empId;
    private String empUserName;
    private String empPassword;

    public Employe(int empId, String empUserName, String empPassword) {
        this.empId = empId;
        this.empUserName = empUserName;
        this.empPassword = empPassword;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public void setEmpUserName(String empUserName) {
        this.empUserName = empUserName;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getEmpUserName() {
        return empUserName;
    }

    public String getEmpPassword() {
        return empPassword;
    }
}
