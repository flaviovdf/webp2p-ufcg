package webp2p_sim.server;

import java.util.List;

// generate traffic on webservers
public class FakeRequestGenerator {

	private List<WebServer> servers;

	public FakeRequestGenerator(List<WebServer> servers) {
		this.servers = servers;
	}
	
	public void generateRequests() {
		// TODO Auto-generated method stub
		// gerar os requests diretamente nos webservers
	}
}
