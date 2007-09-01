package webp2p_sim.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;

public class Params {

	protected long simTime;
	protected int seed;
	
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
	
	public long getSimTime() {
		return simTime;
	}

	public int getSeed() {
		return seed;
	}
}
