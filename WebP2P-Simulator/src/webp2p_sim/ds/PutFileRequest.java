package webp2p_sim.ds;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.core.network.Host;

public class PutFileRequest extends AbstractApplicationMessage {

	private String url;
	private Host server;

	public PutFileRequest(String url, Host server) {
		this.url = url;
		this.server = server;
	}

	public void realProcess() {
		((DiscoveryService) receiverEntity).putRequest(this.url, this.server);
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof PutFileRequest)) return false;
		
		PutFileRequest other = (PutFileRequest) o;
		return this.url.equals( other.url ) && this.server.equals( other.server );
	}

}
