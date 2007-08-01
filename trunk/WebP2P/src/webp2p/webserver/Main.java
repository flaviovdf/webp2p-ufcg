package webp2p.webserver;

import webp2p.util.XMLRPCSkeleton;


public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("* Usage: java -cp [classpath] " + Main.class.getName() + " <port>");
			System.exit(1);
		}
		
		try {
			int port = Integer.parseInt(args[0]);
			System.out.println("Starting WebServerP2P...");
			WebServerP2P.init();
			XMLRPCSkeleton webserverp2pSkel = new XMLRPCSkeleton("ws", WebServerP2P.class);
			webserverp2pSkel.start(port);
			System.out.println("Successfully started!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		}
	}

}
