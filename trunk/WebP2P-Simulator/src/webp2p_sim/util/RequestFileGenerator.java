package webp2p_sim.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

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
	private int numberOfTicks;
	private WebServerFactory serverFactory;
	private Set<WebServer> webServers;
	
	public RequestFileGenerator(Distribution dist, int numberOfTicks, File topologyFile) {
		this.dist = dist;
		this.numberOfTicks = numberOfTicks;
		this.serverFactory = new WebServerFactory(dist,new DiscoveryService("ds",dist));
		this.webServers = this.serverFactory.createServers(topologyFile);
	}
	
	
	public void setDistribution(Distribution dist) {
		this.dist = dist;
	}
	
	
	public boolean generateRequestFile(String pathOut) {
		StringBuffer contents = new StringBuffer();
		
		RandomVariable randomVar = new RandomVariable(this.dist);
		
		for (WebServer server : this.webServers) {
			for (int i = 1 ; i <= numberOfTicks ; i++) {
				long result = Math.round(randomVar.simulate());
				contents.append(selectFileByPopularity(i,result,server));
			}
		}
		try {
			this.writeResult(new File(pathOut), contents.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	private String selectFileByPopularity(long tick, long result, WebServer server) {
		// essa dist tem que ser zipf
		RandomVariable randomVar = new RandomVariable(this.dist);
		
		long total = 0;
		Pair[] values = new Pair[server.getFiles().size()];
		long simulated;
		
		int i = 0;
		for (String url : server.getFiles()) {
			simulated = Math.round(randomVar.simulate());
			total += simulated;
			values[i++] = new Pair(simulated,url);
		}
		
		StringBuffer resultValue = new StringBuffer();
		long times = 0;
		for (Pair pair: values) {
			//TODO com os arredondamentos, nem todos as requisições são distribuídas para os arquivos.
			times = Math.round(pair.getParetoValue()*result/total);
			resultValue.append(tick+" "+times+" "+pair.getFileName()+"\n");
		}
		return resultValue.toString();
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
		private long paretoValue;
		private String fileName;
		
		public Pair(long times, String fileName) {
			this.paretoValue = times;
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

		public long getParetoValue() {
			return paretoValue;
		}
	}
	
	public static void main(String[] args) {
		File topologyFile = new File("topology.xml");
		
		RequestFileGenerator generator = new RequestFileGenerator(new ParetoDistribution(),10, topologyFile);
		generator.generateRequestFile("tests"+File.separator+"requests"+File.separator+"paretoInput.txt");
	}
}
