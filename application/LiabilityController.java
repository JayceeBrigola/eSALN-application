package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LiabilityController {
	private int paneCount = 0;
	private GridPane gridPane;
    private int latestLiabilityID; // Latest ID from the database
	
	@FXML private Button submitButton;
	@FXML private AnchorPane scrollPaneAnchor;
	@FXML private ScrollPane scrollPane;
	@FXML private Pane paneLabels;
	
	@FXML
    public void initialize() {	
		
		if (paneLabels != null) {
	        double[] labelLayoutX = {37, 165, 330, 490};

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
		
		scrollPaneAnchor.setPrefWidth(690);
        scrollPaneAnchor.setPrefHeight(500);
        scrollPane.setVmax(1200); 
        scrollPane.setPrefViewportHeight(100); 
        scrollPane.setVvalue(0);  
        scrollPane.setContent(contentGroup);
		
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        latestLiabilityID = salnDAO.getLatestLiabilityIDFromDatabase();

	}
	
	@FXML
	private void addNewPane() {
		gridPane = new GridPane();
		gridPane.setLayoutX(45);
		gridPane.setLayoutY(30);
	    gridPane.setHgap(25);
	    
	    TextField LiabilityID = new TextField();
	    TextField Nature = new TextField();
	    TextField Name_of_Creditor = new TextField();
	    TextField Outs_Balance = new TextField();
	    
	    LiabilityID.setPrefSize(50, 20);
	    	    
	    ImageView deleteButton = createDeleteButton(gridPane);
	    gridPane.add(deleteButton, 0, 0);

	    gridPane.addRow(0, LiabilityID, Nature, Name_of_Creditor, Outs_Balance);
	    paneCount++;

	    // Calculate the new ID based on the latest Liability and paneCount
        int newID = latestLiabilityID + paneCount;
  		LiabilityID.setText(Integer.toString(newID));  

	    LiabilityID.textProperty().addListener((observable, oldValue, newValue) -> {	
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
	            
	            TextField LiabilityID = (TextField) remainingGridPane.getChildren().get(1);
	            int currentID = Integer.parseInt(LiabilityID.getText());
                
                // Check if the current ID is greater than the deleted ID
                if (currentID > latestLiabilityID + paneCount) {
                	LiabilityID.setText(String.valueOf(currentID - 1));                            
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
	private void SubmitButtonClicked() throws IOException, SQLException{
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to proceed?");
        alert.setContentText("Click OK to continue.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Node node : scrollPaneAnchor.getChildren()) {
                if (node instanceof GridPane) {
                    GridPane gridPane = (GridPane) node;
                    
                    // Update the index values to match the correct order of the fields in the GridPane
                    TextField liabilityIDField = (TextField) gridPane.getChildren().get(1);
                    TextField natureField = (TextField) gridPane.getChildren().get(2);
                    TextField name_of_creditorField = (TextField) gridPane.getChildren().get(3);
                    TextField outs_balanceField = (TextField) gridPane.getChildren().get(4);

                    int liabilityID = Integer.parseInt(liabilityIDField.getText());
                    String nature = natureField.getText();
                    String name_of_creditor = name_of_creditorField.getText();
                    int outs_balance = Integer.parseInt(outs_balanceField.getText());

                    salnDAO.saveDataforLiability(liabilityID, nature, name_of_creditor, outs_balance);
                }
            }

            Stage stage = (Stage) submitButton.getScene().getWindow();
            Parent nextScene = FXMLLoader.load(getClass().getResource("SummaryScene.fxml"));
            Scene scene = new Scene(nextScene);
            stage.setScene(scene);
            stage.show();
        }
	}
	
	

}
