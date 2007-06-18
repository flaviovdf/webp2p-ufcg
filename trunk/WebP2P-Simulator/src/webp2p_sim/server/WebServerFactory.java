package webp2p_sim.server;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.util.InfinitTimeToLive;

import com.sun.org.apache.xpath.internal.XPathAPI;

import edu.uah.math.distributions.Distribution;

public class WebServerFactory {
	
	private DiscoveryService discoveryService;

	public WebServerFactory(DiscoveryService discoveryService) {
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
					Distribution distribution = this.loadServerDistribution((Element) serverElement.getElementsByTagName("distribution").item(0));
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

	private Distribution loadServerDistribution(Element distributionElement) {
		String distributionName = distributionElement.getElementsByTagName("name").item(0).getTextContent();
		List<Double> parameters = this.getParameters(distributionElement);
		try {
			Class distributionClass = Class.forName(distributionName);
			Constructor constructor = distributionClass.getConstructor(this.getTypes(parameters.size()));
			return (Distribution) constructor.newInstance(parameters);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private List<Double> getParameters(Element distributionElement) {
		List<Double> parameters = new LinkedList<Double>();
		
		NodeList paramNodes = distributionElement.getElementsByTagName("param");
		
		for (int i = 0; i < paramNodes.getLength(); i++) {
			Node n = paramNodes.item(i);
			parameters.add(new Double(n.getTextContent()));
		}
		
		return parameters;
	}

	private Class[] getTypes(int numberOfParameters) {
		Class[] types = new Class[numberOfParameters];
		for (int i = 0; i < types.length; i++) {
			types[i] = Double.class;
		}
		return types;
	}
}
