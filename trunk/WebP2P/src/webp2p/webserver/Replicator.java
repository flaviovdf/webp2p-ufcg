package webp2p.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;

import webp2p.util.LineReader;

public class Replicator implements AsyncCallback {

	private List<WebServerStub> adjacents;
	
	public Replicator() {
		this.adjacents = new LinkedList<WebServerStub>();
	}
	
	public void loadAdjacents(String adjacentsFile) {
		try {
			List<String> adjacentsToLoad = LineReader.readFile(new File(adjacentsFile), "#");
			
			for (String adjacentURL : adjacentsToLoad) {
				this.addAdjacent(adjacentURL);
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File " + adjacentsFile + " not found", e);
		}
	}

	public void addAdjacent(String webserverp2pAddr) {
		this.adjacents.add(new WebServerStub(webserverp2pAddr));
	}
	
	public void replicateContent(String url) {
		if (! this.adjacents.isEmpty() ) {
			int random = (int) Math.round(Math.random() * this.adjacents.size());
			WebServerStub webServerStub = this.adjacents.get(random);
			try {
				webServerStub.storeReplica(url, this);
			} catch (XmlRpcException e) {
				// could not replicate the content
			}
		}
	}

	public void handleError(XmlRpcRequest request, Throwable error) {
	}

	public void handleResult(XmlRpcRequest request, Object result) {
	}
	
}
