package models;

public class Model extends java.util.Observable{
	private int value;
	
	public Model(){
		setValue(0);
	}
	
	public void setValue(int val){
		value = val;
		notifyObservers(value);
	}

}
