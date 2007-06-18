package webp2p_sim.server;

import java.util.List;

import webp2p_sim.proxy.Proxy;

// generate traffic on webservers
public class FakeRequestGenerator {

	private List<WebServer> servers;
	private Proxy proxy;
	public static final long id = 12345;
	
	
	public FakeRequestGenerator(List<WebServer> servers, Proxy proxy) {
		this.servers = servers;
		this.proxy = proxy;
	}
	/**
	 * @param rate requests per time unit (tick) 
	 */
	public void generateRequests(int rate, String url) {
		for (WebServer server : this.servers) {
			for (int i= 0 ; i<rate ; i++) {
				server.sendMessage(new GetContentRequest(id,url, proxy));
			}
		}
	}
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((proxy == null) ? 0 : proxy.hashCode());
		result = PRIME * result + ((servers == null) ? 0 : servers.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FakeRequestGenerator other = (FakeRequestGenerator) obj;
		if (proxy == null) {
			if (other.proxy != null)
				return false;
		} else if (!proxy.equals(other.proxy))
			return false;
		if (servers == null) {
			if (other.servers != null)
				return false;
		} else if (!servers.equals(other.servers))
			return false;
		return true;
	}
}
