package webp2p.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import junit.framework.TestCase;

public class LineReaderTest extends TestCase {

	public void testReadInexistentFile() {
		try {
			System.out.println(LineReader.readFile(new File("resources" + File.separator + "inexistent.file")));
			fail("An FileNotFoundException should be thrown!");
		} catch (FileNotFoundException e) {
			// nothing
		}
	}
	
	public void testReadEmptyFile() throws FileNotFoundException {
		List<String> lines = LineReader.readFile(new File("resources" + File.separator + "empty.file"));
		assertEquals(0, lines.size());
	}

	public void testReadRandomFile() throws FileNotFoundException {
		List<String> lines = LineReader.readFile(new File("resources" + File.separator + "random.text"));
		assertEquals(5, lines.size());
		assertEquals("one two", lines.get(0));
		assertEquals("", lines.get(1));
		assertEquals("three ", lines.get(2));
		assertEquals("! four, five... six?", lines.get(3));
		assertEquals("#end .", lines.get(4));
	}

}
