package core;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import proxy.ProxyImpl;
import proxy.RequestGenerator;
import server.WebServer;
import server.WebServerFactory;
import ds.DiscoveryService;
import edu.uah.math.distributions.ExponentialDistribution;


public class Simulator {
	
	private Clock clock;
	private int maxTicks;

	public Simulator(File inputFile, File topologyFile, int maxTicks) {
		this.maxTicks = maxTicks;
		this.clock = new Clock();
		
		DiscoveryService ds = new DiscoveryService(new ExponentialDistribution(2));
		WebServerFactory webServerFactory = new WebServerFactory(new ExponentialDistribution(2), ds);
		Set<WebServer> servers = webServerFactory.createServers(topologyFile);
		ProxyImpl proxy = new ProxyImpl(new ExponentialDistribution(2), ds);
		RequestGenerator req = new RequestGenerator(proxy);
		req.loadFile(inputFile);
		
		// TODO Verificar a ordem em que os objetos sao invocados!
		for (WebServer server : servers) {
			this.clock.addEntity(server);
		}
		this.clock.addEntity(req, ds, proxy);
	}
	
	public void simulate() {
		while (clock.getTicks() != maxTicks) {
			this.clock.countTick();
		}
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.err.println("* Usage: java " + Simulator.class.getName() + " <fileName> <topologyFile> <maxTicks>");
			System.exit(1);
		}
		
		PropertyConfigurator.configure("log4j.properties");
		
		Simulator simulator = new Simulator(new File(args[0]), new File(args[1]), Integer.parseInt(args[2]));
		simulator.simulate();
	}
}
