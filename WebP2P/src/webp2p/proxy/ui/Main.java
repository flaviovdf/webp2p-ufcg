package webp2p.proxy.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Main implements AsyncCallback {

	public static void main(String[] args) throws MalformedURLException, XmlRpcException {
		XmlRpcClientConfigImpl pConfig = new XmlRpcClientConfigImpl();
		pConfig.setServerURL(new URL("http://192.168.1.3:9090"));
		
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(pConfig);
		AsyncCallback callback = new Main();
		client.executeAsync("test.isEven", new Object[] { 18, 0 }, callback);
		System.out.println("Doing other things...");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleError(XmlRpcRequest pRequest, Throwable pError) {
		System.out.println("[ERROR] "+pRequest + " : " + pError);
		
		try{
			throw new RuntimeException(pError);
		} catch(ArithmeticException e){
			System.out.println("BUMMMMMMMMMM");
		}
	}

	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		System.out.println("[OK] "+pRequest + " : " + pResult);
	}
}
