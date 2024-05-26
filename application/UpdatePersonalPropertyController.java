package application;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UpdatePersonalPropertyController {
	
	private int paneCount = 0;
	private GridPane gridPane;
    private int latestPersonalPropertyID; // Latest ID from the database

	@FXML private Button updateButton, doneButton;
	@FXML private AnchorPane scrollPaneAnchor;
	@FXML private ScrollPane scrollPane;
	@FXML private Pane paneLabels;
	
	@FXML
    public void initialize() {	
		
		if (paneLabels != null) {
	        double[] labelLayoutX = {18, 158, 280, 400};

	        ObservableList<Node> children = paneLabels.getChildren();

	        for (int i = 0; i < children.size() && i < labelLayoutX.length; i++) {
	            Node child = children.get(i);
	            if (child instanceof Label) {
	                Label label = (Label) child;
	                label.setLayoutX(labelLayoutX[i]); 
	            }
	        }
	    }		
			    
		Group contentGroup = new Group(scrollPaneAnchor);
		
		scrollPaneAnchor.setPrefWidth(615);
        scrollPaneAnchor.setPrefHeight(500);
        scrollPane.setVmax(1200); 
        scrollPane.setPrefViewportHeight(100); 
        scrollPane.setVvalue(0);  
        scrollPane.setContent(contentGroup);
		
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        latestPersonalPropertyID = salnDAO.getLatestPersonalPropertyIDFromDatabase();
	}
	
	@FXML
	private void addNewPane() {
		gridPane = new GridPane();
		gridPane.setLayoutX(45);
		gridPane.setLayoutY(30);
	    gridPane.setHgap(25);
	    
	    TextField PersonalPropertyID = new TextField();
	    TextField PersonalPropDesc = new TextField();
	    TextField YearAcquired = new TextField();
	    TextField Acq_Cost = new TextField();
	    
	    PersonalPropertyID.setPrefSize(50, 20);
	    YearAcquired.setPrefSize(50, 20);
	    	    
	    ImageView deleteButton = createDeleteButton(gridPane);
	    gridPane.add(deleteButton, 0, 0);

	    gridPane.addRow(0, PersonalPropertyID, PersonalPropDesc, YearAcquired, Acq_Cost);
	    paneCount++;
	    
	    // Calculate the new ID based on the latest PersonalPropertyID and paneCount
        int newID = latestPersonalPropertyID + paneCount;
  		PersonalPropertyID.setText(Integer.toString(newID));
  		
	  	PersonalPropertyID.textProperty().addListener((observable, oldValue, newValue) -> {	
	    });
	  	
	  	double paneY = getLastPaneYPosition() + 20.0;
	    gridPane.setLayoutX(37.0);
	    gridPane.setLayoutY(paneY);	    
	  
	    scrollPaneAnchor.getChildren().add(gridPane);
	    
	    double scrollThreshold = scrollPane.getViewportBounds().getHeight() - 100;
	    if (paneY >= scrollThreshold) {
	        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
	        scrollPane.layout();
	        scrollPane.setVvalue(1.0);
	    } else {
	        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    }
	}
	
	private double getLastPaneYPosition() {
	    double lastPaneY = 10.0;
	    for (Node node : scrollPaneAnchor.getChildren()) {
	        if (node instanceof GridPane) {
	            double nodeY = node.getLayoutY() + node.getBoundsInParent().getHeight();
	            lastPaneY = Math.max(lastPaneY, nodeY);
	        }
	    }
	    return lastPaneY;
	}
	
	private ImageView createDeleteButton(GridPane gridPane) {
	    Image deleteImage = new Image(getClass().getResourceAsStream("/images/trashicon.png"));

	    ImageView deleteButton = new ImageView(deleteImage);
	    deleteButton.setFitWidth(35);
	    deleteButton.setFitHeight(35);

	    deleteButton.setOnMouseClicked(event -> deleteGridPane(gridPane));

	    return deleteButton;
	}
	
	private void deleteGridPane(GridPane gridPane) {
		scrollPaneAnchor.getChildren().remove(gridPane);
	    paneCount--;

	    int index = 1;
	    double labelHeight = paneLabels.getBoundsInParent().getHeight();

	    for (Node node : scrollPaneAnchor.getChildren()) {
	        if (node instanceof GridPane) {
	            GridPane remainingGridPane = (GridPane) node;
	            
	            TextField PersonalPropertyID = (TextField) remainingGridPane.getChildren().get(1);
                int currentID = Integer.parseInt(PersonalPropertyID.getText());
                
                // Check if the current ID is greater than the deleted ID
                if (currentID > latestPersonalPropertyID + paneCount) {
                	PersonalPropertyID.setText(String.valueOf(currentID - 1));                            
                }

	            double paneY = (index - 1) * 30.0;
	            double labelOffset = labelHeight * (index + 1);
	            remainingGridPane.setLayoutY(paneY + labelOffset);

	            index++;
	        }
	    }	    
	    scrollPane.setVvalue(0.0);	    
	}

	// sets the value from the table to the scene
	@FXML
	public void initDataFromTable(String[] rowData) {
		int perosnal_propertyCount = rowData.length / 6; // Adjusted property count based on the column arrangement

		for (int i = 0; i < perosnal_propertyCount; i++) {
	        addNewPane();
	        int startIndex = i * 6;
	
	        int personalPropertyID = Integer.parseInt(rowData[startIndex]);
	        String personal_property_description = rowData[startIndex + 3];
	        String yearAcquired = rowData[startIndex + 4];
	        int PersonalProperty_acqCost = Integer.parseInt(rowData[startIndex + 5]);
	      
	        GridPane gridPane = (GridPane) scrollPaneAnchor.getChildren().get(i + 1);
	
	        TextField personalPropertyIDField = (TextField) gridPane.getChildren().get(1);
	        TextField personal_property_descriptionField = (TextField) gridPane.getChildren().get(2);
	        TextField yearAcquiredField = (TextField) gridPane.getChildren().get(3);
	        TextField PersonalProperty_acqCostField = (TextField) gridPane.getChildren().get(4);
	      
	        personalPropertyIDField.setText(String.valueOf(personalPropertyID));
	        personal_property_descriptionField.setText(personal_property_description);
	        yearAcquiredField.setText(yearAcquired);		        
	        PersonalProperty_acqCostField.setText(String.valueOf(PersonalProperty_acqCost));
	    }
	}
	
	@FXML
    private void updateButtonClicked() {
        for (Node node : scrollPaneAnchor.getChildren()) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;

                TextField personalPropertyIDField = (TextField) gridPane.getChildren().get(1);
    	        TextField personal_property_descriptionField = (TextField) gridPane.getChildren().get(2);
    	        TextField yearAcquiredField = (TextField) gridPane.getChildren().get(3);
    	        TextField PersonalProperty_acqCostField = (TextField) gridPane.getChildren().get(4);
    	        
                // Get the values from the text fields
    	        int personalPropertyID = Integer.parseInt(personalPropertyIDField.getText());
    	        String personal_property_description = personal_property_descriptionField.getText();
    	        String yearAcquired = yearAcquiredField.getText();
    	        int PersonalProperty_acqCost = Integer.parseInt(PersonalProperty_acqCostField.getText());
    	                        
                try {
                    salnDAO.updateDataForPersonalProperty(personalPropertyID, personal_property_description, 
                    		yearAcquired, PersonalProperty_acqCost);       
                    showAlert("Update Successful", "The declarant information has been updated.");
        		} catch (SQLException e) {
                   e.printStackTrace();
                   showAlert("Update Error", "An error occurred while updating the information.");
               }
            }
        }                     
    }
	
	// exit the stage when done button is clicked
    @FXML
    private void DoneButtonClicked() {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
    
    // shows alert message box
    private void showAlert(String title, String message) {
 	    Alert alert = new Alert(AlertType.INFORMATION);
 	    alert.setTitle(title);
 	    alert.setHeaderText(null);
 	    alert.setContentText(message);
 	    alert.showAndWait();
 	}
		
		
	
}
