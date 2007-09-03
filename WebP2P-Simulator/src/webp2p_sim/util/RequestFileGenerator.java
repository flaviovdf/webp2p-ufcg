package webp2p_sim.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import webp2p_sim.server.WebServer;

/**
 * This class generates input files with requests according to a given distribution.
 * 
 * time file fileSize(K)
 */
public class RequestFileGenerator {

	private final long numberOfTicks;
	private final Set<WebServer> webServers;
	private final double meanPerTick;
	
	public RequestFileGenerator(double meanPerTick, long numberOfTicks, Set<WebServer> webServers) {
		this.meanPerTick = meanPerTick;
		this.numberOfTicks = numberOfTicks;
		this.webServers = webServers;
	}
	
	public Map<Long,List<UrlToWebServer>> generateRequests() {
		Map<Long,List<UrlToWebServer>> returnValue = new HashMap<Long,List<UrlToWebServer>>();
		
		for (WebServer server : this.webServers) {
			for (long tick = 0; tick < numberOfTicks ; tick++) {
				List<UrlToWebServer> list = selectFileByPopularity(server);
				
				List<UrlToWebServer> reqs = returnValue.get(tick);
				if (reqs == null) {
					reqs = new ArrayList<UrlToWebServer>();
					returnValue.put(tick, reqs);
				}
				
				reqs.addAll(list);
			}
		}
		
		return returnValue;
	}
	
	public void generateRequestFile(String pathOut) {
		Map<Long, List<UrlToWebServer>> map = generateRequests();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(pathOut)));
			
			for (Entry<Long, List<UrlToWebServer>> entry : map.entrySet()) {
				for (UrlToWebServer urlTo : entry.getValue()) {
					writer.println(entry.getKey() + "\t" + urlTo.getUrl());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) writer.close();
		}
	}
	
	private List<UrlToWebServer> selectFileByPopularity(WebServer server) {
		List<UrlToWebServer> resultValue = new ArrayList<UrlToWebServer>();
		
		String[] files = new ArrayList<String>(server.getFiles()).toArray(new String[server.getFiles().size()]);
		
		for (int i = 0; i < this.meanPerTick; i++) {
			resultValue.add(new UrlToWebServer(server, files[i % files.length]));
		}
		
		return resultValue;
	}
}
