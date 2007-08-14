package webp2p.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LineReader {

	/**
	 * Reads the entire file and returns its lines in a <code>List</code>.
	 * 
	 * @param f The file to be read.
	 * @return The file lines or <code>null</code> if the lines cannot be read.
	 * @throws FileNotFoundException if the file cannot be found.
	 */
	public static List<String> readFile(File f) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		try {
			
			List<String> result = new LinkedList<String>();
			String line = null;
			
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			
			return result;
		} catch (IOException e) {
			return null;
		} finally {
			if (br != null) try { br.close(); } catch (IOException e) {}
		}
	}

}
