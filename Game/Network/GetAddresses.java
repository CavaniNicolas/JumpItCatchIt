package Game.Network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetAddresses {
	public static void main(String[] args) {
		Boolean addressFound = false;
		try {
			Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
			
			while(list.hasMoreElements() && !addressFound){
			  
			NetworkInterface ni = list.nextElement();
			Enumeration<InetAddress> listAddress = ni.getInetAddresses();
			
				while(listAddress.hasMoreElements()){
					InetAddress address = listAddress.nextElement();
					if (!address.isAnyLocalAddress() && !address.isLoopbackAddress() && !address.isLinkLocalAddress() && !address.isSiteLocalAddress()) {
						showInformations(address);
					}
				}
		   	}
		} catch (SocketException e) {
		   e.printStackTrace();
		}
	 }
	 
	 public static void showInformations(InetAddress address){
		System.out.println("-----------------------------------------------");
		System.out.println("Nom  : " + address.getHostName());
		System.out.println("Adresse : " + address.getHostAddress());
		System.out.println("Nom canonique : " + address.getCanonicalHostName());
	 }
}