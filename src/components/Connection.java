package components;

import java.net.Socket;

public class Connection extends java.util.Observable{
	private Socket connection;

	public Connection(Socket connection){
		this.connection = connection;
	}
}
