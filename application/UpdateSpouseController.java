package application;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class UpdateSpouseController {
		
	@FXML private TextField SpouseNametxtField, SpouseAgencytxtField, 
	SpousePositiontxtField, OfficeAddresstxtField;
	@FXML private Button UpdateButton, DoneButton;
	
	private String employeeID;
    private String declarationYear;

		
	public void initData(String[] rowData) {	
		employeeID = rowData[0];
        declarationYear = rowData[1];
		// Display the values in the text fields		
		SpouseNametxtField.setText(rowData[8]);
		SpousePositiontxtField.setText(rowData[9]);
		SpouseAgencytxtField.setText(rowData[10]);
		OfficeAddresstxtField.setText(rowData[11]);      	
    }
    
	@FXML
 	private void UpdateButtonClicked(){
 		
 		String spouseName = SpouseNametxtField.getText();
 		String spousePosition = SpousePositiontxtField.getText();
 		String spouseAgency = SpouseAgencytxtField.getText();
 		String spouseOfficeAdd = OfficeAddresstxtField.getText();
 		
 		try {
             salnDAO.updateDataforSpouse(spouseName, spousePosition, spouseAgency, spouseOfficeAdd, employeeID, declarationYear);       
             showAlert("Update Successful", "The declarant information has been updated.");
             } catch (SQLException e) {
             e.printStackTrace();
             showAlert("Update Error", "An error occurred while updating the information.");
         }
     }
 	
 	private void showAlert(String title, String message) {
 	    Alert alert = new Alert(AlertType.INFORMATION);
 	    alert.setTitle(title);
 	    alert.setHeaderText(null);
 	    alert.setContentText(message);
 	    alert.showAndWait();
 	}
	
 	@FXML
    private void DoneButtonClicked() {
        Stage stage = (Stage) DoneButton.getScene().getWindow();
        stage.close();
    }
	

}
