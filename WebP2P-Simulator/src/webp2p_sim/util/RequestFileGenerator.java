package webp2p_sim.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.ParetoDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
 * This class generates input files with requests according to a given distribution.
 * 
 * time file fileSize(K)
 */
public class RequestFileGenerator {

	private Distribution dist;
	private int numberOfTicks;
	
	public RequestFileGenerator(Distribution dist, int numberOfTicks) {
		this.dist = dist;
		this.numberOfTicks = numberOfTicks;
	}
	
	
	public void setDistribution(Distribution dist) {
		this.dist = dist;
	}
	
	
	public boolean generateRequestFile(String pathOut) {
		StringBuffer contents = new StringBuffer();
		
		RandomVariable randomVar = new RandomVariable(this.dist);
		for (int i = 0 ; i < numberOfTicks ; i++) {		
			contents.append(i+" "+Math.round(randomVar.simulate())+"\n");
		}
		try {
			this.writeResult(new File(pathOut), contents.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public void writeResult(File file, String contents) throws IOException {
		Writer output = null;
	    try {
	      output = new BufferedWriter( new FileWriter(file) );
	      output.write( contents );
	    } 
	    finally {
	      if (output != null) output.close();
	    }
	}
	
	public static void main(String[] args) {
		RequestFileGenerator generator = new RequestFileGenerator(new ParetoDistribution(),10);
		generator.generateRequestFile("tests"+File.separator+"requests"+File.separator+"url1.txt");
	}
}
