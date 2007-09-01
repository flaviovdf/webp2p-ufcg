package webp2p_sim.util;

import java.io.File;
import java.util.Set;

import designwizard.design.ClassNode;
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
	
	public void testAllEntitiesCommunicateNetwork() throws Exception {
//
//		ClassNode simpleQueued = this.dw.getClass(SimpleQueuedEntity.class);
//		
//		Set<MethodNode> constructors = simpleQueued.getConstructors();
//		Set<ClassNode> subClasses = simpleQueued.getSubClasses();
//		
//		MethodNode uniqueCase = this.dw.getMethod(SimpleQueuedEntity.class.getName() + ".tickOcurred()");
//		
//		for (ClassNode subClass : subClasses) {
//			
//			Set<MethodNode> methods = subClass.getAllMethods();
//			for (MethodNode method: methods) {
//				
//				Set<MethodNode> called = method.getCalledMethods();
//				for (MethodNode calledMethod: called) {
//					
//					Class<?>[] interfaces = calledMethod.getParentClass().getClass().getInterfaces();
//					boolean isNetworkEntity = Arrays.asList(interfaces).contains(NetworkEntity.class);
//					
//					//Can only call methods from a network entity if it is a super classes or a constructors.
//					if (isNetworkEntity) {
//						assertTrue(subClass.getName() + " is calling " + calledMethod, constructors.contains(calledMethod) || calledMethod.equals(uniqueCase));
//					}
//				}
//			}
//		}
	}
}
