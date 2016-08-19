package views;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.LoginModel;
import views.LoginFailed;

public class Login extends Application implements Observer{
	final Text signin = new Text();
	LoginModel actiontarget;
	Button btn;
	TextField userTextField;
	PasswordField pwBox;
	Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws IOException{
		this.primaryStage = primaryStage;
		primaryStage.setTitle("JavFX Welcome");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		
		Label userName = new Label("User Name: ");
		grid.add(userName, 0, 1);
		
		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);
		
		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);
		
		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);
		
		//grid.setGridLinesVisible(true);
		
		btn = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		
		//add functionality to login button
		actiontarget = new LoginModel();
		actiontarget.addObserver(this);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				actiontarget.login(userTextField.getText(), pwBox.getText());
			}
		});
		grid.add(signin, 1, 6);
		
		Scene scene = new Scene(grid,300,275);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void update(Observable t, Object o){
		signin.setFill(Color.FIREBRICK);
        signin.setText("Sign in: " + actiontarget.getValid());
        if (actiontarget.hasConnection() == false)  {@SuppressWarnings("unused")
		LoginFailed wdw = new LoginFailed();}
        else{
        	ChatWindow main_chat = new ChatWindow();
        	primaryStage.close();
        }
	}

}