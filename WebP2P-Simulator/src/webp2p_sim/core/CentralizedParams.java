package webp2p_sim.core;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Browser;
import webp2p_sim.server.WebServer;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.ExponentialDistribution;

public class CentralizedParams extends Params {

	private DiscoveryService ds;
	private WebServer webServer;
	private Browser browser;
	private String browserInputFile;
	private Distribution webServerTrafficDist;

	public CentralizedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");
		int seed = config.getInt("sim.seed");
		
		String browserInputFile = config.getString("browser.inputfile");
		String[] distDefinitionArray = config.getStringArray("browser.process.distribution");
		Distribution browserDist = extractObject(distDefinitionArray);
		
		distDefinitionArray = config.getStringArray("server.process.distribution");
		Distribution webServerDist = extractObject(distDefinitionArray);
		
		distDefinitionArray = config.getStringArray("server.traffic.distribution");
		Distribution webServerTrafficDist = extractObject(distDefinitionArray);
		
		buildMe(simTime, seed, browserInputFile, browserDist, webServerDist, webServerTrafficDist);
	}
	
	public CentralizedParams(long simTime, int seed, String browserInputFile, Distribution browserDist, Distribution webServerDist, Distribution webServerTrafficDist) {
		buildMe(simTime, seed, browserInputFile, browserDist, webServerDist, webServerTrafficDist);
	}
	
	private void buildMe(long simTime, int seed, String browserInputFile, Distribution browserDist, Distribution webServerDist, Distribution webServerTrafficDist) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		this.seed = seed;
		this.webServerTrafficDist = webServerTrafficDist;
		
		this.ds = new DiscoveryService("FAKE Discovery Service", new ContinuousUniformDistribution(0, 0));
		this.webServer = new WebServer("192.168.254.1", new ExponentialDistribution(1f/50), ds);
		this.browser = new Browser("Browser", browserDist, webServer);
	}

	public String getBrowserInputFile() {
		return browserInputFile;
	}

	public DiscoveryService getDs() {
		return ds;
	}

	public WebServer getWebServer() {
		return webServer;
	}

	public Browser getBrowser() {
		return browser;
	}

	public Distribution getTrafficDistribution() {
		return webServerTrafficDist;
	}
}
