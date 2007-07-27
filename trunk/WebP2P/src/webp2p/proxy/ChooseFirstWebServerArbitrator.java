package webp2p.proxy;

/**
 * A simple <code>WebServerArbitrator</code> which returns the first <code>WebServer</code>
 * from a list of web servers.
 */
public class ChooseFirstWebServerArbitrator implements WebServerArbitrator {

	/* (non-Javadoc)
	 * @see webp2p.proxy.WebServerArbitrator#chooseWebServer(java.lang.String[])
	 */
	public String chooseWebServer(String[] wsAddrs) {
		if (wsAddrs.length == 0) return null;
		
		return wsAddrs[0];
	}

}