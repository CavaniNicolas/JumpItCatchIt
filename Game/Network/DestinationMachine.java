package Game.Network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DestinationMachine {
	private BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	
	private InetAddress destAddress;
	private int destPortUDP;

	private ObjectOutputStream outputTCP;
	private ObjectInputStream inputTCP;
	private Socket socketTCP;

	public DestinationMachine(Socket socketTCP) {
		this.socketTCP = socketTCP;
		this.destAddress = socketTCP.getInetAddress();
	}

	public InetAddress getDestAddress() {
		return destAddress;
	}

	public void setDestPortUDP(int destPortUDP) {
		this.destPortUDP = destPortUDP;
	}

	public int getDestPortUDP() {
		return destPortUDP;
	}

	public BlockingQueue<Object> getQueue() {
		return queue;
	}

	public Boolean refersTo(InetAddress destAddress, int destPortUDP) {
		return (this.destAddress.equals(destAddress) && this.destPortUDP == destPortUDP);
	}

	/** creates the output and input streams */
	public Boolean initializeStreams() {
		try {
			outputTCP = new ObjectOutputStream(socketTCP.getOutputStream());
			inputTCP = new ObjectInputStream(socketTCP.getInputStream());
			new Thread(new InputHandlerTCP()).start();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** handles client connections */
	public class InputHandlerTCP implements Runnable {
		public void run() {
			Boolean isRunning = true;
			while (isRunning) {
				Object obj = (Object)readObject();
				if (obj == null) {
					isRunning = false;
					queue.offer(new String("PLAYER DISCONNECTION"));
				} else {
					queue.offer(obj);
				}
			}
		}
	}

	/** return true if the object has been sent */
	public Boolean outputObject(Object obj) {
		try {
			outputTCP.writeUnshared(obj);
			outputTCP.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** reads object from the stream, returns null if an exception occurs */
	public Object readObject() {
		try {
			return inputTCP.readUnshared();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** ends the connection, closes the socket */
	public void endConnection() {
		try {
			outputTCP.close(); 
			inputTCP.close(); 
			socketTCP.close(); 
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
