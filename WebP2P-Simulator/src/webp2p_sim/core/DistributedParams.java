package webp2p_sim.core;

import java.io.File;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.browser.Browser;
import webp2p_sim.core.network.Host;
import webp2p_sim.ds.DiscoveryService;
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
	private Proxy proxy;
	private double webServerTrafficMean;

	public DistributedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");

		//Browser
		String browserInputFile = config.getString("browser.inputfile");
		
		String browserIP = config.getString("browser.ip");
		long browserUpBand = config.getLong("browser.upband") * 1024;
		long browserDownBand = config.getLong("browser.downband") * 1024;
		
		Host browserHost = createHost(browserIP, browserUpBand, browserDownBand);
		
		Distribution browserDist = extractObject(config, "browser.process.distribution");
		
		//Proxy
		String proxyIP = config.getString("proxy.ip");
		long proxyUpBand = config.getLong("proxy.upband") * 1024;
		long proxyDownBand = config.getLong("proxy.downband") * 1024;
		
		Host proxyHost = createHost(proxyIP, proxyUpBand, proxyDownBand);
		
		Distribution proxyDist = extractObject(config, "proxy.process.distribution");
		
		//Traffic
		long trafficMean = config.getLong("server.traffic.mean");
		
		//DS
		String dsIP = config.getString("ds.ip");
		Distribution dsDist = extractObject(config, "ds.process.distribution");
		long dsUpBand = config.getLong("ds.upband") * 1024;
		long dsDownBand = config.getLong("ds.downband") * 1024;
		
		Host dsHost = createHost(dsIP, dsUpBand, dsDownBand);
		
		//Servers
		File topologyXML = new File(config.getString("server.topologyfile"));
		
		buildMe(simTime, browserInputFile, browserDist, browserHost, proxyHost, proxyDist, dsDist, dsHost, topologyXML, trafficMean);
	}
	
	public DistributedParams(long simTime, String browserInputFile, Distribution browserDist, Host browserIP, Host proxyIP, Distribution proxyDist, Distribution dsDist, Host dsIP, File topologyXML, long trafficMean) {
		buildMe(simTime, browserInputFile, browserDist, browserIP, proxyIP, proxyDist, dsDist, dsIP, topologyXML, trafficMean);
	}
	
	private void buildMe(long simTime, String browserInputFile, Distribution browserDist, Host browserIP, Host proxyIP, Distribution proxyDist, Distribution dsDist, Host dsIP, File topologyXML, long trafficMean) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		
		this.webServerTrafficMean = trafficMean;
		
		this.ds = new DiscoveryService(dsIP, dsDist, getNetwork(), true);
		this.proxy = new Proxy(proxyIP, proxyDist, getNetwork(), ds.getHost(), new RandomLongGenerator(), true);
		this.webServers = new WebServerFactory(dsIP, getNetwork()).createServers(topologyXML, true);
		this.browser = new Browser(browserIP, browserDist, getNetwork(), proxy.getHost(), true);
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

	public double getTrafficMean() {
		return webServerTrafficMean;
	}

	public Proxy getProxy() {
		return proxy;
	}
}
