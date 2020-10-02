package Game.Network;

import java.net.InetAddress;

public class DestinationMachine {
	private InetAddress destAddress;
	private int destPort;
	private int id;
	private Boolean ready;

	public DestinationMachine(InetAddress destAddress, int destPort, int id) {
		this.destAddress = destAddress;
		this.destPort = destPort;
		this.id = id;
	}

	public InetAddress getDestAddress() {
		return destAddress;
	}

	public int getDestPort() {
		return destPort;
	}

	public int getId() {
		return id;
	}
	
	public void setReady(Boolean ready) {
		this.ready = ready;
	}

	public Boolean isReady() {
		return ready;
	}

	public Boolean equals(DestinationMachine destMachine) {
		return destMachine.getDestAddress().equals(destAddress) && destMachine.getDestPort() == destPort;
	}

	@Override
	public String toString() {
		return "DestinationMachine [destAddress=" + destAddress + ", destPort=" + destPort + ", id=" + id + ", ready="
				+ ready + "]";
	}
}
