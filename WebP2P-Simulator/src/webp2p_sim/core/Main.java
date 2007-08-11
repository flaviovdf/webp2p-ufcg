package webp2p_sim.core;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		if (args == null || args.length < 1) {
			usage(null);
		}
		
		Simulator simulator;
		if (args[0].equals("distributed")) {
			try {
//				FIXME AQUI JOÃO;
//				simulator = new SimulatorDistributed(new File(args[1]), new File(args[2]), Integer.parseInt(args[3]));
			} catch (Throwable e) {
				usage(e);
				return;
			}
		} else if (args[0].equals("centralized")) {
			try {
//				FIXME AQUI JOÃO;
//				simulator = new SimulatorCentralized(new File(args[1]), Integer.parseInt(args[2]));
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
		
		try {
//			FIXME AQUI JOÃO;
//			simulator.simulate();
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
				"\t[distributed] receives:  <browser input file> <topology file> <max ticks> \n" +
				"\t[centralized] receives:  <browser input file> <max ticks>");
		
		if (t != null) {
			System.err.println("\nExtra Info: ");
			t.printStackTrace();
		}
		
		System.exit(1);
	}
	
}
