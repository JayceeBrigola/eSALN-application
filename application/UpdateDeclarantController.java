package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateDeclarantController {
	
	@FXML private TextField EmployeeIDtxtField, DeclarationYeartxtField, DeclarantFullNametxtField, 
	CompAddtxtField, DeclarantPositiontxtField, DeclarantAgencytxtField, DeclarantOfficeAddtxtField;
	@FXML private Button NextButton, UpdateButton;
	@FXML private ChoiceBox<String> FilingTypeChoiceBox;
	
	// creating a new instance of salnDAO
	salnDAO salndao = new salnDAO();
	private String[] rowData;
	@SuppressWarnings("unused")
	private UpdateSpouseController updateSpouseController;

	
	// initialize the values in the Filing type choice box
	public void initialize() {
        setChoiceBoxChoices();       
    }
	
	// sets the values in the Filing type choice box
    private void setChoiceBoxChoices() {
        List<String> choices = Arrays.asList("Joint Filing", "Separate Filing", "Not Applicable");
        FilingTypeChoiceBox.getItems().addAll(choices);
    }
    
    public void initData(String[] rowData) {
    	this.rowData = rowData;
    	EmployeeIDtxtField.setText(rowData[0]);
    	DeclarationYeartxtField.setText(rowData[1]);   	
    	FilingTypeChoiceBox.setValue(rowData[2]);
    	DeclarantFullNametxtField.setText(rowData[3]);
    	CompAddtxtField.setText(rowData[4]);    	
    	DeclarantPositiontxtField.setText(rowData[5]);  	
    	DeclarantAgencytxtField.setText(rowData[6]);
    	DeclarantOfficeAddtxtField.setText(rowData[7]);
    }
	 
    // changes the scene to spouseScene
 	@FXML
 	private void NextButtonClicked(){
 		
 		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UpdateSpouseScene.fxml"));
            Parent FileSALNRoot = fxmlLoader.load();
            UpdateSpouseController updateSpouseController = fxmlLoader.getController();
            updateSpouseController.initData(rowData);
                       
            Scene FileSALNScene = new Scene(FileSALNRoot,700,400);
            Stage primaryStage = (Stage) NextButton.getScene().getWindow();
            primaryStage.setScene(FileSALNScene);
            primaryStage.show();         
            
           
		} catch (IOException e) {
            e.printStackTrace();
        }
     }
 	 	
 	@FXML
 	private void UpdateButtonClicked(){
 		
 		String EmployeeID = EmployeeIDtxtField.getText();
 		String declarationYear = DeclarationYeartxtField.getText();
 		String FilingType = FilingTypeChoiceBox.getValue().toString();
 		String declarantName = DeclarantFullNametxtField.getText();
 		String declarantAddress = CompAddtxtField.getText();
 		String declarantPosition = DeclarantPositiontxtField.getText();
 		String declarantAgency = DeclarantAgencytxtField.getText();
 		String declarantOfficeAdd = DeclarantOfficeAddtxtField.getText(); 	
 		 		
 		try {
             salnDAO.updateDataforDeclarant(EmployeeID, declarationYear, FilingType, declarantName, declarantAddress,
             		declarantPosition, declarantAgency, declarantOfficeAdd);       
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
 	
 	public void setUpdateSpouseController(UpdateSpouseController updateSpouseController) {
 	    this.updateSpouseController = updateSpouseController;
 	}

     

}
