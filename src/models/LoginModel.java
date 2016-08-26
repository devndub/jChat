package models;

import components.Connection;

public class LoginModel extends java.util.Observable implements Runnable{
		Connection connection = null;
		
		private boolean valid = false;
		private String indata = "";
		private Thread t;
		
		private String ip, username, password;
		private int port;
		
		private Exception login_exception = null;
		
		private boolean thread_running = false;
		
		public void login(){
			try{
				Connection login_connection = null;
				//do connection stuff
				login_connection = new Connection(ip,port);
				String to_send = username+":"+password;
				login_connection.forceSend("LOGIN",to_send);
				login_connection.receive();
				indata = login_connection.getMessage().getBody();
				//if data returns true, we have valid credentials
				if (indata.equals("True")) {valid = true; connection = login_connection;}
				else if (indata.equals("False")) {valid = false; connection = null; login_exception = null;}
				else {valid = false; connection = null; login_exception = null;}
			} catch (Exception e){
				login_exception = e;
			}
			
			setChanged();
			notifyObservers();
		}
		
		public boolean getValid(){
			return valid;
		}
		
		public Exception getException(){
			return login_exception;
		}
		
		public boolean hasConnection(){
			if (connection == null) return false;
			else return true;
		}
		
		public Connection getConnection(){
			return connection;
		}
		
		public void run() {
			thread_running = true;
			login();
			thread_running = false;
		}
		
		public void start(String ip, int port, String username, String password){
			this.ip = ip;
			this.port = port;
			this.username = username;
			this.password = password;
			
			if (t == null || thread_running == false){
				t = new Thread(this,"CONNECTION_THREAD");
				t.start();
			}
		}
}
