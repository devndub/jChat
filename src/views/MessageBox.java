package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MessageBox {
	public MessageBox(String title, String text, int width, int height){
		Stage stage = new Stage();
		stage.setTitle(title);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		Label message = new Label(text);
		message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
		VBox vb = new VBox();
		vb.setAlignment(Pos.TOP_CENTER);
		vb.setPadding(new Insets(20,0,10,0));
		vb.getChildren().add(message);
		
		grid.add(vb, 0, 0);
		
		Button btn = new Button("Close");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				stage.close();
			}
		});
		VBox vb_2 = new VBox();
		vb_2.setAlignment(Pos.BOTTOM_CENTER);
		vb_2.getChildren().add(btn);
		
		grid.add(vb_2, 0, 1);
		
		//grid.setGridLinesVisible(true);
		
		Scene scene = new Scene(grid,width,height);
		stage.setScene(scene);
		stage.show();
	}
}
