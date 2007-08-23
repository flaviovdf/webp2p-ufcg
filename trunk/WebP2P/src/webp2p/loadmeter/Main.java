package webp2p.loadmeter;

import java.util.Timer;

public class Main {
	//TODO colocar os parametros configuráveis
	public static void main(String[] args) {
		LoadMeter loadMeter = new LoadMeter("server", 1000);
		
		Pinger pinger = new Pinger(loadMeter);
				
		Timer time = new Timer();
		
		time.schedule(pinger,0,2000);
	}
}
