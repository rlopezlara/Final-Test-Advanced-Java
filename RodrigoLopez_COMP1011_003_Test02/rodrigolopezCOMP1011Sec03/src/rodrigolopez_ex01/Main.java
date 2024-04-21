package rodrigolopez_ex01;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	@Override
	   public void start(Stage stage) throws Exception {
	      Parent root = 
	         FXMLLoader.load(getClass().getResource("TestForm.fxml"));
	      
	      Scene scene = new Scene(root);
	      stage.setTitle("Test02 JAVA");
	      stage.setScene(scene);
	      stage.show();
	   }

	   public static void main(String[] args) {
	      launch(args);
	   }
	}
