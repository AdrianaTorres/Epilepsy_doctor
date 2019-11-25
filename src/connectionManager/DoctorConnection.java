package connectionManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.List;

import mainMethodDoctor.UserProfile;

public class DoctorConnection {
	//private boolean requestedMonitoring;
	private Socket manager;
	private PrintWriter pw;
	private BufferedReader bf;
	private Thread t;
	private UserProfile up;
	
	public DoctorConnection (String ip) throws Exception {
        try {
        	manager = new Socket(ip, 9000);
        	pw = new PrintWriter(manager.getOutputStream(), true);
        	bf = new BufferedReader(new InputStreamReader(manager.getInputStream()));
			//requestedMonitoring = false;
        } catch (Exception e) {
        	System.out.println("Could not connect to server!");
        	
        	manager = null;
        	pw = null;
        	bf = null;
        	
        	e.printStackTrace();
        	
        	throw new Exception();
        }
    }
	
	public void closeConnection() {
		try {
			manager.close();
			pw.close();
			this.killThread();
		} catch (IOException e) {
			System.out.println("Could not close the connection!");
			e.printStackTrace();
		}
	}
	
	private void killThread() {
		this.t.interrupt();
	}
	
	public UserProfile login (String UserName, String Password) throws Exception {
		pw.println("USER REQUESTING LOGIN");
		pw.println(UserName);
		pw.println(Password);
		
		Thread.sleep(100);
		
		String serverAnswer = bf.readLine();
		
		if(serverAnswer.contains("REJECTED")) {
			if(serverAnswer.contains("404")) {
				throw new Exception();
			} else {
				return null;
			}
		} else {
			String name = bf.readLine();
			UserProfile login = new UserProfile(name);
			System.out.println(login.getName());
			this.up = login;
			return login;
		}
	}
	
	public void createProfile(String userName, String password) throws Exception{
		pw.println("USER REQUESTING NEW PROFILE");
		pw.println(userName);
		pw.println(password);
		String serverReply = bf.readLine();
		if (!serverReply.equals("CONFIRM")) {
			throw new Exception();
		}
	}
	
	public void sendProfile(UserProfile up) {
		pw.println("USER REQUESTING NEW USER PROFILE");
		pw.println(up.getName());
	}
}


