package activiti.lx.gateway.parallel;

import java.util.HashMap;
import java.util.List;
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
public class TestParallelGateWay {
	// 初始化测试环境
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/gateway/parallel/gateWay2.bpmn")
				.addClasspathResource("activiti/lx/gateway/parallel/gateWay2.bpmn").name("部署测试并行网关");
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署ID:" + deploy.getId() + "\t部署名称" + deploy.getName());
	}

	// 启动流程实例
	@Test
	public void startProcessInst() {
		String processDefinitionKey = "gateWay2";
		ProcessInstance processInstance = ProcessEngines.getDefaultProcessEngine().getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID:" + processInstance.getId());
	}
	private void completeTaskByDefKeyAndAssignee(String processDefinitionKey,String assignee) {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> tasks = processEngine.getTaskService().createTaskQuery()
				.taskAssignee(assignee)
				.processDefinitionKey(processDefinitionKey)
				.list();
		if (tasks.size()>0) {
			for (Task task : tasks) {
				processEngine.getTaskService().complete(task.getId());
			}
		}
	}
	// 办理任务
	@Test
	public void completeSecond() {
		String processDefinitionKey = "gateWay2";
		String assignee = "买家";
		//String assignee = "卖家";
		completeTaskByDefKeyAndAssignee(processDefinitionKey, assignee );
	}
}
