package util;

import java.io.File;
import java.util.Set;

import core.entity.SimpleQueuedEntity;

import designwizard.design.entity.ui.Class;
import designwizard.design.entity.ui.Method;
import designwizard.exception.InexistentEntityException;
import designwizard.main.DesignWizard;

public class DesignTest extends SmartTestCase {

	protected static final String PROJECT_JAR = "." + File.separator + "out" + File.separator + "webp2psim.jar";
	private DesignWizard dw;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.dw = new DesignWizard(PROJECT_JAR);
	}
	
	public void testAllTestsExtendSmartTestCase() throws Exception {
		Set<Class> allClasses = this.dw.getAllClasses();
		for (Class classEntity: allClasses) {
			if (classEntity.getName().endsWith("Test")) {
				assertTrue("A classe "+ classEntity.getName() + " nao estende SmartTestCase",classEntity.getSuperClass().equals(SmartTestCase.class.getName()));
			}
		}
	}
	
	public void testAllEntitiesCommunicateThroughSendMessage() throws InexistentEntityException {
		//TODO Incluir o nivel de metodo no dw.
		
		/*Class simpleQueued = this.dw.getClass(SimpleQueuedEntity.class);
		Set<String> subClasses = simpleQueued.getSubClasses();
		for (String subClassName : subClasses) {
			Class subClass = this.dw.getClass(subClassName);
			Set<String> calledMethods = subClass.getClassesUsedBy();
			for (String called : calledMethods) {
				Class calledClass = dw.getClass(called);
				if (calledClass.getName().equals(SimpleQueuedEntity.class.getName())) {
					
				}
			}
		}*/
		
	}
	
}
