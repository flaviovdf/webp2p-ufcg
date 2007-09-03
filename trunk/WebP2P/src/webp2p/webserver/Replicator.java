package webp2p.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;

import webp2p.util.LineReader;

public class Replicator implements AsyncCallback {
	
	private static final Logger LOG = Logger.getLogger(Replicator.class);

	private List<String> adjacents;
	private Map<String, Set<String>> currentReplicas;
	private int replicaValidity;
	
	public Replicator(int replicaValidity) {
		this.adjacents = new LinkedList<String>();
		this.currentReplicas = new HashMap<String, Set<String>>();
		this.replicaValidity = replicaValidity;
	}
	
	public void loadAdjacents(String adjacentsFile) {
		LOG.info("Loading adjacents from file: " + adjacentsFile);
		try {
			List<String> adjacentsToLoad = LineReader.readFile(new File(adjacentsFile), "#");
			
			for (String adjacentURL : adjacentsToLoad) {
				this.addAdjacent(adjacentURL);
			}
		} catch (FileNotFoundException e) {
			LOG.error("File " + adjacentsFile + " not found", e);
		}
	}

	public void addAdjacent(String webserverp2pAddr) {
		this.adjacents.add(webserverp2pAddr);
		LOG.debug("Adjacent " + webserverp2pAddr + " added");
	}
	
	public void replicateContent(String... files) {
		if (! this.adjacents.isEmpty() ) {
				for (String url : files) {
					int random = (int) (Math.random() * this.adjacents.size());
					String webServerAddr = this.adjacents.get(random);
					
					LOG.debug("Trying to replicate the content " + url + " to " + webServerAddr);
					
					synchronized (this.currentReplicas) {
						if (! this.currentReplicas.containsKey(url) ) {
							this.currentReplicas.put(url, new HashSet<String>());
						}

						Set<String> servers = this.currentReplicas.get(url);
						if (! servers.contains(webServerAddr) ) {
							servers.add(webServerAddr);

							LOG.debug("Scheduling a thread to remove the file " + url + " from server " + webServerAddr + " in " + this.replicaValidity + " milliseconds");
							new Timer().schedule(new ReplicaManager(this.currentReplicas, url, webServerAddr), this.replicaValidity);

							try {
								new WebServerStub(webServerAddr).storeReplica(url, this);
							} catch (XmlRpcException e) {
								LOG.error("Could not replicate the content " + url + " for the server " + webServerAddr, e);
							}
						} else {
							LOG.debug("Skipping the server " + webServerAddr + " because it already has a replica for file " + url);
						}
					}
				}
		} else {
			LOG.debug("Empty adjacents list");
		}
	}
	
	private class ReplicaManager extends TimerTask {

		private Map<String, Set<String>> currentReplicas;
		private String url;
		private String webServerAddr;
		
		public ReplicaManager(Map<String, Set<String>> currentReplicas, String url, String webServerAddr) {
			this.currentReplicas = currentReplicas;
			this.url = url;
			this.webServerAddr= webServerAddr;
		}

		@Override
		public void run() {
			LOG.debug("Removing the replica " + this.url + " from server " + this.webServerAddr);
			synchronized (this.currentReplicas) {
				Set<String> list = this.currentReplicas.get(this.url);
				if (list != null) list.remove(this.webServerAddr);
			}
		}
		
	}

	public void handleError(XmlRpcRequest pRequest, Throwable pError) {
		// TODO Auto-generated method stub
		LOG.error(pError);
	}

	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		// TODO Auto-generated method stub
		LOG.debug(pResult);
	}
	
}
