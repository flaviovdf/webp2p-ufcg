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
	private Distribution webServerTrafficDist;
	private long trafficUpBand;
	private long trafficDownBand;

	public CentralizedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");
		
		//Browser
		String browserInputFile = config.getString("browser.inputfile");
		
		String browserIP = config.getString("browser.ip");
		long browserUpBand = config.getLong("browser.upband");
		long browserDownBand = config.getLong("browser.downband");
		Host browserHost = createHost(browserIP, browserUpBand, browserDownBand);
		
		Distribution browserDist = extractObject(config, "browser.process.distribution");

		//Server
		String webServerIP = config.getString("server.ip");
		long serverUpBand = config.getLong("server.upband");
		long serverDownBand = config.getLong("server.downband");
		Host serverHost = createHost(webServerIP, serverUpBand, serverDownBand);
		
		Distribution webServerDist = extractObject(config, "server.process.distribution");
		String webServerFile = config.getString("server.file");
		
		//Traffic
		Distribution webServerTrafficDist = extractObject(config, "server.traffic.distribution");
		long trafficUpBand = config.getLong("server.traffic.upband");
		long trafficDownBand = config.getLong("server.traffic.downband");
		
		buildMe(simTime,  browserInputFile, browserHost, browserDist, webServerDist, serverHost, webServerFile, webServerTrafficDist, trafficUpBand, trafficDownBand);
	}
	
	public CentralizedParams(long simTime, String browserInputFile, Host browserHost, Distribution browserDist, Distribution webServerDist, Host webServerHost, String  webServerFile, Distribution webServerTrafficDist, long trafficUpBand, long trafficDownBand) {
		buildMe(simTime, browserInputFile,  browserHost, browserDist, webServerDist, webServerHost, webServerFile, webServerTrafficDist, trafficUpBand, trafficDownBand);
	}
	
	private void buildMe(long simTime, String browserInputFile,  Host browserHost, Distribution browserDist, Distribution webServerDist, Host webServerHost, String  webServerFile, Distribution webServerTrafficDist, long trafficUpBand, long trafficDownBand) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		
		this.webServerTrafficDist = webServerTrafficDist;
		this.trafficUpBand = trafficUpBand;
		this.trafficDownBand = trafficDownBand;
		
		this.ds = new DiscoveryService(new Host(new Address(127, 0, 0, 1), new AsymetricBandwidth(Long.MAX_VALUE, Long.MAX_VALUE)), new ContinuousUniformDistribution(0, 0), getNetwork());
		this.webServer = new WebServer(webServerHost, webServerDist, getNetwork(), ds.getHost());
		this.webServer.loadFile(webServerFile, 1, new InfinitTimeToLive());
		this.browser = new Browser(browserHost, browserDist, getNetwork(), webServer.getHost());
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

	public long getTrafficUpBand() {
		return trafficUpBand;
	}
	
	public long getTrafficDownBand() {
		return trafficDownBand;
	}
}

