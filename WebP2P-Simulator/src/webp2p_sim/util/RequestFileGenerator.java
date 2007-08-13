package webp2p_sim.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.server.WebServer;
import webp2p_sim.server.WebServerFactory;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.ParetoDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
 * This class generates input files with requests according to a given distribution.
 * 
 * time file fileSize(K)
 */
public class RequestFileGenerator {

	private Distribution dist;
	private long numberOfTicks;
	private Set<WebServer> webServers;
	
	public RequestFileGenerator(Distribution dist, long numberOfTicks, Set<WebServer> webServers) {
		this.setDistribution(dist);
		this.numberOfTicks = numberOfTicks;
		this.webServers = webServers;
	}
	
	
	public void setDistribution(Distribution dist) {
		this.dist = dist;
	}
	
	public Map<Long,List<UrlToWebServer>> generateRequests() {
		Map<Long,List<UrlToWebServer>> returnValue = new HashMap<Long,List<UrlToWebServer>>();
		
		RandomVariable randomVar = new RandomVariable(this.dist);
		
		for (WebServer server : this.webServers) {
			for (long tick = 1 ; tick <= numberOfTicks ; tick++) {
				long result = Math.round(randomVar.simulate());
				List<UrlToWebServer> list = selectFileByPopularity(result,server);
				returnValue.put(tick, list);
			}
		}
		
		return returnValue;
	}
	
	public boolean generateRequestFile(String pathOut) {
		Map<Long, List<UrlToWebServer>> map = generateRequests();
		
		StringBuffer st = new StringBuffer();
		for (Entry<Long, List<UrlToWebServer>> entry : map.entrySet()) {
			for (UrlToWebServer urlTo : entry.getValue()) {
				st.append(entry.getKey()+" "+urlTo.getUrl());
			}
		}
		try {
			this.writeResult(new File(pathOut), st.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		
	}
	
	private List<UrlToWebServer> selectFileByPopularity(long result, WebServer server) {
		// essa dist tem que ser zipf 
		RandomVariable randomVar = new RandomVariable(this.dist);
		
		double total = 0;
		Pair[] values = new Pair[server.getFiles().size()];
		
		int i = 0;
		for (String url : server.getFiles()) {
			double simulated = randomVar.simulate();
			total += simulated;
			values[i++] = new Pair(simulated,url);
		}
		
		List<UrlToWebServer> resultValue = new ArrayList<UrlToWebServer>();
		long times = 0;
		for (Pair pair: values) {
		
			times = Math.round(pair.getParetoValue()*result/total);
			for (int k = 0; k<times; k++) {
				
				resultValue.add(new UrlToWebServer(server,pair.getFileName()));
			}
		}
		
		return resultValue;
	}


	public void writeResult(File file, String contents) throws IOException {
		Writer output = null;
	    try {
	      output = new BufferedWriter( new FileWriter(file) );
	      output.write( contents );
	    } 
	    finally {
	      if (output != null) output.close();
	    }
	}
	
	
	private class Pair {
		private double paretoValue;
		private String fileName;
		
		public Pair(double times, String fileName) {
			this.paretoValue = times;
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

		public double getParetoValue() {
			return paretoValue;
		}
	}
	
	public static void main(String[] args) {
		WebServerFactory factory = new WebServerFactory(new DiscoveryService("ds", new ParetoDistribution()));
		File topologyFile = new File("topology.xml");
		
		Set<WebServer> servers = factory.createServers(topologyFile);
				
		RequestFileGenerator generator = new RequestFileGenerator(new ParetoDistribution(),10, servers);
		generator.generateRequestFile("tests"+File.separator+"requests"+File.separator+"paretoInput.txt");
	}
}
