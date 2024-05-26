package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInScene.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root,700,400); 
			String css = this.getClass().getResource("/CSS/application.css").toExternalForm();
			scene.getStylesheets().add(css);
			Image logoIcon = new Image(getClass().getResourceAsStream("/images/SALNLogo.png"));			
			primaryStage.getIcons().add(logoIcon);
			primaryStage.setScene(scene);
			primaryStage.setTitle("SALN");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	 	
	public static void main(String[] args) {
		launch(args);
	}
	
	 
}
