package webp2p.proxy;




public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("* Usage: java -cp [classpath] " + Main.class.getName() + " <port> <dsAddr> <dsPort>");
			System.exit(1);
		}
		
		try {
			int port = Integer.parseInt(args[0]);
			String dsAddr = args[1];
			int dsPort = Integer.parseInt(args[2]);
			ProxySkel proxySkel = new ProxySkel(dsAddr, dsPort);
			System.out.println("Proxy listening...");
			proxySkel.start(port);
			System.out.println("Bye!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		}
	}

}
