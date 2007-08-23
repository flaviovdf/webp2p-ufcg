package webp2p.loadmeter.test;

import java.io.*;
import java.net.*;

public class ReachableTest {
 
	public static void main(String args[]) {
     try {
       InetAddress address = InetAddress.getByName("google.com");
       System.out.println("Name: " + address.getHostName());
       System.out.println("Addr: " + address.getHostAddress());
       System.out.println("Reach: " + address.isReachable(3000));
     }
     catch (UnknownHostException e) {
       System.err.println("Unable to lookup web.mit.edu");
     }
     catch (IOException e) {
       System.err.println("Unable to reach web.mit.edu");
     }
   }
	
	 /*public static void main1(String[] args) { 
	        try { 
	            URL url = new URL("http://www.lsd.ufcg.edu.br"); 
	            BufferedReader in = new BufferedReader( 
	                new InputStreamReader(url.openStream())); 
	            String inputLine; 
	    
	            while ((inputLine = in.readLine()) != null) { 
	                // Process each line.
	                System.out.println(inputLine); 
	            } 
	            in.close(); 
	    
	        } catch (MalformedURLException me) { 
	            System.out.println(me); 
	    
	        } catch (IOException ioe) { 
	            System.out.println(ioe); 
	        } 
	    }//end main 
*/}