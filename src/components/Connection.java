package components;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import components.Message;

public class Connection extends java.util.Observable{
	private Socket connection;
	private LinkedList<Message> received = new LinkedList<Message>();
	private LinkedList<Message> sent = new LinkedList<Message>();
	private InputStream in;
	private OutputStream out;
	private PrintWriter print_out;
	//private boolean keep_alive = true;

	public Connection(String host, int port) throws UnknownHostException, IOException{
		this.connection = new Socket(host,port);
		in = connection.getInputStream();
		out = connection.getOutputStream();
		print_out = new PrintWriter(out,true);
	}
	
	public void receive() throws Exception{
		//read in the size of the message
		byte[] size = new byte[8];
		in.read(size, 0, 8);
		int length = Integer.parseInt(new String(size), 16);
		//create buffer equal to message length
		byte[] msg = new byte[length];
		//read in message to buffer
		in.read(msg,0,length);
		//add message object to history
		received.add(new Message(Integer.toString(length) + new String(msg)));
		
		setChanged();
		notifyObservers();
	}
	
	public void send(String msg) throws Exception{
		Message msg_obj = new Message(msg);
		print_out.println(msg_obj.getText());
		sent.add(msg_obj);
	}
	
	public void send(String header, String msg) throws Exception{
		Message msg_obj = new Message(header, msg);
		print_out.println(msg_obj.getText());
		sent.add(msg_obj);
	}
	
	public Message getMessage(){
		return received.remove();
	}
	
	public Message getMessage(String header){
		int index = 0;
		int found_at = -1;
		for (Message item : received){
			if (item.getHeader().equals(header)) {
				found_at = index;
				break;
			}
			++index;
		}
		try{
			return received.remove(found_at);
		}
		catch (IndexOutOfBoundsException e){
			return null;
		}
	}
}
