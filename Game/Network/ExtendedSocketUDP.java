package Game.Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class ExtendedSocketUDP {
	private DatagramSocket socketUDP;
	private ServerSocket socketServerTCP;
	private ArrayList<DestinationMachine> destMachine = new ArrayList<DestinationMachine>();
	private ByteArrayOutputStream baos;
	private ObjectOutputStream oos;

	private ByteArrayInputStream bais;
	private ObjectInputStream ois;

	private int portNumberUDP;

	private final int bufferSize = 10000;
	byte[] buffer = new byte[bufferSize];

	public ExtendedSocketUDP(int portNumberUDP) {
		this.portNumberUDP = portNumberUDP;
	}

	public DestinationMachine initializeConnection(String destAddress, int destPortTCP, int destPortUDP) {
		try {
			DestinationMachine dest = new DestinationMachine(new Socket(destAddress, destPortTCP));
			if (dest.initializeStreams()) {
				destMachine.add(dest);
				dest.setDestPortUDP(destPortUDP);
				return dest;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean createServer(int portNumber) {
		try {
			socketServerTCP = new ServerSocket(portNumber);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/** creates the output and input streams */
	public Boolean initializeStreams() {
		try { 
			socketUDP = new DatagramSocket(portNumberUDP);
			new Thread(new HandleUDPReading()).start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** handles every object received */
	public class HandleUDPReading extends Thread {
		public void run() {
			Boolean isRunning = true;
			while (isRunning) {
				try {
					DatagramPacket packetReceived = new DatagramPacket(buffer, bufferSize); 
					socketUDP.receive(packetReceived); 

					bais = new ByteArrayInputStream(buffer);
					ois = new ObjectInputStream(bais);

					for (DestinationMachine destinationMachine : destMachine) {
						if (destinationMachine.refersTo(packetReceived.getAddress(), packetReceived.getPort())) {
							destinationMachine.getQueue().offer((Object)ois.readObject());
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** return true if the object has been sent */
	public Boolean outputUDPObject(Object obj, DestinationMachine destMachine) {
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);

			oos.writeUnshared(obj);
			oos.flush();
			// get the byte array of the object
			byte[] buf = baos.toByteArray();
			DatagramPacket packetSent = new DatagramPacket(buf, buf.length , destMachine.getDestAddress(), destMachine.getDestPortUDP());
			socketUDP.send(packetSent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** return true if the object has been sent */
	public Boolean outputObjectToAll(Object obj, Boolean protocolUDP) {
		Boolean result = true;
		if (protocolUDP) {
			for (DestinationMachine dMachine : destMachine) {
				if (!outputUDPObject(obj, dMachine)) {
					result = false;
				}
			}
		} else {
			for (DestinationMachine dMachine : destMachine) {
				if (!dMachine.outputObject(obj)) {
					result = false;
				}
			}
		}
		return result;
	}

	/**closes the socket */
	public void endConnection() {
		try {
			socketUDP.close(); 
			destMachine.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DestinationMachine awaitConnection() {
		try {
			DestinationMachine dest = new DestinationMachine(socketServerTCP.accept());
			dest.initializeStreams();
			
			destMachine.add(dest);
			return dest;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}