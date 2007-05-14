package server;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import proxy.HereIsContentMessage;
import proxy.ProxyImpl;
import core.entity.SimpleQueuedEntity;
import ds.DiscoveryService;
import ds.PutFileRequest;
import edu.uah.math.distributions.Distribution;

public class WebServer extends SimpleQueuedEntity {
	
	private DiscoveryService discoveryService;
	private Set<String> files;
	private List<WebServer> adj;

	private Map<String, ReplicaInfo> url2replicaInfo;
	
	public WebServer(Distribution distribution, DiscoveryService discoveryService) {
		super(distribution);
		this.discoveryService = discoveryService;
		this.files = new HashSet<String>();
		this.adj = new LinkedList<WebServer>();
	}

	void getContent(long request, String url, ProxyImpl proxy) {
		int result = (this.files.contains(url) ? 0 : -1);
		proxy.sendMessage(new HereIsContentMessage(request, result));
	}

	void loadFile(String url, int size) {
		this.files.add(url);
		this.discoveryService.sendMessage(new PutFileRequest(url, this));
	}
	
	Set<String> getFiles() {
		return this.files;
	}
	
	void addAdj(WebServer server) {
		this.adj.add(server);
	}
	
	List<WebServer> getAdj() {
		return this.adj;
	}
	
	@Override
	public void tickOcurred() {
		super.tickOcurred();
		
		Collection<ReplicaInfo> replicaInfos = url2replicaInfo.values();
		for (ReplicaInfo ri : replicaInfos) {
			ri.tickOcurred();
		}
	}
}
