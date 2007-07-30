package webp2p.webserver.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class WebServerP2PConfig {
	
	public static final String PROP_DISCOVERYSERVICE_ADRRESS = "ds.addr";
	public static final String PROP_DISCOVERYSERVICE_PORT = "ds.port";
	public static final String PROP_WEBSERVERP2P_LIST = "ws.list";
	public static final String PROP_WEBSERVERP2P_SHAREDFILES = "ws.sharedfiles";
	
	private static final String PROPERTIES_FILENAME = "webserverp2p.properties";
	
	private Properties props;

	private static WebServerP2PConfig uniqueInstance;
	
	private WebServerP2PConfig() {
		this.props = new Properties();
		try {
			this.props.load(new FileInputStream(PROPERTIES_FILENAME));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("The properties file '" + PROPERTIES_FILENAME + "' was not found", e);
		} catch (IOException e) {
			throw new RuntimeException("The properties file '" + PROPERTIES_FILENAME + "' could not be read", e);
		}
	}

	public static WebServerP2PConfig getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new WebServerP2PConfig();
		}
		return uniqueInstance;
	}
	
	public String getDiscoveryServiceAddress() {
		return this.props.getProperty(PROP_DISCOVERYSERVICE_ADRRESS);
	}
	
	public int getDiscoveryServicePort() {
		return Integer.parseInt(this.props.getProperty(PROP_DISCOVERYSERVICE_PORT));
	}

}
