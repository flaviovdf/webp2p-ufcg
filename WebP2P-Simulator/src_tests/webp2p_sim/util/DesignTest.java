package webp2p_sim.util;

import java.io.File;
import java.util.Set;

import designwizard.design.entity.ClassNode;
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
		Set<ClassNode> allClasses = this.dw.getAllClasses();
		for (ClassNode classEntity: allClasses) {
			if (classEntity.getName().endsWith("Test")) {
				assertTrue("A classe "+ classEntity.getName() + " nao estende SmartTestCase",
						classEntity.getSuperClass().equals(this.dw.getClass(SmartTestCase.class)));
			}
		}
	}
	
	/*public void testAllEntitiesCommunicateThroughSendMessage() throws InexistentEntityException {
		
		MethodNode testedMethod = dw.getMethod("core.entity.SimpleQueuedEntity.sendMessage(core.entity.Message)");
		ClassNode simpleQueued = this.dw.getClass(SimpleQueuedEntity.class);
		
		Set<MethodNode> constructors = simpleQueued.getConstructors();
		
		Set<ClassNode> subClasses = simpleQueued.getSubClasses();
		for (ClassNode subClass : subClasses) {
			
			Set<MethodNode> methods = subClass.getAllMethods();
			for (MethodNode method: methods) {
			
				Set<MethodNode> called = method.getCalledMethods();
				for (MethodNode calledMethod: called) {
				
					if (calledMethod.getParentClass().equals(simpleQueued)) {
						assertTrue("The method "+ method.getName() + " calls " + calledMethod.getName(),
								calledMethod.equals(testedMethod) || constructors.contains(calledMethod));
					}
				}
			}
		}
	}*/
}
