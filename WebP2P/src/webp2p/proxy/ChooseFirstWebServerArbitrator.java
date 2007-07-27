package webp2p.proxy;

public class ChooseFirstWebServerArbitrator implements WebServerArbitrator {

	public String chooseWebServer(String[] wsAddrs) {
		if (wsAddrs.length == 0) return null;
		
		return wsAddrs[0];
	}

}
