package Game.Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class ExtendedSocketUDP {
	private Boolean acceptingConnection;
	private int currentPlayerNumber = 0;
	private int maxPlayerNumber;

	private DatagramSocket socket;
	private ArrayList<DestinationMachine> destMachine = new ArrayList<DestinationMachine>();
	private ArrayList<PlayerState> playerStates;
	private ByteArrayOutputStream baos;
	private ObjectOutputStream oos;

	private BoardServerUDP boardServer;

	private ByteArrayInputStream bais;
	private ObjectInputStream ois;

	private final int bufferSize = 10000;
	byte[] buffer = new byte[bufferSize];

	private final int port = 5000;

	/** server side socket */
	public ExtendedSocketUDP(int maxPlayerNumber, ArrayList<PlayerState> playerStates, BoardServerUDP boardServer) {
		this.maxPlayerNumber = maxPlayerNumber;
		this.playerStates = playerStates;
		this.boardServer = boardServer;
		acceptingConnection = true;
		initializeStreams(true);
	}

	/** client side socket */
	public ExtendedSocketUDP() {
		acceptingConnection = false;
	}

	public Boolean initializeConnection(String destAddress, int destPort) {
		try {
			this.destMachine.add(new DestinationMachine(InetAddress.getByName(destAddress), destPort, 0));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** creates the output and input streams */
	public Boolean initializeStreams(Boolean isServer) {
		try { 
			if (isServer) {
				socket = new DatagramSocket(port);
			} else {
				socket = new DatagramSocket();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** reads object from the stream, returns null if an exception occurs */
	public IdentifiedObject readObject() {
		try {
			DatagramPacket packetReceived = new DatagramPacket(buffer, bufferSize); 
			socket.receive(packetReceived); 

			bais = new ByteArrayInputStream(buffer);
			ois = new ObjectInputStream(bais);

			if (acceptingConnection) {
			 	if (currentPlayerNumber < maxPlayerNumber) {
					DestinationMachine newDestMachine = new DestinationMachine(packetReceived.getAddress(), packetReceived.getPort(), currentPlayerNumber);
					if (!destMachine.contains(newDestMachine)) {
						destMachine.add(newDestMachine);
						playerStates.add(new PlayerState(currentPlayerNumber));
						currentPlayerNumber++;
						boardServer.setCurrentPlayerNumber(currentPlayerNumber);
					} 
				} else {
					acceptingConnection = false;
				}
			}

			for (DestinationMachine destinationMachine : destMachine) {
				if (destinationMachine.equals(new DestinationMachine(packetReceived.getAddress(), packetReceived.getPort(), 0))) {
					return new IdentifiedObject(destinationMachine.getId(), (Object)ois.readObject());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean outputObject(Object obj) {
		return outputObject(obj, 0);
	}

	/** return true if the object has been sent */
	public Boolean outputObject(Object obj, int id) {
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);

			oos.writeUnshared(obj);
			oos.flush();
			// get the byte array of the object
			byte[] buf = baos.toByteArray();
			DatagramPacket packetSent = new DatagramPacket(buf, buf.length , destMachine.get(id).getDestAddress(), destMachine.get(id).getDestPort());
			socket.send(packetSent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			endConnection();
			return false;
		}
	}

	/** return true if the object has been sent */
	public void outputObjectToAll(Object obj) {
		for (DestinationMachine dMachine : destMachine) {
			outputObject(obj, dMachine.getId());
		}
	}

	/**closes the socket */
	public void endConnection() {
		try {
			socket.close(); 
			destMachine.clear();
			playerStates.clear();
			currentPlayerNumber = 0;
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}