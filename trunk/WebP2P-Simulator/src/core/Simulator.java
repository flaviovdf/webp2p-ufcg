package core;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import proxy.Proxy;
import proxy.RequestGenerator;
import server.WebServer;
import server.WebServerFactory;
import core.entity.TimedEntity;
import ds.DiscoveryService;
import edu.uah.math.distributions.ExponentialDistribution;


public class Simulator {
	
	private int maxTicks;

	public Simulator(File inputFile, File topologyFile, int maxTicks) {
		this.maxTicks = maxTicks;
		
		DiscoveryService ds = new DiscoveryService("Discovery Service", new ExponentialDistribution(2));
		WebServerFactory webServerFactory = new WebServerFactory(new ExponentialDistribution(2), ds);
		Set<WebServer> servers = webServerFactory.createServers(topologyFile);
		Proxy proxy = new Proxy("Proxy", new ExponentialDistribution(2), ds);
		RequestGenerator req = new RequestGenerator(proxy);
		req.loadFile(inputFile);
		
		// TODO Verificar a ordem em que os objetos sao invocados!
		Clock.getInstance().addEntities(servers.toArray(new TimedEntity[0]));
		Clock.getInstance().addEntities(req, ds, proxy);
	}
	
	public void simulate() {
		while (Clock.getInstance().getTicks() != maxTicks) {
			Clock.getInstance().countTick();
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
