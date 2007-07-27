package webp2p.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.StringTokenizer;

import webp2p.discoveryservice.DiscoveryServiceStub;

public class ProxySkel {
	
	private Proxy proxy;

	public ProxySkel(String dsAddr, int dsPort) {
		this.proxy = new Proxy(new DiscoveryServiceStub(dsAddr, dsPort), new ChooseFirstWebServerArbitrator());
	}

	public void start(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		
		while(true) {
			Socket socket = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String getStatement = br.readLine();

			StringTokenizer tks = new StringTokenizer(getStatement);
			tks.nextToken(); // skip 'GET' token
			String filename = tks.nextToken();
			
			byte[] content = this.proxy.getContent("http://" + filename);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.write("HTTP/1.0 200 Document follows\n");
			writer.write("MIME-Version: 1.0\n");
			writer.write("Server: CERN/3.0\n");
			writer.write("Date: " + Calendar.getInstance() + "\n");
			writer.write("Content-Type: text/html\n");
			writer.write("Content-Length: 0\n");
			writer.write("Last-Modified: Wednesday, 16-Oct-96 10:14:01 GMT\n");
			writer.write("\n");
			writer.write(content == null ? "Empty result for: " + filename : new String(content));
			writer.write("\n");
			writer.close();
			socket.close();
		}
	}

}
