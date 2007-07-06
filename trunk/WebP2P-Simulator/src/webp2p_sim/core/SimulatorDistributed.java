package webp2p_sim.core;

import java.io.File;
import java.util.Set;

import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Browser;
import webp2p_sim.proxy.Proxy;
import webp2p_sim.proxy.RequestGenerator;
import webp2p_sim.server.TrafficGenerator;
import webp2p_sim.server.WebServer;
import webp2p_sim.server.WebServerFactory;
import webp2p_sim.util.RandomLongGenerator;
import webp2p_sim.util.RequestFileGenerator;
import edu.uah.math.distributions.ExponentialDistribution;
import edu.uah.math.distributions.ParetoDistribution;


public class SimulatorDistributed extends Simulator {
	
	public SimulatorDistributed(File inputFile, File topologyFile, int maxTicks) {
		super(maxTicks);
		
		DiscoveryService ds = new DiscoveryService("Discovery Service", new ExponentialDistribution(2));
		WebServerFactory webServerFactory = new WebServerFactory(ds);
		Set<WebServer> servers = webServerFactory.createServers(topologyFile);
		Proxy proxy = new Proxy("Proxy", new ExponentialDistribution(2), ds, new RandomLongGenerator());
		Browser browser = new Browser("Browser", new ParetoDistribution(), proxy);
		RequestGenerator req = new RequestGenerator(browser);
		TrafficGenerator trafficGenerator = new TrafficGenerator(new RequestFileGenerator(new ParetoDistribution(), getNumberOfTicks(), servers).generateRequests());
		req.loadFile(inputFile);
		
		Clock.getInstance().addEntities(servers.toArray(new TimedEntity[servers.size()]));
		Clock.getInstance().addEntities(req, ds, proxy, browser, trafficGenerator);
	}
}
