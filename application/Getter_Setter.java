package application;

public class Getter_Setter {
	
	private static Getter_Setter instance;

    private String employeeID;
    private String declarationYear;

    // Private constructor to prevent instantiation
    private Getter_Setter() {
    	
    }

    public static Getter_Setter getInstance() {
        if (instance == null) {
            instance = new Getter_Setter();
        }
        return instance;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getDeclarationYear() {
        return declarationYear;
    }

    public void setDeclarationYear(String declarationYear) {
        this.declarationYear = declarationYear;
    }

}
