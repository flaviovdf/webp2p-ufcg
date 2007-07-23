package webp2p.discoveryservice;


public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("* Usage: java -cp [classpath] " + Main.class.getName() + " <port>");
			System.exit(1);
		}
		
		try {
			int port = Integer.parseInt(args[0]);
			DiscoveryServiceSkel discoveryServiceSkel = new DiscoveryServiceSkel();
			System.out.println("Starting DiscoveryService...");
			discoveryServiceSkel.start(port);
			System.out.println("Successfully started!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		}
	}

}
