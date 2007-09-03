package webp2p_sim.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import webp2p_sim.browser.Browser;
import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Proxy;
import webp2p_sim.proxy.RequestGenerator;
import webp2p_sim.server.P2PTrafficGenerator;
import webp2p_sim.server.TrafficGenerator;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RequestFileGenerator;
import webp2p_sim.util.UrlToWebServer;

public class SimulatorDistributed extends Simulator {
	
	public SimulatorDistributed(DistributedParams params) {
		super(params);
		
		DiscoveryService ds = params.getDs();
		
		Set<WebServer> servers = params.getWebServers();
		
		Proxy proxy = params.getProxy();
		
		Browser browser = params.getBrowser();
		RequestGenerator req = new RequestGenerator(browser);
		req.loadFile(new File(params.getBrowserInputFile()));
		
		List<String> filesToOverload = params.getFilesToOverload();
		
		//STA
		Set<WebServer> toReceive = new HashSet<WebServer>(); 
		for (String file: filesToOverload) {
			for (WebServer server : servers) {
				if (!server.getFiles().contains(file)) {
					toReceive.add(server);
				}
			}
		}
		
		Map<Long, List<UrlToWebServer>> generateRequests = new RequestFileGenerator(params.getTrafficMean(), getNumberOfTicks(), toReceive).generateRequests();
		TrafficGenerator trafficGenerator = new TrafficGenerator(generateRequests, params.getNetwork());

		//We overload files from a server, but replicas of it will be created
		Map<Long, List<String>> overLoadRequests = new HashMap<Long, List<String>>();
		long mean = params.getOverloadedMean();
		for (long t = 0; t < params.getSimTime(); t++) {
			
			List<String> toAdd = new ArrayList<String>();
			
			for (long i = 0; i < mean; i++) {
				toAdd.addAll(filesToOverload);
			}
			
			overLoadRequests.put(t, toAdd);
		}
		
		P2PTrafficGenerator overLoadGenerator = new P2PTrafficGenerator(overLoadRequests, params.getNetwork(), ds.getHost());
		
		//Starting simulation
		Clock.getInstance().addEntities(servers.toArray(new TimedEntity[servers.size()]));
		Clock.getInstance().addEntities(req, ds, proxy, browser, trafficGenerator, overLoadGenerator);
	}

}
