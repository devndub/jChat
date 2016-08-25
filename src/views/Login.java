package views;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.LoginModel;
import views.LoginFailed;

public class Login extends Application implements Observer{
	LoginModel actiontarget;
	Button btn;
	TextField userTextField, ip_field, port_field;
	PasswordField pwBox;
	Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws IOException{
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Login");
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		
		
		Label userName = new Label("User Name: ");
		
		userTextField = new TextField();
		
		Label pw = new Label("Password:");
		
		pwBox = new PasswordField();
		
		Label lbl_ip = new Label("IP: ");
		
		ip_field = new TextField();
		
		Label lbl_port = new Label("Port: ");
		
		port_field = new TextField();
		
		actiontarget = new LoginModel();
		actiontarget.addObserver(this);
		
		EventHandler<KeyEvent> handler1 = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event){
				if (event.getCode().toString().equals("ENTER")){
					doLogin();
				}
			}
		};
		pwBox.addEventHandler(KeyEvent.KEY_PRESSED, handler1);
		port_field.addEventHandler(KeyEvent.KEY_PRESSED, handler1);
		
		btn = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		
		//add functionality to login button
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				doLogin();
			}
		});
		
		Button reg_btn = new Button("Register");
		HBox hbBtn2 = new HBox(10);
		hbBtn2.setAlignment(Pos.BOTTOM_LEFT);
		hbBtn2.getChildren().add(reg_btn);
		
		reg_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				new Register();
			}
		});
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		grid.add(scenetitle, 0, 0, 2, 1);
		grid.add(userName, 0, 1);
		grid.add(userTextField, 1, 1);
		grid.add(pw, 0, 2);
		grid.add(pwBox, 1, 2);
		grid.add(lbl_ip, 0, 4);
		grid.add(ip_field, 1, 4);
		grid.add(lbl_port, 0, 5);
		grid.add(port_field, 1, 5);
		grid.add(hbBtn2, 0, 6);
		grid.add(hbBtn, 1, 6);
		
		//grid.setGridLinesVisible(true);
		Scene scene = new Scene(grid,300,250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void update(Observable t, Object o){
        if (actiontarget.getValid() == false)  {
        	Exception e = actiontarget.getException();
        	//Create error message based off of exception
        	if (e == null){
        		Platform.runLater(() -> new LoginFailed());
        	} else if (e instanceof UnknownHostException) {
				Platform.runLater(() -> new MessageBox("Error", "Host not found.", 250, 100));
			} else if (e instanceof ConnectException) {
				Platform.runLater(() -> new MessageBox("Error", "Connection refused by host.", 250, 100));
			} else {
				e.printStackTrace();
			}
        } else {
        	Platform.runLater(() -> new ChatWindow(actiontarget.getConnection()));
        	Platform.runLater(() -> primaryStage.close());
        }
	}
	
	public void doLogin(){
		try{
			actiontarget.start(ip_field.getText(), Integer.parseInt(port_field.getText()),
				userTextField.getText(), pwBox.getText());
		} catch (NumberFormatException e){
			new MessageBox("Error","'Port' requires positive integer.",250,100);
		}
	}

}