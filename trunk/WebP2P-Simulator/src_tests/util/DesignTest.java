package util;

import java.io.File;

import designwizard.main.DesignWizard;

public class DesignTest extends SmartTestCase {

	protected static String PROJECT_JAR = "." + File.separator + "out" + File.separator + "webp2psim.jar";
	private DesignWizard dw;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.dw = new DesignWizard(PROJECT_JAR);
	}
	
	public void testAllTestsExtendTestCase() {
		//FIXME
	}
	
	public void testAllEntitiesCommunicateThroughSendMessage() {
		//FIXME		
	}
	
}
