package application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import javafx.util.StringConverter;

public class UpdateChildrenController {

    private int paneCount = 0;
    private GridPane gridPane;
    private int latestChildID; // Latest ID from the database
    
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane scrollPaneAnchor;
    @FXML private Pane paneLabels;
    @FXML private ImageView addiconimg;    
    @FXML private Button doneButton;
       
    // initializes the layout of the elements in the scene
    @FXML
    public void initialize() {
        // sets the layout X of the textfields
        if (paneLabels != null) {
            double[] labelLayoutX = { 62, 155, 345, 477 };

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

        // Get the latest ID from the database and set it as the initial value for UCCounter
        latestChildID = salnDAO.getLatestChildIDFromDatabase();
    }
    
    // sets the value from the table to the scene
    @FXML
    public void initDataFromTable(String[] rowData) {
        int childCount = rowData.length / 6;
     
        for (int i = 0; i < childCount; i++) {
            addNewPane();
            int startIndex = i * 6;           
            int childID = Integer.parseInt(rowData[startIndex]);
            String fullname = rowData[startIndex + 3];
            String birthdateString = rowData[startIndex + 4];
            int age = Integer.parseInt(rowData[startIndex + 5]);

            // Get the GridPane from the scrollPaneAnchor, skipping the first child (the Pane for labels)
            GridPane gridPane = (GridPane) scrollPaneAnchor.getChildren().get(i + 1);

            // Get the TextFields from the GridPane
            TextField UCCounter = (TextField) gridPane.getChildren().get(1);
            TextField UCfullnametxtField = (TextField) gridPane.getChildren().get(2);
            DatePicker UCBirthdateDatePicker = (DatePicker) gridPane.getChildren().get(3);
            TextField UCAgetxtField = (TextField) gridPane.getChildren().get(4);

            UCCounter.setText(String.valueOf(childID));
            UCfullnametxtField.setText(fullname);
            
            // Parse the birthdate string and set the value in the DatePicker
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthdate = LocalDate.parse(birthdateString, dateFormatter);
            UCBirthdateDatePicker.getEditor().setText(birthdate.format(dateFormatter));
            UCBirthdateDatePicker.setValue(birthdate);
    
            UCAgetxtField.setText(String.valueOf(age));
        }
    }

    // adds new pane with textfields
    @FXML
    public void addNewPane() {
        gridPane = new GridPane();
        gridPane.setHgap(10);

        TextField UCCounter = new TextField();
        TextField UCfullnametxtField = new TextField();
        TextField UCAgetxtField = new TextField();
        DatePicker UCBirthdateDatePicker = new DatePicker();

        UCAgetxtField.setPrefWidth(50);
        UCCounter.setPrefSize(55, 25);

        ImageView deleteButton = createDeleteButton(gridPane);
        gridPane.add(deleteButton, 0, 0);

        gridPane.addRow(0, UCCounter, UCfullnametxtField, UCBirthdateDatePicker, UCAgetxtField);
        paneCount++;

        // Calculate the new ID based on the latestChildID and paneCount
        int newID = latestChildID + paneCount;
        UCCounter.setText(Integer.toString(newID));

        UCCounter.textProperty().addListener((observable, oldValue, newValue) -> {
        });

        // Calculate the age through the selected birthdate
        UCAgetxtField.textProperty().bindBidirectional(UCBirthdateDatePicker.valueProperty(), new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    int age = calculateAge(date);
                    return String.valueOf(age);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                return null;
            }
        });

        double paneY = getLastPaneYPosition() + 20.0;
        gridPane.setLayoutX(37.0);
        gridPane.setLayoutY(paneY);

        scrollPaneAnchor.getChildren().add(gridPane);

        double scrollThreshold = scrollPane.getViewportBounds().getHeight() - 50;
        if (paneY >= scrollThreshold) {
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        } else {
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
               
    }

    // creates delete button
    private ImageView createDeleteButton(GridPane gridPane) {
        Image deleteImage = new Image(getClass().getResourceAsStream("/images/trashicon.png"));

        ImageView deleteButton = new ImageView(deleteImage);
        deleteButton.setFitWidth(35);
        deleteButton.setFitHeight(35);

        deleteButton.setOnMouseClicked(event -> deleteGridPane(gridPane));

        return deleteButton;
    }

    // method to delete the selected gridpane
    private void deleteGridPane(GridPane gridPane) {
        scrollPaneAnchor.getChildren().remove(gridPane);
        paneCount--;

        int index = 1;
        double labelHeight = paneLabels.getBoundsInParent().getHeight();

        for (Node node : scrollPaneAnchor.getChildren()) {
            if (node instanceof GridPane) {
                GridPane remainingGridPane = (GridPane) node;

                TextField UCCounter = (TextField) remainingGridPane.getChildren().get(1);
                int currentID = Integer.parseInt(UCCounter.getText());
                
                // Check if the current ID is greater than the deleted ID
                if (currentID > latestChildID + paneCount) {
                    UCCounter.setText(String.valueOf(currentID - 1));                            
                }
                
                double paneY = (index - 1) * 30.0;
                double labelOffset = labelHeight * (index + 1);
                remainingGridPane.setLayoutY(paneY + labelOffset);

                index++;
            }

        scrollPane.setVvalue(0.0);
        }
    }
    
    // gets the last layout Y position of the created panes
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

    // automatically calculates the age once birthdate has been set
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    // updates the entered data to the database
    @FXML
    private void UpdateButtonClicked() throws SQLException {
        for (Node node : scrollPaneAnchor.getChildren()) {
		    if (node instanceof GridPane) {
		        GridPane gridPane = (GridPane) node;

		        // Update the index values to match the correct order of the fields in the GridPane
		        TextField counterField = (TextField) gridPane.getChildren().get(1);
		        TextField fullnameField = (TextField) gridPane.getChildren().get(2);
		        DatePicker birthdatePicker = (DatePicker) gridPane.getChildren().get(3);
		        TextField ageField = (TextField) gridPane.getChildren().get(4);

		        int counter = Integer.parseInt(counterField.getText());
		        String fullname = fullnameField.getText();
		        LocalDate birthdate = birthdatePicker.getValue();
		        int age = Integer.parseInt(ageField.getText());

		        // Call the update method if the counter value is not zero
		        if (counter != 0) {
		        	try {
		                salnDAO.updateDataforUnmarriedChilrenBelow18(counter, fullname, birthdate, age);
		                showAlert("Update Successful", "The children information has been updated.");
		                } catch (SQLException e) {
		                e.printStackTrace();
		                showAlert("Update Error", "An error occurred while updating the information.");
		            }                       
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