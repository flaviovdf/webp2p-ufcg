package webp2p_sim.core;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.browser.Browser;
import webp2p_sim.core.network.Address;
import webp2p_sim.core.network.AsymetricBandwidth;
import webp2p_sim.core.network.Host;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.InfinitTimeToLive;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.Distribution;

public class CentralizedParams extends Params {

	private DiscoveryService ds;
	private WebServer webServer;
	private Browser browser;
	private String browserInputFile;
	private double webServerTrafficMean;

	public CentralizedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");
		
		//Browser
		String browserInputFile = config.getString("browser.inputfile");
		
		String browserIP = config.getString("browser.ip");
		long browserUpBand = config.getLong("browser.upband") * 1024;
		long browserDownBand = config.getLong("browser.downband") * 1024;
		Host browserHost = createHost(browserIP, browserUpBand, browserDownBand);
		
		Distribution browserDist = extractObject(config, "browser.process.distribution");

		//Server
		String webServerIP = config.getString("server.ip");
		long serverUpBand = config.getLong("server.upband") * 1024;
		long serverDownBand = config.getLong("server.downband") * 1024;
		Host serverHost = createHost(webServerIP, serverUpBand, serverDownBand);
		
		Distribution webServerDist = extractObject(config, "server.process.distribution");
		String webServerFile = config.getString("server.file");
		
		//Traffic
		long trafficMean = config.getLong("server.traffic.mean");
		
		buildMe(simTime,  browserInputFile, browserHost, browserDist, webServerDist, serverHost, webServerFile, trafficMean);
	}
	
	public CentralizedParams(long simTime, String browserInputFile, Host browserHost, Distribution browserDist, Distribution webServerDist, Host webServerHost, String  webServerFile, long trafficMean) {
		buildMe(simTime, browserInputFile,  browserHost, browserDist, webServerDist, webServerHost, webServerFile, trafficMean);
	}
	
	private void buildMe(long simTime, String browserInputFile,  Host browserHost, Distribution browserDist, Distribution webServerDist, Host webServerHost, String  webServerFile, long trafficMean) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;

		this.webServerTrafficMean = trafficMean;
		
		this.ds = new DiscoveryService(new Host(new Address(127, 0, 0, 1), new AsymetricBandwidth(Long.MAX_VALUE, Long.MAX_VALUE)), new ContinuousUniformDistribution(0, 0), getNetwork(), true);
		this.webServer = new WebServer(webServerHost, webServerDist, getNetwork(), ds.getHost(), true, 100, 100, 100);
		this.browser = new Browser(browserHost, browserDist, getNetwork(), webServer.getHost(), true);
		
		this.webServer.loadFile(webServerFile, 70 * 1024 * 8, new InfinitTimeToLive());
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

	public double getTrafficMean() {
		return webServerTrafficMean;
	}
}

