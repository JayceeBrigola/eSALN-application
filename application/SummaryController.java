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

public class SummaryController {
	
	@FXML private Button doneButton;
	@FXML private TextField SummEmpIDtxtField, SummYeartxtField, RealPropSubtotaltxtField,
	PerPropSubtotaltxtField, TotalLiabilitiestxtField, TotalAssetstxtField, NetWorthtxtField;
	
	public void initialize() {
        // Retrieve the values from the SALNDataModel
        Getter_Setter dataModel = Getter_Setter.getInstance();
        String employeeID = dataModel.getEmployeeID();
        String declarationYear = dataModel.getDeclarationYear();

        // Set the values in the text fields
        SummEmpIDtxtField.setText(employeeID);
        SummYeartxtField.setText(declarationYear);
        
        // Calculations for the summary table
        try {
            int RPsum = salnDAO.getSumOfRealPropertyAcqCost(employeeID, declarationYear);
            RealPropSubtotaltxtField.setText(String.valueOf(RPsum));
            
            int PPsum = salnDAO.getSumOfPersonalPropertyAcqCost(employeeID, declarationYear);
            PerPropSubtotaltxtField.setText(String.valueOf(PPsum));
            
            int LiabilitySum = salnDAO.getSumOfOutstandingBalance(employeeID, declarationYear);
            TotalLiabilitiestxtField.setText(String.valueOf(LiabilitySum));
            
            int TotalAsset = RPsum + PPsum;
            TotalAssetstxtField.setText(String.valueOf(TotalAsset));
            
            double NetWorth = TotalAsset - LiabilitySum;
            NetWorthtxtField.setText(String.valueOf(NetWorth));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }       
    
    }
	
	@FXML
	private void doneButtonClicked() throws SQLException {
		try {
			
            int RPsum = Integer.parseInt(RealPropSubtotaltxtField.getText());
            int PPsum = Integer.parseInt(PerPropSubtotaltxtField.getText());
            int liabilitySum = Integer.parseInt(TotalLiabilitiestxtField.getText());
            int totalAsset = Integer.parseInt(TotalAssetstxtField.getText());
            double networth = Double.parseDouble(NetWorthtxtField.getText());

            salnDAO.saveDataforSummary(RPsum, PPsum, liabilitySum, totalAsset, networth);
            
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeDashboardScene.fxml"));
            Parent FileSALNRoot = fxmlLoader.load();	
            Scene employeeDashboardScene = new Scene(FileSALNRoot, 700, 400);
            Stage primaryStage = (Stage) doneButton.getScene().getWindow();
            
            dashboardController dashboardController = fxmlLoader.getController();
            Getter_Setter dataModel = Getter_Setter.getInstance();
            String employeeID = dataModel.getEmployeeID();
            dashboardController.setUserData(employeeID);

            primaryStage.setScene(employeeDashboardScene);
            primaryStage.show();            
		} catch (IOException e) {
            e.printStackTrace();
        }
	}

}
