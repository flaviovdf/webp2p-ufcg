package webp2p_sim.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;

import webp2p_sim.core.network.Address;
import webp2p_sim.core.network.AsymetricBandwidth;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;

public class Params {

	protected long simTime;
	
	private Network network = new Network(); 
	
	@SuppressWarnings("unchecked")
	protected <T> T extractObject(Configuration config, String name) {
		ArrayList<String> defitinion = new ArrayList<String>();
		String className = config.getString(name);
		defitinion.add(className);
		
		for (int i = 1; i <= Integer.MAX_VALUE; i++) {
		 	String value = config.getString(name + ".param" + i);
		 	
		 	if (value == null) {
		 		break;
		 	}
		 	
			defitinion.add(value);
		}
		
		String[] definitionArray = defitinion.toArray(new String[0]);
		
		if (definitionArray.length == 0) {
			throw new RuntimeException("Cannot create object from empty array");
		}
		
		try {
			Class[] paramTypes = new Class[definitionArray.length - 1];
			Object[] paramsValues = new Object[definitionArray.length - 1];
			
			for (int i = 1; i < definitionArray.length; i++) {
				String paramValue = definitionArray[i];
				Class<?> paramClass = double.class;
				paramTypes[i - 1] = paramClass;
				Object objectParam = new Double(paramValue).doubleValue();
				paramsValues[i - 1] = objectParam;
			}

			Class<T> clazz = (Class<T>) Class.forName(definitionArray[0]);
			Constructor<T> constructor = clazz.getConstructor(paramTypes);
			return constructor.newInstance(paramsValues);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	protected Host createHost(String ip, long upBand, long downBand) {
		String[] split = ip.split("\\.");
		if (split.length != 4) {
			throw new RuntimeException("IP: " + ip + " is invalid");
		}
		
		int[] bytes = new int[4];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = Integer.parseInt(split[i]);
			
			if (bytes[i] <= 0 || bytes[i] >= 255) {
				throw new RuntimeException("IP: " + ip + " is invalid");	
			}
		}
		
		return new Host(new Address(bytes[0], bytes[1], bytes[2], bytes[3]), new AsymetricBandwidth(upBand, downBand));
	}
	
	public long getSimTime() {
		return simTime;
	}
	
	public Network getNetwork() {
		return network;
	}
}
