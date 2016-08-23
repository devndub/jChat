package run;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import views.Login;
public class RunChat extends Application{
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws IOException{
		Login l = new Login();
		l.start(primaryStage);
	}

}
