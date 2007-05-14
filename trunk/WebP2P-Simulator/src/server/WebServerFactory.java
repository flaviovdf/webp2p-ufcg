package server;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.XPathAPI;

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
		Set<WebServer> servers = new HashSet<WebServer>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(topologyFile);
			
			Element root = document.getDocumentElement();
			NodeList serversList = root.getElementsByTagName("server");
			for (int i = 0; i < serversList.getLength(); i++) {
				Node serverNode = serversList.item(i);
				if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
					
					WebServer server = new WebServer(distribution, this.discoveryService);
					Element serverElement = (Element) serverNode;
					String url = serverElement.getElementsByTagName("url").item(0).getTextContent();
					
					NodeList filesList = XPathAPI.selectNodeList(serverElement, "file");
					for (int j = 0; j < filesList.getLength(); j++) {
						Node fileNode = filesList.item(j);
						if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
							
							Element fileElement = (Element) fileNode;
							String path = fileElement.getElementsByTagName("path").item(0).getTextContent();
							
							server.loadFile(url + path, 1);
						}
					}
					
					servers.add(server);
				}
			}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid xml file: " + topologyFile.getName(), e);
		}
		
		return servers;
	}

}
