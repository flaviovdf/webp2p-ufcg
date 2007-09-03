package webp2p.proxy.arbitrator;

/**
 * A simple <code>WebServerArbitrator</code> which returns a random <code>WebServerP2P</code>
 * from a list of web servers.
 */
public class ChooseRandomWebServerArbitrator implements WebServerArbitrator {

	/* (non-Javadoc)
	 * @see webp2p.proxy.WebServerArbitrator#chooseWebServer(java.lang.String[])
	 */
	public String chooseWebServer(String[] wsAddrs) {
		if (wsAddrs.length == 0) return null;
		
		int index = (int) (Math.random() * wsAddrs.length);
		
		return wsAddrs[index];
	}

}
