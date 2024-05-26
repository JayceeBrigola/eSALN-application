package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SpouseController {
		
	@FXML private TextField SpouseNametxtField, SpouseAgencytxtField, 
	SpousePositiontxtField, OfficeAddresstxtField;
	@FXML private Button NextButton;
	
    // saves the entered data to the database and will change the scene to ChildrenScene
	@FXML
	private void NextButtonClicked() {
		
		String spouseName = SpouseNametxtField.getText();
		String spouseAgency = SpouseAgencytxtField.getText();
		String spousePosition = SpousePositiontxtField.getText();
		String spouseOfficeAdd = OfficeAddresstxtField.getText();

		try {
			salnDAO.saveDataforSpouse(spouseName, spouseAgency, spousePosition, spouseOfficeAdd);
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChildrenScene.fxml"));
            Parent FileSALNRoot = fxmlLoader.load();	
            
            Scene FileSALNScene = new Scene(FileSALNRoot,700,400);
            Stage primaryStage = (Stage) NextButton.getScene().getWindow();
            primaryStage.setScene(FileSALNScene);
            primaryStage.show();            
		} catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
