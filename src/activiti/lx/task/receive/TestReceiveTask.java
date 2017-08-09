package activiti.lx.task.receive;

import static org.junit.Assert.assertNotNull;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 测试接收任务
 * 
 * @author liuxun
 *
 */
public class TestReceiveTask {
	// 初始化测试环境
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/task/receive/receiveTask.bpmn")
				.addClasspathResource("activiti/lx/task/receive/receiveTask.png").name("部署测试接收任务");
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署ID:" + deploy.getId() + "\t部署名称" + deploy.getName());
	}

	// 启动流程实例
	@Test
	public void startProcessInst() {
		String processDefinitionKey = "receiveProcess";
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
        if (task!=null) {
			processEngine.getTaskService().complete(task.getId());
		}
	}
	// 处理Receivetask
	@Test
	public void signal() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Execution execution = processEngine.getRuntimeService().createExecutionQuery() // 查询活动分支
		            .processInstanceId("101")  //整个流程实例id
		            .activityId("receivetask1")   //Receive task的活动ID
		            .singleResult();
		assertNotNull(execution);
		processEngine.getRuntimeService().signal(execution.getId());
	}
}
