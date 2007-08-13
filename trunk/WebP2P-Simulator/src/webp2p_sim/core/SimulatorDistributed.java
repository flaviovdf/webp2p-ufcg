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
import webp2p_sim.util.RequestFileGenerator;


public class SimulatorDistributed extends Simulator {
	
	public SimulatorDistributed(DistributedParams params) {
		super(params);
		
		DiscoveryService ds = params.getDs();
		Set<WebServer> servers = params.getWebServers();
		Proxy proxy = params.getProxy();
		Browser browser = params.getBrowser();
		
		RequestGenerator req = new RequestGenerator(browser);
		TrafficGenerator trafficGenerator = new TrafficGenerator(new RequestFileGenerator(params.getTrafficDistribution(), getNumberOfTicks(), servers).generateRequests());
		req.loadFile(new File(params.getBrowserInputFile()));
		
		Clock.getInstance().addEntities(servers.toArray(new TimedEntity[servers.size()]));
		Clock.getInstance().addEntities(req, ds, proxy, browser, trafficGenerator);
	}
}
