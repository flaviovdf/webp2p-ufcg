package webp2p.proxy;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.proxy.arbitrator.ChooseFirstWebServerArbitrator;

public class ProxyContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent arg0) {
		DiscoveryServiceStub dsStub = null;
		ChooseFirstWebServerArbitrator arbitrator = new ChooseFirstWebServerArbitrator();
		
		try {
			Properties props = new Properties();
			props.load(this.getClass().getResourceAsStream("/proxy.properties"));

			String dsAddr = props.getProperty("ds.addr");
			int dsPort = Integer.parseInt(props.getProperty("ds.port"));
			
			dsStub = new DiscoveryServiceStub(dsAddr, dsPort);
		} catch (IOException e) {
			// if this happens, the 'dsStub' reference will be 'null'
		}
		
		Proxy.getInstance().config(dsStub, arbitrator);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}

}
