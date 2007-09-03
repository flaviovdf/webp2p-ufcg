package webp2p.loadmeter;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;

import webp2p.webserver.WebServerStub;

public class ProxyLoadListener implements LoadListener, AsyncCallback {

	private static final Logger LOG = Logger.getLogger(ProxyLoadListener.class);
	
	private WebServerStub webServerStub;
	
	public ProxyLoadListener(String webServerAddress) {
		this.webServerStub = new WebServerStub(webServerAddress);
	}
	
	
	public void overheadDetected(LoadEvent event) {
		List<FilesToDownloadRate> metric = event.getRankFilesList();
		List<String> files = new LinkedList<String>();
		
		for (FilesToDownloadRate file: metric) {
			files.add(file.getFile());
		}
		
		LOG.debug("LoadMeter delivered the notification of overhead for the following files: "+files);
		
		try {
			webServerStub.overheadDetected(files, this);
		} catch (XmlRpcException e) {
			LOG.error("Could not notify the server", e);
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