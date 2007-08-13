package webp2p_sim.core;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Browser;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.InfinitTimeToLive;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.Distribution;

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
		String browserIP = config.getString("browser.ip");
		Distribution browserDist = extractObject(config, "browser.process.distribution");
		
		String webServerIP = config.getString("server.ip");
		Distribution webServerDist = extractObject(config, "server.process.distribution");
		String webServerFile = config.getString("server.file");
		
		Distribution webServerTrafficDist = extractObject(config, "server.traffic.distribution");
		
		buildMe(simTime, seed, browserInputFile, browserIP, browserDist, webServerDist, webServerIP, webServerFile, webServerTrafficDist);
	}
	
	public CentralizedParams(long simTime, int seed, String browserInputFile, String browserIP, Distribution browserDist, Distribution webServerDist, String webServerIP, String  webServerFile, Distribution webServerTrafficDist) {
		buildMe(simTime, seed, browserInputFile,  browserIP, browserDist, webServerDist, webServerIP, webServerFile, webServerTrafficDist);
	}
	
	private void buildMe(long simTime, int seed, String browserInputFile,  String browserIP, Distribution browserDist, Distribution webServerDist, String webServerIP, String  webServerFile, Distribution webServerTrafficDist) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		this.seed = seed;
		this.webServerTrafficDist = webServerTrafficDist;
		
		this.ds = new DiscoveryService("127.0.0.1", new ContinuousUniformDistribution(0, 0));
		this.webServer = new WebServer(webServerIP, webServerDist, ds);
		this.webServer.loadFile(webServerFile, 1, new InfinitTimeToLive());
		this.browser = new Browser(browserIP, browserDist, webServer);
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

