package webp2p.discoveryservice;


public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Parameters format: <port>");
			System.exit(1);
		}
		
		try {
			int port = Integer.parseInt(args[0]);
			
			DiscoveryServiceSkel discoveryServiceSkel = new DiscoveryServiceSkel();
			discoveryServiceSkel.start(port);
			
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
		}
	}

}
