package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignInController {
	
	@FXML private TextField EmpNum;
	@FXML private PasswordField EmpPass;
	@FXML private Button SignInButton;
	@FXML private Label errorLabel;
	
	// Declare userDAO object
	private userDAO userDao; 
	
	// Instantiate userDAO object
	public SignInController() {
		userDao = new userDAO(); 
	}
	
	// changes the scene to Employee dashboard scene once the program grants access
	@FXML
	private void SignInButtonClicked (){
		String EmployeeID = EmpNum.getText();
		String password = EmpPass.getText();
		
		if(userDao.authenticateUser(EmployeeID, password)) { 
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeDashboardScene.fxml"));
	            Parent dashboardRoot = fxmlLoader.load();	
	            
	            dashboardController dashboardController = fxmlLoader.getController();
	            dashboardController.setUserData(EmployeeID);
	            
	            Scene dashboardScene = new Scene(dashboardRoot,700,400);
	            Stage primaryStage = (Stage) EmpNum.getScene().getWindow();
	            primaryStage.setScene(dashboardScene);
	            primaryStage.show();
	            
			} catch (IOException e) {
	            e.printStackTrace();
	        }
		} else {
			errorLabel.setText("Invalid Employee ID or password!");
		}
	}
}
	

