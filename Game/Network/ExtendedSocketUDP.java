package Game.Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ExtendedSocketUDP {
	private int ID;
	private Boolean ready = false;
	private DatagramSocket socket;
	private InetAddress destAddress;
	private int destPort;

	private ByteArrayOutputStream baos;
	private ObjectOutputStream oos;

	private ByteArrayInputStream bais;
	private ObjectInputStream ois;

	private final int bufferSize = 1000;

	private final int port = 5000;

	public ExtendedSocketUDP(int ID, boolean isServer) {
		this.ID = ID;

		try {
			if (isServer) {
				this.socket = new DatagramSocket(port);
			} else {
				this.socket = new DatagramSocket();
			}
		} catch (Exception e) {

		}
		
		this.destAddress = this.socket.getInetAddress();
		this.destPort = this.socket.getPort();
		initializeStreams();

		System.out.println(this.socket.getInetAddress().getHostAddress());
	}

	/** creates the output and input streams */
	public void initializeStreams() {
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			ready = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** reads object from the stream, returns null if an exception occurs */
	public Object readObject() {
		try {
			byte[] buffer = new byte[bufferSize];
			DatagramPacket packetReceived = new DatagramPacket(buffer,bufferSize); 
			socket.receive(packetReceived); 

			bais = new ByteArrayInputStream(buffer);
			ois = new ObjectInputStream(bais);
			
			return (Object)ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** return true if the object has been sent */
	public Boolean outputObject(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
			// get the byte array of the object
			byte[] buf = baos.toByteArray();
            DatagramPacket packetSent = new DatagramPacket(buf, buf.length , destAddress, destPort);
            socket.send(packetSent);
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
			socket.close(); 
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

