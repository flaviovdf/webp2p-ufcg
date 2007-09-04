package webp2p.proxy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("url");
		
		if (url == null) {
			response.getWriter().println("Hi guest, you need to specify the 'url' parameter.");
			response.getWriter().println("For example: http://[site]/webp2p-proxy/?url=[url]");
			return;
		}
		
		byte[] content = Proxy.getInstance().getContent(url);
		
		if (content == null) {
			response.getWriter().println("The content '" + url + "' is not available in the Proxy.");
			return;
		}
		
		// TODO What about the status code??
		// reading the HTTP response header from the array of bytes
		BufferedReader byteReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content)));
		String line;
		
//System.out.println("Setting the HTTP response header...");
		while ((line = byteReader.readLine()) != null && !"".equals(line)) {
			StringTokenizer tks = new StringTokenizer(line, ":", true);
			String name = tks.nextToken().trim();
			tks.nextToken(); // skip delim
			String value = tks.nextToken("\n").trim();
//System.out.println("> "+name+" # "+value);
			response.addHeader(name, value);
		}
		
		// sending the content
		int nextByte;
//System.out.println("Setting the HTTP response content...");
		while ((nextByte = byteReader.read()) != -1) {
			response.getWriter().write(nextByte);
		}
	}

}
