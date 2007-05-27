package util;

import junit.framework.TestCase;
import core.Clock;

public abstract class SmartTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Clock.reset();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Clock.reset();
	}
}
