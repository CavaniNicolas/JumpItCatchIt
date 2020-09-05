package Game.Network;

import java.net.*;
import java.io.*;

public class ExtendedSocket {
	private int ID;
	private Boolean ready = false;
	private Boolean isServer;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	private Socket clientSocket;

	public ExtendedSocket(int ID, Socket socket, Boolean isServer) {
		this.ID = ID;
		this.clientSocket = socket;
		this.isServer = isServer;
		System.out.println(this.clientSocket.getInetAddress().getHostAddress());
		initializeStreams();
	}

	/** creates the output and input streams */
	public void initializeStreams() {
		try {
			if (isServer) {
				objectOutput = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
				objectInput = new ObjectInputStream(clientSocket.getInputStream());
			} else {
				objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
				objectInput = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			}
			ready = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** reads object from the stream, returns null if an exception occurs */
	public Object readObject() {
		try {
			return objectInput.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** return true if the object has been sent */
	public Boolean outputObject(Object obj) {
		try {
			objectOutput.writeObject(obj);
			objectOutput.flush();
			objectOutput.reset();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			endConnection();
			return false;
		}
	}

	/** ends the connection, closes the socket */
	public void endConnection() {
		try {
			objectOutput.close(); 
			objectInput.close(); 
			clientSocket.close(); 
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public int getID() {
		return ID;
	}

	public Boolean getReady() {
		return ready;
	}
}

