package webp2p.loadmeter.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class ExecutionTimer {
	private double start;
	private double end;

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

	public double duration(){
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
		t.download("http://www.dsc.ufcg.edu.br/~jarthur/upload/Apresentacao.ppt");
		t.end();
		
		//miliseconds
		double x = t.duration()/1000;
		System.out.println(10/x);
	}

	public void download(String file) {
		URLConnection conn = null;
		InputStream  in = null;
		try {
			URL url = new URL(file);
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024*10];
			in.read(buffer);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {
			}
		}
	}
}