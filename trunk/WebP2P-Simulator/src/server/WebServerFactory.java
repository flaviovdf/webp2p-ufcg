package server;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.XPathAPI;
import common.InfinitTimeToLive;

import ds.DiscoveryService;
import edu.uah.math.distributions.ExponentialDistribution;

public class WebServerFactory {
	
	private DiscoveryService discoveryService;
	private final ExponentialDistribution distribution;

	public WebServerFactory(ExponentialDistribution distribution, DiscoveryService discoveryService) {
		this.distribution = distribution;
		this.discoveryService = discoveryService;
	}

	public Set<WebServer> createServers(File topologyFile) {
		Map<String, WebServer> servers = new HashMap<String, WebServer>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(topologyFile);
			
			Element root = document.getDocumentElement();
			NodeList serversList = root.getElementsByTagName("server");
			for (int i = 0; i < serversList.getLength(); i++) {
				Node serverNode = serversList.item(i);
				if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element serverElement = (Element) serverNode;
					String url = serverElement.getElementsByTagName("url").item(0).getTextContent();
					WebServer server = new WebServer(url, distribution, this.discoveryService);
					
					NodeList filesList = XPathAPI.selectNodeList(serverElement, "file");
					for (int j = 0; j < filesList.getLength(); j++) {
						Node fileNode = filesList.item(j);
						if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
							
							Element fileElement = (Element) fileNode;
							String path = fileElement.getElementsByTagName("path").item(0).getTextContent();
							
							server.loadFile(url + path, 1, new InfinitTimeToLive());
						}
					}
					
					servers.put(url, server);
				}
			}
			
			NodeList linksList = root.getElementsByTagName("link");
			for (int i = 0; i < linksList.getLength(); i++) {
				Node linkNode = linksList.item(i);
				if (linkNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element linkElement = (Element) linkNode;
					String server1url = linkElement.getElementsByTagName("server1").item(0).getTextContent();
					String server2url = linkElement.getElementsByTagName("server2").item(0).getTextContent();
					
					WebServer server1 = servers.get( server1url );
					WebServer server2 = servers.get( server2url );
					server1.addAdj( server2 );
					server2.addAdj( server1 );
				}
			}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid xml file: " + topologyFile.getName(), e);
		}
		
		return new HashSet<WebServer>(servers.values());
	}

}
