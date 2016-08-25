package views;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

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
import javafx.stage.Stage;
import models.RegisterModel;

public class Register implements Observer {
	RegisterModel actiontarget;
	PasswordField pwBox, confirm_pwBox;
	TextField userTextField, ip_field, port_field;
	
	public Register(){
		Stage stage = new Stage();
		stage.setTitle("Register");
		
		Label userName = new Label("User Name: ");
		
		userTextField = new TextField();
		
		Label pw = new Label("Password:");
		
		pwBox = new PasswordField();
		
		Label lbl_confirm = new Label("Confirm Password:");
		
		confirm_pwBox = new PasswordField();
		
		Label lbl_ip = new Label("IP: ");
		
		ip_field = new TextField();
		
		Label lbl_port = new Label("Port: ");
		
		port_field = new TextField();
		
		Button btn = new Button("Register");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		
		//add functionality to login button
		actiontarget = new RegisterModel();
		actiontarget.addObserver(this);
		
		EventHandler<KeyEvent> handler1 = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event){
				if (event.getCode().toString().equals("ENTER")){
					doRegister();
				}
			}
		};
		
		port_field.addEventHandler(KeyEvent.KEY_PRESSED, handler1);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				doRegister();
			}
		});
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10,10,10,10));
		
		grid.add(userName, 0, 1);
		grid.add(userTextField, 1, 1);
		grid.add(pw, 0, 2);
		grid.add(pwBox, 1, 2);
		grid.add(lbl_confirm, 0, 3);
		grid.add(confirm_pwBox, 1, 3);
		grid.add(lbl_ip, 0, 4);
		grid.add(ip_field, 1, 4);
		grid.add(lbl_port, 0, 5);
		grid.add(port_field, 1, 5);
		grid.add(hbBtn, 1, 6);
		
		//grid.setGridLinesVisible(true);

		Scene scene = new Scene(grid,300,235);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (actiontarget.getValid() == true){
			Platform.runLater(() -> new MessageBox("Success","Registration successful, please login.",250,100));
		}
		else{
			Exception e = actiontarget.getException();
			if (e == null){
				Platform.runLater(() -> new MessageBox("Error", "Username already exists.", 250, 100));
			} else if (e instanceof UnknownHostException) {
				Platform.runLater(() -> new MessageBox("Error", "Host not found.", 250, 100));
			} else if (e instanceof ConnectException) {
				Platform.runLater(() -> new MessageBox("Error", "Connection refused by host.", 250, 100));
			} else {
				e.printStackTrace();
			}
		}
	}
	
	public void doRegister(){
		if (pwBox.getText().equals(confirm_pwBox.getText())){
			try{
				actiontarget.start(ip_field.getText(), Integer.parseInt(port_field.getText()),
						userTextField.getText(), pwBox.getText());
			} catch (NumberFormatException e){
				new MessageBox("Error","'Port' requires positive integer.",250,100);
			} 
		}
		else{
			new MessageBox("Failure","Passwords don't match.",250,100);
		}
	}
}
