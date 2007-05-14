package proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import core.entity.TimedEntity;

public class RequestGenerator implements TimedEntity {

	private Proxy proxy;
	private Map<Integer, Set<String>> requests;
	private int ticks;

	public RequestGenerator(Proxy proxy) {
		this.proxy = proxy;
		this.requests = new HashMap<Integer, Set<String>>();
		this.ticks = 0;
	}
	
	public boolean loadFile(File requestsFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(requestsFile));
			String line;
			
			while ((line = reader.readLine()) != null) {
				StringTokenizer tks = new StringTokenizer(line);
				int time = Integer.parseInt(tks.nextToken());
				String url = tks.nextToken();
				
				if (!this.requests.containsKey(time)) {
					this.requests.put(time, new HashSet<String>());
				}
				
				Set<String> req = this.requests.get(time);
				req.add(url);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void tickOcurred() {
		this.ticks++;
		
		if (this.requests.containsKey(this.ticks)) {
			Set<String> req = this.requests.get(this.ticks);
			
			for (String url : req) {
				this.proxy.makeRequest(url);
			}
		}
		
	}

}
