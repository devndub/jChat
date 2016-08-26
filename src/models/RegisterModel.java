package models;

import components.Connection;

public class RegisterModel extends java.util.Observable implements Runnable{
	Connection connection = null;
	private boolean valid = false;
	String indata, ip, username, password;
	int port;
	
	private Thread t;
	
	private Exception reg_exception = null;
	private boolean thread_running = false;
	
	public void register(){
		try {
			connection = new Connection(ip,port);
			connection.forceSend("REGISTER",username+":"+password);
			connection.receive();
			indata = connection.getMessage().getBody();
			if (indata.equals("True")) valid = true;
			else if (indata.equals("False")) valid = false;
			reg_exception = null;
			
		} catch (Exception e){
			reg_exception = e;
		}
			
		setChanged();
		notifyObservers();
	}
	
	public boolean getValid(){
		return valid;
	}
	
	public Exception getException(){
		return reg_exception;
	}

	public void run() {
		thread_running = true;
		register();
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
