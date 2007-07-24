package webp2p.proxy.ui;

import java.util.Arrays;

import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;

public class Main {

	public static void main(String[] args) throws XmlRpcException {
		DiscoveryServiceStub dsStub = new DiscoveryServiceStub("http://localhost", 1234);
		dsStub.put("http://1.1.1.1:999", "http://1.1.1.1:999/file1.in");
		dsStub.put("http://1.1.1.2:999", "http://1.1.1.1:999/file1.in");
		System.out.println(Arrays.toString(dsStub.get("http://1.1.1.1:999/file1.in")));
	}

}
