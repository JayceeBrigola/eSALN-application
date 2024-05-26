package application;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RUD_Controller {
	
	@FXML private TextField EmployeeIDtxtField, DeclarationYeartxtField;
	@FXML private ChoiceBox<String> tableChoiceBox;
	@FXML private Button SeachButton, DeleteButton, DoneButton;
	@FXML private TableView<String[]> resultTable;    
	
	private salnDAO SalnDAO;	
	private UpdateSpouseController updateSpouseController;

	// initialize the scene
    public void initialize() {
        SalnDAO = new salnDAO();
        setChoiceBoxChoices();
        setupTableClickListener();          
    } 
 	
 	// sets the values in the table choice box
     private void setChoiceBoxChoices() {
         List<String> choices = Arrays.asList("declarant", "unmarriedchildrenbelow18", "realproperty", 
        		 "personalproperty", "liability", "summary");
         tableChoiceBox.getItems().addAll(choices);
     }
    
     //search records when the search button is clicked
     @FXML
     public void searchButtonClicked() {
         String employeeId = EmployeeIDtxtField.getText();
         String year = DeclarationYeartxtField.getText();
         String tableName = tableChoiceBox.getValue();

         if (tableName == null || employeeId.isEmpty() || year.isEmpty()) {
             showAlert("Missing Information!", "Please fill in all the required fields.");
         } else {
             SalnDAO.searchSalnData(employeeId, year, resultTable, tableName);
         }
     }

     // sets table click listener
     private void setupTableClickListener() {
 	    resultTable.setOnMouseClicked(event -> {
 	        if (event.getClickCount() == 2) {
 	            String[] rowData = resultTable.getSelectionModel().getSelectedItem();
 	            if (rowData != null) {
 	                openNewScene(rowData);
 	            }
 	        }
 	    });
     }

     // opens a scene based on the table selected
     private void openNewScene(String[] rowData) {
	 	 String tableName = tableChoiceBox.getValue();
	
	 	 try {
	 		 FXMLLoader loader = null;
		         if (tableName.equals("declarant")) {
	             loader = new FXMLLoader(getClass().getResource("UpdateFileSALNScene.fxml"));
	         } else if (tableName.equals("unmarriedchildrenbelow18")) {
	             loader = new FXMLLoader(getClass().getResource("UpdateChildrenScene.fxml"));
	         } else if (tableName.equals("realproperty")) {
	             loader = new FXMLLoader(getClass().getResource("UpdateRealPropertyScene.fxml"));
	         } else if (tableName.equals("personalproperty")) {
	             loader = new FXMLLoader(getClass().getResource("UpdatePersonalPropertyScene.fxml"));
	         } else if (tableName.equals("liability")) {
	             loader = new FXMLLoader(getClass().getResource("UpdateLiabilityScene.fxml"));
	         }
	         
	         if (loader != null) {
	             Parent newSceneRoot = loader.load();
	             
	             if (tableName.equals("declarant")) {
	                 UpdateDeclarantController updateDeclarantController = loader.getController();
	                 updateDeclarantController.initData(rowData);
	                 updateDeclarantController.setUpdateSpouseController(updateSpouseController);
	             } else if (tableName.equals("unmarriedchildrenbelow18")) {
	            	 UpdateChildrenController updateChildrenController = loader.getController();
	                 updateChildrenController.initDataFromTable(rowData);
	             } else if (tableName.equals("realproperty")) {
	            	 UpdateRealPropertyController updateRealPropertyController = loader.getController();
	            	 updateRealPropertyController.initDataFromTable(rowData);
	             } else if (tableName.equals("personalproperty")) {
	            	 UpdatePersonalPropertyController updatePersonalPropertyController = loader.getController();
	            	 updatePersonalPropertyController.initDataFromTable(rowData);
	             } else if (tableName.equals("liability")) {
		            	 UpdateLiabilityController updateLiabilityController = loader.getController();
		            	 updateLiabilityController.initDataFromTable(rowData);
		             } 
		            	 
		             
		             Stage newStage = new Stage();
		             newStage.setScene(new Scene(newSceneRoot));
		             newStage.show();
		         }
		     } catch (IOException e) {
		         e.printStackTrace();
		     }
	  }          
     
    
     // exits the program
     @FXML
     private void DoneButtonClicked() {
         Stage stage = (Stage) DoneButton.getScene().getWindow();
         stage.close();
     }
     
     // sets Spouse Controller
     public void setUpdateSpouseController(UpdateSpouseController updateSpouseController) {
 	    this.updateSpouseController = updateSpouseController;
     }
     
     // sets alert box
     private void showAlert(String title, String message) {
         Alert alert = new Alert(AlertType.INFORMATION);
         alert.setTitle(title);
         alert.setHeaderText(null);
         alert.setContentText(message);
         alert.showAndWait();
     }   
     
     @FXML
     private void deleteButtonClicked() {
         String tableName = tableChoiceBox.getValue();
         String[] rowData = resultTable.getSelectionModel().getSelectedItem();

         if (tableName == null || rowData == null) {
             showAlert("Selection Error", "Please select a record from the table.");
             return;
         }

         Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.setTitle("Confirmation");
         alert.setHeaderText("Delete Record");
         alert.setContentText("Are you sure you want to delete the selected record?");

         Optional<ButtonType> result = alert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
             boolean deleted = SalnDAO.deleteRecord(tableName, rowData);
             if (deleted) {
                 showAlert("Delete Success", "The selected record is successfully deleted.");
             } else {
                 showAlert("Delete Error", "An error occurred while deleting the record.");
             }
         }
     }

     
}
     
     


	

