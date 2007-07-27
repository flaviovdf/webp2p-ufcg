package webp2p.proxy;

/**
 * This class is responsible to choose a <code>WebServer</code> among a list of web servers.
 */
public interface WebServerArbitrator {

	/**
	 * Returns the chosen <code>WebServer</code>.
	 * 
	 * @param wsAddrs The array of web servers.
	 * @return The chosen <code>WebServer</code>.
	 */
	public String chooseWebServer(String[] wsAddrs);

}
