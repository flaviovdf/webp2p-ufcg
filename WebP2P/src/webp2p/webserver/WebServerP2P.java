package webp2p.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.util.LineReader;
import webp2p.webserver.config.WebServerP2PConfig;

public class WebServerP2P {
	
	private static final String SHAREDFILES_FILENAME = "webserverp2p.sharedfiles";
	
	public WebServerP2P() {
	}
	
	static void init() {
		DiscoveryServiceStub discoveryService;
		
		String dsAddr = WebServerP2PConfig.getInstance().getDiscoveryServiceAddress();
		int dsPort = WebServerP2PConfig.getInstance().getDiscoveryServicePort();
		discoveryService = new DiscoveryServiceStub(dsAddr, dsPort);
		
		String wsAddr = WebServerP2PConfig.getInstance().getWebServerP2PAddress();
		int wsPort = WebServerP2PConfig.getInstance().getWebServerP2PPort();
		String wsFullAddr = "http://" + wsAddr + ":" + wsPort;
		
		try {
			List<String> sharedFiles = LineReader.readFile(new File(SHAREDFILES_FILENAME));

			for (String file : sharedFiles) {
				try {
					discoveryService.put(wsFullAddr, file);
				} catch (XmlRpcException e) {
					// the file could not be published
				}
			}
		} catch (FileNotFoundException e) {
			// the shared files could not be read
		}
	}

	public byte[] getContent(String url) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(256);
			URL site = new URL(url);
			URLConnection connection = site.openConnection();

			for (String key : connection.getHeaderFields().keySet()) {
				if (key != null) {
					out.write((key + ": " + connection.getHeaderFields().get(key).get(0) + "\n").getBytes());
				}
			}
			
			out.write("\n".getBytes());
			
			InputStream in = connection.getInputStream();
			int nextByte = -1;
			while ((nextByte = in.read()) != -1) {
				out.write(nextByte);
			}
			
			return out.toByteArray();
		} catch (Exception e) {
			return e.toString().getBytes();
		}
	}

	public boolean createReplica(String url) {
		return false;
	}
}