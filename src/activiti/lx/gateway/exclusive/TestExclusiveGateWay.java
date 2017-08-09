package activiti.lx.gateway.exclusive;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
/**
 * 测试排他网关
 * @author liuxun
 *
 */
public class TestExclusiveGateWay {
	// 初始化测试环境
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/gateway/exclusive/gateWay1.bpmn")
				.addClasspathResource("activiti/lx/gateway/exclusive/gateWay1.png").name("部署测试排他网关");
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署ID:" + deploy.getId() + "\t部署名称" + deploy.getName());
	}

	// 启动流程实例
	@Test
	public void startProcessInst() {
		String processDefinitionKey = "gateWay1";
		ProcessInstance processInstance = ProcessEngines.getDefaultProcessEngine().getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID:" + processInstance.getId());
	}

	// 办理任务(查询第二个节点张三的任务并进行办理)
	@Test
	public void completeSecond() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Task task = processEngine.getTaskService().createTaskQuery().taskAssignee("张三").singleResult();
		System.out.println(task.getId());
		if (task != null) {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("money", 800);
			processEngine.getTaskService().complete(task.getId(), variables );
		}
	}
}
