package webp2p_sim.server;

import webp2p_sim.proxy.GeneratorInterested;

// generate straffic on webservers
public class FakeRequestGenerator implements GeneratorInterested {

	private WebServer server;

	public FakeRequestGenerator(WebServer server) {
		this.server = server;
	}
	
	public void generateRequest(String url) {
		// TODO Auto-generated method stub
		// gerar os requests diretamento nos webservers
	}

}
