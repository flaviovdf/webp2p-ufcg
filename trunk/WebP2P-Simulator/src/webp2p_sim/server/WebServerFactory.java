package webp2p_sim.server;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import webp2p_sim.core.network.Address;
import webp2p_sim.core.network.AsymetricBandwidth;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.util.InfinitTimeToLive;

import com.sun.org.apache.xpath.internal.XPathAPI;

import edu.uah.math.distributions.Distribution;

public class WebServerFactory {
	
	private final Host discoveryService;
	private final Network network;

	public WebServerFactory(Host discoveryService, Network network) {
		this.discoveryService = discoveryService;
		this.network = network;
	}

	public Set<WebServer> createServers(File topologyFile, boolean bind) {
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
					long upBand = Long.parseLong(serverElement.getElementsByTagName("upband").item(0).getTextContent()) * 1024;
					long downBand = Long.parseLong(serverElement.getElementsByTagName("downband").item(0).getTextContent()) * 1024;
					Host createHost = createHost(url, upBand, downBand);
					
					Distribution distribution = this.loadServerDistribution((Element) serverElement.getElementsByTagName("distribution").item(0));
					WebServer server = new WebServer(createHost, distribution, network, this.discoveryService, bind);
					
					NodeList filesList = XPathAPI.selectNodeList(serverElement, "file");
					for (int j = 0; j < filesList.getLength(); j++) {
						Node fileNode = filesList.item(j);
						if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
							
							Element fileElement = (Element) fileNode;
							String path = fileElement.getElementsByTagName("path").item(0).getTextContent();
							int size = Integer.parseInt(fileElement.getElementsByTagName("size").item(0).getTextContent());
							
							//Converting from KB to bits
							server.loadFile(url + path, size * 1024 * 8, new InfinitTimeToLive());
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
					server1.addAdj( server2.getHost() );
					server2.addAdj( server1.getHost() );
				}
			}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid xml file: " + topologyFile.getName(), e);
		}
		
		return new HashSet<WebServer>(servers.values());
	}

	@SuppressWarnings("unchecked")
	private Distribution loadServerDistribution(Element distributionElement) {
		String distributionName = distributionElement.getElementsByTagName("name").item(0).getTextContent();
		Object[] parameters = this.getParameters(distributionElement);
		try {
			Class distributionClass = Class.forName(distributionName);
			Constructor constructor = distributionClass.getConstructor(this.getTypes(parameters.length));
			return (Distribution) constructor.newInstance(parameters);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
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
		} catch ( NoSuchMethodException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Double[] getParameters(Element distributionElement) {
		NodeList paramNodes = distributionElement.getElementsByTagName("param");
		
		Double[] parameters = new Double[paramNodes.getLength()];
		
		for (int i = 0; i < paramNodes.getLength(); i++) {
			Node n = paramNodes.item(i);
			parameters[i] = new Double(n.getTextContent());
		}
		
		return parameters;
	}

	private Class[] getTypes(int numberOfParameters) {
		Class[] types = new Class[numberOfParameters];
		for (int i = 0; i < types.length; i++) {
			types[i] = double.class;
		}
		return types;
	}
	
	private Host createHost(String ip, long upBand, long downBand) {
		String[] split = ip.split("\\.");
		if (split.length != 4) {
			throw new RuntimeException("IP: " + ip + " is invalid");
		}
		
		int[] bytes = new int[4];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = Integer.parseInt(split[i]);
			
			if (bytes[i] <= 0 || bytes[i] >= 255) {
				throw new RuntimeException("IP: " + ip + " is invalid");	
			}
		}
		
		return new Host(new Address(bytes[0], bytes[1], bytes[2], bytes[3]), new AsymetricBandwidth(upBand, downBand));
	}
}
