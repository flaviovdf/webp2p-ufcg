package webp2p_sim.core;

import java.util.Arrays;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		if (args == null || args.length < 1) {
			usage(null);
		}
		
		try {
			Configuration config = new PropertiesConfiguration(args[1]);
			
			Simulator simulator;
			if (args[0].equals("distributed")) {
				try {
					simulator = new SimulatorDistributed(new DistributedParams(config));
				} catch (Throwable e) {
					usage(e);
					return;
				}
			} else if (args[0].equals("centralized")) {
				try {
					simulator = new SimulatorCentralized(new CentralizedParams(config));
				} catch (Throwable e) {
					usage(e);
					return;
				}
			} else {
				usage(null);
				return;
			}
			
			String startMessage = "Simulation is about to start, arguments received: " + Arrays.toString(args);
			LOG.info(startMessage);
			System.out.println(startMessage);
		
			simulator.simulate();
			
			System.out.println(simulator.printResults());
			
			String endMessage = "Simulation finished";
			System.out.println(endMessage);
			LOG.info(endMessage);
		} catch (Throwable e) {
			String errorMessage = "Simulation halted abnormally";
			LOG.error(errorMessage, e);
			System.err.println(errorMessage);
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void usage(Throwable t) {
		System.err.println("usage java " + Main.class.getName() + " [distributed | centralized]. where: \n" +
				"\t[distributed] receives:  <config> \n" +
				"\t[centralized] receives:  <config>");
		
		if (t != null) {
			System.err.println("\nExtra Info: ");
			t.printStackTrace();
		}
		
		System.exit(1);
	}
	
}
