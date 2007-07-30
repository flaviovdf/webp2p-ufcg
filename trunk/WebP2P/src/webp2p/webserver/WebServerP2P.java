package webp2p.webserver;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.webserver.config.WebServerP2PConfig;

public class WebServerP2P {
	
	static void init() {
		DiscoveryServiceStub discoveryService;
		
		String dsAddr = WebServerP2PConfig.getInstance().getDiscoveryServiceAddress();
		int dsPort = WebServerP2PConfig.getInstance().getDiscoveryServicePort();
		discoveryService = new DiscoveryServiceStub(dsAddr, dsPort);
		
		// test
		try {
System.out.println("WebServerP2P.WebServerP2P() -- 1");
			discoveryService.put("http://localhost:9090", "http://acm.uva.es/p/v109/10986.html");
			discoveryService.put("http://localhost:9090", "http://upload.wikimedia.org/wikipedia/en/2/24/Lenna.png");
System.out.println("WebServerP2P.WebServerP2P() -- 2");
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WebServerP2P() {
	}
	
	public byte[] getContent(String url) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(256);
			URL site = new URL(url);
			URLConnection connection = site.openConnection();
System.out.println(connection.getHeaderFields());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ("file content: " + url).getBytes();
	}

}