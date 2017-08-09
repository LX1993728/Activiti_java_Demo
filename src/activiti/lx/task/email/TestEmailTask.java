package activiti.lx.task.email;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 测试email task
 * @author liuxun
 *
 */
public class TestEmailTask {
// 初始化测试环境
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/task/email/emailTask.bpmn")
				.addClasspathResource("activiti/lx/task/email/emailTask.png").name("部署测试邮件任务");
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署ID:" + deploy.getId() + "\t部署名称" + deploy.getName());
	}

	// 启动流程实例
	@Test
	public void startProcessInst() {
		String processDefinitionKey = "emailProcess";
		ProcessInstance processInstance = ProcessEngines.getDefaultProcessEngine().getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID:" + processInstance.getId());
	}
}
