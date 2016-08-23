package components;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import components.Message;

public class Connection extends java.util.Observable implements Runnable{
	private Socket connection;
	private LinkedList<Message> received = new LinkedList<Message>();
	private LinkedList<Message> sent = new LinkedList<Message>();
	private InputStream in;
	private OutputStream out;
	private PrintWriter print_out;
	private Thread t;
	private boolean keep_alive = true;

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
		int read = 0;
		while (read < length){
			if (length - read <= 10240){
				in.read(msg,0,length-read);
				read += length-read;
			}
			else{
				in.read(msg,0,10240);
				read += 10240;
			}
		}
		//add message object to history
		received.add(new Message(Integer.toString(length) + new String(msg)));
		
		setChanged();
		notifyObservers();
	}
	
	//DEPRECATED, do not use
	public void send(String msg) throws Exception{
		Message msg_obj = new Message(msg);
		print_out.println(msg_obj.getText());
		sent.add(msg_obj);
	}
	
	public void send(String header, String msg) throws Exception{
		Message msg_obj = new Message(header, msg);
		try{
			print_out.println(msg_obj.getText());
			sent.add(msg_obj);
		} catch (Exception e){
			
		}
		
	}
	
	public Message getMessage(){
		try{
			return received.remove();
		} catch (NoSuchElementException e){
			return null;
		}
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
		catch (NoSuchElementException e){
			return null;
		}
	}

	@Override
	public void run() {
		while (this.keep_alive){
			try{
				receive();
			} catch (Exception e){
				this.keep_alive = false;
				received.add(new Message("CLOSE","Connection lost."));
			}
		}
	}
	
	public void start(){
		if (t == null){
			t = new Thread(this,"CONNECTION_THREAD");
			t.start();
		}
	}
	
	public void stop() throws IOException{
		keep_alive = false;
		connection.close();
	}
}
