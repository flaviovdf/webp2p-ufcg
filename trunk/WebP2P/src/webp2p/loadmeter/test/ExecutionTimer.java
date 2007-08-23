package webp2p.loadmeter.test;

import java.io.*;
import java.net.*;

public class ExecutionTimer {
	private long start;
	private long end;

	public ExecutionTimer() {
		reset();
	}

	public void start() {
		System.gc();
		start = System.currentTimeMillis();
	}

	public void end() {
		System.gc();
		end = System.currentTimeMillis();
	}

	public long duration(){
		return (end-start);
	}

	public void reset() {
		start = 0;  end   = 0;
	}

	private void ping(String server, int maxTimetoWait) {
		try {
			InetAddress address = InetAddress.getByName(server);
			System.out.println("Name: " + address.getHostName());
			System.out.println("Addr: " + address.getHostAddress());
			System.out.println("Reach: " + address.isReachable(maxTimetoWait));
		}
		catch (UnknownHostException e) {
			System.err.println("Unable to lookup "+server);
		}
		catch (IOException e) {
			System.err.println("Unable to reach"+server);
		}
	}

	public static void main(String s[]) {
		// simple example
		ExecutionTimer t = new ExecutionTimer();
		
		t.start();
		t.ping("www.google.com.br",10000);
		t.end();
		
		System.out.println("\n" + t.duration() + " ms");
	}
}