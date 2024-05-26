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

public class UpdateRealPropertyController {
	
	private int paneCount = 0;
	private GridPane gridPane;
    private int latestRealPropertyID; // Latest ID from the database

	@FXML private Button updateButton, doneButton;
	@FXML private AnchorPane scrollPaneAnchor;
	@FXML private ScrollPane scrollPane;
	@FXML private Pane paneLabels;
	
	@FXML
    public void initialize() {	
		if (paneLabels != null) {
	        double[] labelLayoutX = {15, 140, 318, 465, 630, 798, 902, 1017, 1182};

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
		
		scrollPaneAnchor.setPrefWidth(1400);
        scrollPaneAnchor.setPrefHeight(500);
        scrollPane.setVmax(1200); 
        scrollPane.setPrefViewportHeight(100); 
        scrollPane.setVvalue(0);  
        scrollPane.setContent(contentGroup);
		
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        latestRealPropertyID = salnDAO.getLatestRealPropertyIDFromDatabase();

	}
	
    // sets the value from the table to the scene
	@FXML
	public void initDataFromTable(String[] rowData) {
	    int propertyCount = rowData.length / 11; // Adjusted property count based on the column arrangement

	    for (int i = 0; i < propertyCount; i++) {
	        addNewPane();
	        int startIndex = i * 11; // Adjusted index

	        int realPropertyID = Integer.parseInt(rowData[startIndex]);
	        String description = rowData[startIndex + 3];
	        String kind = rowData[startIndex + 4];
	        String exactLocation = rowData[startIndex + 5];
	        int assessedValue = Integer.parseInt(rowData[startIndex + 6]);
	        int marketValue = Integer.parseInt(rowData[startIndex + 7]);
	        String acquisitionYear = rowData[startIndex + 8];
	        String acquisitionMode = rowData[startIndex + 9];
	        int acquisitionCost = Integer.parseInt(rowData[startIndex + 10]);

	        GridPane gridPane = (GridPane) scrollPaneAnchor.getChildren().get(i + 1);

	        TextField realPropertyIDField = (TextField) gridPane.getChildren().get(1);
	        TextField descriptionField = (TextField) gridPane.getChildren().get(2);
	        TextField kindField = (TextField) gridPane.getChildren().get(3);
	        TextField exactLocationField = (TextField) gridPane.getChildren().get(4);
	        TextField assessedValueField = (TextField) gridPane.getChildren().get(5);
	        TextField marketValueField = (TextField) gridPane.getChildren().get(6);
	        TextField acquisitionYearField = (TextField) gridPane.getChildren().get(7);
	        TextField acquisitionModeField = (TextField) gridPane.getChildren().get(8);
	        TextField acquisitionCostField = (TextField) gridPane.getChildren().get(9);

	        realPropertyIDField.setText(String.valueOf(realPropertyID));
	        descriptionField.setText(description);
	        kindField.setText(kind);
	        exactLocationField.setText(exactLocation);
	        assessedValueField.setText(String.valueOf(assessedValue));
	        marketValueField.setText(String.valueOf(marketValue));
	        acquisitionYearField.setText(String.valueOf(acquisitionYear));
	        acquisitionModeField.setText(acquisitionMode);
	        acquisitionCostField.setText(String.valueOf(acquisitionCost));
	    }
	}

	@FXML
	private void addNewPane() {
		gridPane = new GridPane();
		gridPane.setLayoutX(45);
		gridPane.setLayoutY(30);
	    gridPane.setHgap(15);
	    
	    TextField RealPropertyID = new TextField();
	    TextField RealPropDesc = new TextField();
	    TextField Kind = new TextField();
	    TextField ExactLocation = new TextField();
	    TextField AssessedValue = new TextField();
	    TextField MarketValue = new TextField();
	    TextField RealProp_AcqYear = new TextField();
	    TextField RealProp_AcqMode = new TextField();
	    TextField RealProp_AcqCost = new TextField();
	    
	    RealPropertyID.setPrefSize(50, 20);
	    RealProp_AcqYear.setPrefSize(50, 20);
	    
	    
	    ImageView deleteButton = createDeleteButton(gridPane);
	    gridPane.add(deleteButton, 0, 0);

	    gridPane.addRow(0, RealPropertyID, RealPropDesc, Kind, ExactLocation, AssessedValue,
	    		MarketValue, RealProp_AcqYear, RealProp_AcqMode, RealProp_AcqCost);
	    paneCount++;
	    
	    // Calculate the new ID based on the latestChildID and paneCount
        int newID = latestRealPropertyID + paneCount;
  		RealPropertyID.setText(Integer.toString(newID));

	  	RealPropertyID.textProperty().addListener((observable, oldValue, newValue) -> {	
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
	            
	            TextField RealPropertyID = (TextField) remainingGridPane.getChildren().get(1);
                int currentID = Integer.parseInt(RealPropertyID.getText());
                
                // Check if the current ID is greater than the deleted ID
                if (currentID > latestRealPropertyID + paneCount) {
                	RealPropertyID.setText(String.valueOf(currentID - 1));                            
                }
	            
	            double paneY = (index - 1) * 30.0;
	            double labelOffset = labelHeight * (index + 1);
	            remainingGridPane.setLayoutY(paneY + labelOffset);

	            index++;
	        }
	    }	    
	    scrollPane.setVvalue(0.0);	    
	}
	
	@FXML
    private void updateButtonClicked() {
        for (Node node : scrollPaneAnchor.getChildren()) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;

                TextField realPropertyIDField = (TextField) gridPane.getChildren().get(1);
                TextField descriptionField = (TextField) gridPane.getChildren().get(2);
                TextField kindField = (TextField) gridPane.getChildren().get(3);
                TextField exactLocationField = (TextField) gridPane.getChildren().get(4);
                TextField assessedValueField = (TextField) gridPane.getChildren().get(5);
                TextField marketValueField = (TextField) gridPane.getChildren().get(6);
                TextField acquisitionYearField = (TextField) gridPane.getChildren().get(7);
                TextField acquisitionModeField = (TextField) gridPane.getChildren().get(8);
                TextField acquisitionCostField = (TextField) gridPane.getChildren().get(9);

                // Get the values from the text fields
                int realPropertyID = Integer.parseInt(realPropertyIDField.getText());
                String description = descriptionField.getText();
                String kind = kindField.getText();
                String exactLocation = exactLocationField.getText();
                int assessedValue = Integer.parseInt(assessedValueField.getText());
                int marketValue = Integer.parseInt(marketValueField.getText());
                String acquisitionYear = acquisitionYearField.getText();
                String acquisitionMode = acquisitionModeField.getText();
                int acquisitionCost = Integer.parseInt(acquisitionCostField.getText());

                try {
                    salnDAO.updateDataForRealProperty(realPropertyID, description, kind, exactLocation, assessedValue,
                    		marketValue, acquisitionYear, acquisitionMode, acquisitionCost);       
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
