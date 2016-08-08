package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoginModel extends Model{
		private ArrayList<String> usernames = new ArrayList<String>();
		private ArrayList<String> passwords = new ArrayList<String>();
		
		private boolean valid = false;
		
		public LoginModel() throws IOException{
		//PRE: File 'userpass.txt' must exist and be properly formatted:
		//			myusername:mypassword
			BufferedReader rdr = new BufferedReader(new FileReader("userpass.txt"));
			String line;
			String[] parts;
			while (rdr.ready()){
				line = rdr.readLine();
				parts = line.split(":");
				usernames.add(parts[0]);
				passwords.add(parts[1]);
			}
			rdr.close();
		}
		
		public void exists(String username, String password){
			for (String user: usernames){
				if (user.equals(username)){
					for (String pass: passwords){
						if (pass.equals(password)){
							valid = true;
							setChanged();
							notifyObservers();
							return;
						}
					}
				}
			}
			valid = false;
			setChanged();
			notifyObservers();
		}
		
		public boolean getValid(){
			return valid;
		}
}
