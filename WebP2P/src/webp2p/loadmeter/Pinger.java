package webp2p.loadmeter;

import java.util.TimerTask;

public class Pinger extends TimerTask {
	
	private LoadMeter loadMeter;
	
	public Pinger(LoadMeter loadMeter) {
		this.loadMeter = loadMeter;
	}

	@Override
	public void run() {
		System.out.println("huhi!");
//		this.loadMeter.ping();
	}

}
