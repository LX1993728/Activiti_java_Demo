package activiti.lx.spring;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringActivitiTest {
	@Test
	public void test1() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"activiti/lx/spring/applicationContext.xml");
		ProcessEngine processEngine = (ProcessEngine) ctx.getBean("processEngine");
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
				.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/processdefination/MyProcess.bpmn");
		deploymentBuilder.addClasspathResource("activiti/lx/processdefination/MyProcess.png");
		deploymentBuilder.deploy();
	}

}
