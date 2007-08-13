package webp2p_sim.core;

import java.io.File;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Browser;
import webp2p_sim.proxy.Proxy;
import webp2p_sim.server.WebServer;
import webp2p_sim.server.WebServerFactory;
import webp2p_sim.util.RandomLongGenerator;
import edu.uah.math.distributions.Distribution;

public class DistributedParams extends Params {

	private DiscoveryService ds;
	private Browser browser;
	private String browserInputFile;
	private Set<WebServer> webServers;
	private Distribution trafficDist;
	private Proxy proxy;

	public DistributedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");
		int seed = config.getInt("sim.seed");

		String browserIP = config.getString("browser.ip");
		String browserInputFile = config.getString("browser.inputfile");
		Distribution browserDist = extractObject(config, "browser.process.distribution");
		
		String proxyIP = config.getString("proxy.ip");
		Distribution proxyDist = extractObject(config, "proxy.process.distribution");
		
		Distribution trafficDist = extractObject(config, "server.traffic.distribution");
		
		String dsIP = config.getString("browser.ip");
		Distribution dsDist = extractObject(config, "ds.process.distribution");
		
		File topologyXML = new File(config.getString("server.topologyfile"));
		
		buildMe(simTime, seed, browserInputFile, browserDist, browserIP, proxyIP, proxyDist, trafficDist, dsDist, dsIP, topologyXML);
	}
	
	public DistributedParams(long simTime, int seed, String browserInputFile, Distribution browserDist, String browserIP, String proxyIP, Distribution proxyDist, Distribution trafficDist, Distribution dsDist, String dsIP, File topologyXML) {
		buildMe(simTime, seed, browserInputFile, browserDist, browserIP, proxyIP, proxyDist, trafficDist, dsDist, dsIP, topologyXML);
	}
	
	private void buildMe(long simTime, int seed, String browserInputFile, Distribution browserDist, String browserIP, String proxyIP, Distribution proxyDist, Distribution trafficDist, Distribution dsDist, String dsIP, File topologyXML) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		this.seed = seed;
		this.trafficDist = trafficDist;
		
		this.ds = new DiscoveryService(dsIP, dsDist);
		this.proxy = new Proxy(proxyIP, proxyDist, ds, new RandomLongGenerator());
		this.webServers = new WebServerFactory(ds).createServers(topologyXML);
		this.browser = new Browser(browserIP, browserDist, proxy);
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

	public Proxy getProxy() {
		return proxy;
	}
}
