package models;

import java.io.IOException;
import java.net.ConnectException;

import components.Connection;
import components.Message;

public class LoginModel extends java.util.Observable{
		Connection connection = null;
		
		private boolean valid = false;
		private String indata = "";
		
		public LoginModel(){}
		
		public void login(String username, String password){
			Connection login_connection = null;
			//do connection stuff
			try {
				login_connection = new Connection("localhost",1234);
				String to_send = username+":"+password;
				login_connection.send("LOGIN",to_send);
				login_connection.receive();
				indata = login_connection.getMessage().getBody();
			} catch (ConnectException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//if data returns true, we have valid credentials
			if (indata.equals("True")) {valid = true; connection = login_connection;}
			else if (indata.equals("False")) {valid = false; connection = null;}
			else {valid = false; connection = null;}
			
			setChanged();
			notifyObservers();
		}
		
		public boolean getValid(){
			return valid;
		}
		
		public boolean hasConnection(){
			if (connection == null) return false;
			else return true;
		}
		
		public Connection getConnection(){
			return connection;
		}
}
