package webp2p.webserver;

import webp2p.util.XMLRPCSkeleton;


public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("* Usage: java -cp [classpath] " + Main.class.getName() + " <port>");
			System.exit(1);
		}
		
		try {
			WebServerP2P.init();
			int port = Integer.parseInt(args[0]);
			XMLRPCSkeleton webserverp2pSkel = new XMLRPCSkeleton("ws", WebServerP2P.class);
			System.out.println("Starting WebServerP2P...");
			webserverp2pSkel.start(port);
			System.out.println("Successfully started!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		}
	}

}
