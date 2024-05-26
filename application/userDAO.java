package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {
	
	// setting database connectivity in 'verified_employees' database
	private static final String DB_URL = "jdbc:mysql://localhost:3306/verified_employees";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Jaycee@300130";
    
    // authenticate user from loggin in the program
    public boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM employee WHERE employee_ID = ? AND employee_password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }        
    
}
