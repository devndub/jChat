package views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import components.Connection;
import components.Message;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatWindow implements Observer{
	Connection connection;
	ObservableList<String> names;
	TextArea msgs;
	TextField enter_field;
	int ROWS = 17;
	public ChatWindow(Connection c){
		connection = c;
		connection.addObserver(this);
		connection.start();

		Stage stage = new Stage();
		stage.setTitle("jChat");
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		names = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>();
		listView.setPrefWidth(200);
		listView.setItems(names);
		VBox vb = new VBox();
		vb.setAlignment(Pos.TOP_CENTER);
		vb.setPadding(new Insets(15,15,15,15));
		vb.getChildren().add(listView);
		
		msgs = new TextArea("Test\nnextline\nthirdline");
		msgs.setPrefRowCount(ROWS);
		msgs.setEditable(false);
		VBox vb2 = new VBox();
		vb2.setAlignment(Pos.TOP_CENTER);
		vb2.setPadding(new Insets(15,15,0,15));
		vb2.getChildren().add(msgs);
		
		enter_field = new TextField();
		enter_field.setPrefWidth(320);
		enter_field.setEditable(true);
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.setPadding(new Insets(15,0,15,15));
		hb.getChildren().add(enter_field);
		
		Button send_btn = new Button("Send");
		HBox hb2 = new HBox();
		hb2.setAlignment(Pos.CENTER_RIGHT);
		hb2.setPadding(new Insets(15,15,15,5));
		hb2.getChildren().add(send_btn);
		
		//Create event handler for sending messages
		
		EventHandler<KeyEvent> handler1 = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event){
				if (event.getCode().toString().equals("ENTER"))
					genericSend();
			}
		};
		
		EventHandler<InputEvent> handler2 = new EventHandler<InputEvent>() {
			public void handle(InputEvent event){
				genericSend();
			}
		};
		
		//add event handler to elements
		enter_field.addEventHandler(KeyEvent.KEY_PRESSED, handler1);
		send_btn.addEventHandler(MouseEvent.MOUSE_RELEASED, handler2);
		
		grid.add(vb, 0, 0, 1, 2);
		grid.add(vb2, 1, 0, 2, 1);
		grid.add(hb, 1, 1);
		grid.add(hb2, 2, 1);
		//grid.setGridLinesVisible(true);
		
		Scene scene = new Scene(grid,700,400);
		stage.setScene(scene);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent e){
				try {
					connection.stop();
				} catch (IOException e1) {
					
				}
			}
		});
		stage.show();
	}
	
	public void update(Observable t, Object o){
		Message msg = connection.getMessage();
		if (msg != null){
			if (msg.getHeader().equals("ONLINE")){
				if (!names.contains(msg.getBody()))
					names.add(msg.getBody());
			}
			else if (msg.getHeader().equals("OFFLINE")){
				if (names.contains(msg.getBody()))
					names.remove(msg.getBody());
			}
			else if (msg.getHeader().equals("MSG")){
				msgs.appendText("\n"+msg.getBody());
			}
			else if (msg.getHeader().equals("CLOSE")){
				msgs.appendText("\n"+msg.getBody());
			}
		}
	}
	
	private void genericSend(){
		try {
			if (!enter_field.getText().equals("")){
				connection.send("MSG",enter_field.getText());
				enter_field.clear();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
