package webp2p_sim.core;

import java.io.File;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Browser;
import webp2p_sim.server.ContentIF;
import webp2p_sim.server.WebServer;
import webp2p_sim.server.WebServerFactory;
import edu.uah.math.distributions.Distribution;

public class DistributedParams extends Params {

	private DiscoveryService ds;
	private WebServer webServer;
	private Browser browser;
	private String browserInputFile;
	private Set<WebServer> webServers;
	private Distribution trafficDist;

	public DistributedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");
		int seed = config.getInt("sim.seed");

		String browserIP = config.getString("browser.ip");
		String browserInputFile = config.getString("browser.inputfile");
		String[] distDefinitionArray = config.getStringArray("browser.process.distribution");
		Distribution browserDist = extractObject(distDefinitionArray);
		
		distDefinitionArray = config.getStringArray("server.traffic.distribution");
		Distribution trafficDist = extractObject(distDefinitionArray);
		
		String dsIP = config.getString("browser.ip");
		distDefinitionArray = config.getStringArray("ds.process.distribution");
		Distribution dsDist = extractObject(distDefinitionArray);
		
		File topologyXML = new File(config.getString("server.topologyfile"));
		
		buildMe(simTime, seed, browserInputFile, browserDist, browserIP, trafficDist, dsDist, dsIP, topologyXML);
	}
	
	public DistributedParams(long simTime, int seed, String browserInputFile, Distribution browserDist, String browserIP, Distribution trafficDist, Distribution dsDist, String dsIP, File topologyXML) {
		buildMe(simTime, seed, browserInputFile, browserDist, browserIP, trafficDist, dsDist, dsIP, topologyXML);
	}
	
	private void buildMe(long simTime, int seed, String browserInputFile, Distribution browserDist, String browserIP, Distribution trafficDist, Distribution dsDist, String dsIP, File topologyXML) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		this.seed = seed;
		this.trafficDist = trafficDist;
		
		this.ds = new DiscoveryService(dsIP, dsDist);
		this.webServers = new WebServerFactory(ds).createServers(topologyXML);
		this.browser = new Browser(browserIP, browserDist, chooseWebServer());
	}

	//FIXME implementar
	private ContentIF chooseWebServer() {
		return this.webServers.iterator().next();
	}

	public String getBrowserInputFile() {
		return browserInputFile;
	}

	public DiscoveryService getDs() {
		return ds;
	}

	public Set<WebServer> getWebServers() {
		return webServers;
	}

	public Browser getBrowser() {
		return browser;
	}

	public Distribution getTrafficDistribution() {
		return trafficDist;
	}
}
