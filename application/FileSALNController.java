package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FileSALNController {
	
	@FXML private TextField EmployeeIDtxtField, DeclarationYeartxtField, DeclarantFullNametxtField, 
	CompAddtxtField, DeclarantPositiontxtField, DeclarantAgencytxtField, DeclarantOfficeAddtxtField;
	@FXML private Button NextButton;
	@FXML private ChoiceBox<String> FilingTypeChoiceBox;
	
	// creating a new instance of salnDAO
	salnDAO salndao = new salnDAO();
	
	// initialize the values in the Filing type choice box
	public void initialize() {
        setChoiceBoxChoices();
    }
	
	// sets the values in the Filing type choice box
    private void setChoiceBoxChoices() {
        List<String> choices = Arrays.asList("Joint Filing", "Separate Filing", "Not Applicable");
        FilingTypeChoiceBox.getItems().addAll(choices);
    }
	 
    // saves the entered data to the database and will change the scene to spouseScene
	@FXML
	private void NextButtonClicked(){
		
		String EmployeeID = EmployeeIDtxtField.getText();
		String declarationYear = DeclarationYeartxtField.getText();
		String FilingType = FilingTypeChoiceBox.getValue().toString();
		String declarantName = DeclarantFullNametxtField.getText();
		String declarantAddress = CompAddtxtField.getText();
		String declarantPosition = DeclarantPositiontxtField.getText();
		String declarantAgency = DeclarantAgencytxtField.getText();
		String declarantOfficeAdd = DeclarantOfficeAddtxtField.getText();
		
		Getter_Setter dataModel = Getter_Setter.getInstance();
	    dataModel.setEmployeeID(EmployeeID);
	    dataModel.setDeclarationYear(declarationYear);
		
		try {
            salnDAO.saveDataforDeclarant(EmployeeID, declarationYear, FilingType, declarantName, declarantAddress,
            		declarantPosition, declarantAgency, declarantOfficeAdd);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SpouseScene.fxml"));
            Parent FileSALNRoot = fxmlLoader.load();
                                               
            Scene FileSALNScene = new Scene(FileSALNRoot, 700, 400);
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
