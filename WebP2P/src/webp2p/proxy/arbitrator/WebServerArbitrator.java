package webp2p.proxy.arbitrator;


/**
 * This class is responsible to choose a <code>WebServerP2P</code> among a list of web servers.
 */
public interface WebServerArbitrator {

	/**
	 * Returns the chosen <code>WebServerP2P</code>.
	 * 
	 * @param wsAddrs The array of web servers.
	 * @return The chosen <code>WebServerP2P</code>.
	 */
	public String chooseWebServer(String[] wsAddrs);
	
}
