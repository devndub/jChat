package components;

public class Message {
	private String msg_string;
	private String header;
	private String body;
	private int length;
	
	public Message(String text){
		msg_string = text;
		String[] parts = msg_string.split(":",3);
		header = parts[1];
		body = parts[2];
		length = Integer.parseInt(Integer.toString((":"+header+":"+body).getBytes().length),16);
	}
	public Message(String header, String body){
		String length = String.format("%08x", (header+body).getBytes().length);
		msg_string = length+":"+header+":"+body;
		this.header = header;
		this.body = body;
		this.length = (header+body).getBytes().length;
	}
	
	public String getText(){
		return msg_string;
	}
	
	public String getHeader(){
		return header;
	}
	
	public String getBody(){
		return body;
	}
	
	public int getLength(){
		return length;
	}
}
