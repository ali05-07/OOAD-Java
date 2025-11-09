import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file - use simple filename without spaces
        Parent root = FXMLLoader.load(getClass().getResource("Banking system.fxml"));
        
        // Create the scene
        Scene scene = new Scene(root, 900, 700);
        
        // Set up the stage
        primaryStage.setTitle("Banking System - OOAD Assignment");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}