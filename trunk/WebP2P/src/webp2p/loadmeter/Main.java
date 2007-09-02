package webp2p.loadmeter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;

public class Main {

	public static void main(String[] args) {
		// default values
		int bufferSize = 1024;
		int pingInterval = 30000;
		
		try {
			Properties props = new Properties();
			props.load(new BufferedInputStream(new FileInputStream("loadmeter.properties")));
			bufferSize = Integer.parseInt(props.getProperty("buffer"));
			pingInterval = Integer.parseInt(props.getProperty("pingInterval"));
		} catch (IOException e) {
			System.out.println("loadmeter.properties could not be readed. Using the default value 1024 for buffer's size and 30 seconds for ping interval.");
		}
		
		LoadMeter loadMeter = new LoadMeter("webserver",bufferSize);
		Pinger pinger = new Pinger(loadMeter);
		Timer time = new Timer();
		time.schedule(pinger,0,pingInterval);
	}
}
