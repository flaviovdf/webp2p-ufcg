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
	private Distribution trafficDist;
	private Proxy proxy;
	private long trafficUpBand;
	private long trafficDownBand;

	public DistributedParams(Configuration config) {
		long simTime = config.getLong("sim.runtime");

		//Browser
		String browserInputFile = config.getString("browser.inputfile");
		
		String browserIP = config.getString("browser.ip");
		long browserUpBand = config.getLong("browser.upband");
		long browserDownBand = config.getLong("browser.downband");
		
		Host browserHost = createHost(browserIP, browserUpBand, browserDownBand);
		
		Distribution browserDist = extractObject(config, "browser.process.distribution");
		
		//Proxy
		String proxyIP = config.getString("proxy.ip");
		long proxyUpBand = config.getLong("proxy.upband");
		long proxyDownBand = config.getLong("proxy.downband");
		
		Host proxyHost = createHost(proxyIP, proxyUpBand, proxyDownBand);
		
		Distribution proxyDist = extractObject(config, "proxy.process.distribution");
		
		//Traffic
		Distribution trafficDist = extractObject(config, "server.traffic.distribution");
		long trafficUpBand = config.getLong("server.traffic.upband");
		long trafficDownBand = config.getLong("server.traffic.downband");
		
		//DS
		String dsIP = config.getString("browser.ip");
		Distribution dsDist = extractObject(config, "ds.process.distribution");
		long dsUpBand = config.getLong("ds.upband");
		long dsDownBand = config.getLong("ds.downband");
		
		Host dsHost = createHost(dsIP, dsUpBand, dsDownBand);
		
		//Servers
		File topologyXML = new File(config.getString("server.topologyfile"));
		
		buildMe(simTime, browserInputFile, browserDist, browserHost, proxyHost, proxyDist, trafficDist, dsDist, dsHost, topologyXML, trafficUpBand, trafficDownBand);
	}
	
	public DistributedParams(long simTime, String browserInputFile, Distribution browserDist, Host browserIP, Host proxyIP, Distribution proxyDist, Distribution trafficDist, Distribution dsDist, Host dsIP, File topologyXML, long trafficUpBand, long trafficDownBand) {
		buildMe(simTime, browserInputFile, browserDist, browserIP, proxyIP, proxyDist, trafficDist, dsDist, dsIP, topologyXML, trafficUpBand, trafficDownBand);
	}
	
	private void buildMe(long simTime, String browserInputFile, Distribution browserDist, Host browserIP, Host proxyIP, Distribution proxyDist, Distribution trafficDist, Distribution dsDist, Host dsIP, File topologyXML, long trafficUpBand, long trafficDownBand) {
		this.browserInputFile = browserInputFile;
		this.simTime = simTime;
		
		this.trafficDist = trafficDist;
		this.trafficUpBand = trafficUpBand;
		this.trafficDownBand = trafficDownBand;
		
		this.ds = new DiscoveryService(dsIP, dsDist, getNetwork());
		this.proxy = new Proxy(proxyIP, proxyDist, getNetwork(), ds.getHost(), new RandomLongGenerator());
		this.webServers = new WebServerFactory(ds, getNetwork()).createServers(topologyXML);
		this.browser = new Browser(browserIP, browserDist, getNetwork(), proxy.getHost());
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
	
	public long getTrafficUpBand() {
		return trafficUpBand;
	}
	
	public long getTrafficDownBand() {
		return trafficDownBand;
	}
}
