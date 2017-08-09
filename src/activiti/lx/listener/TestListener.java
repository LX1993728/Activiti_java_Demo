package activiti.lx.listener;
/**
 * 测试执行监听器
 * @author liuxun
 *
 */

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestListener {
	@Test
	public void startProcess() {
		// 部署
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment()
				.addClasspathResource("activiti/lx/listener/logListener.bpmn").deploy();
		// 启动
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("execution");
		assertNotNull(processInstance.getId());
		// 推移流程
		runtimeService.signal(processInstance.getId());
		Task task = processEngine.getTaskService().createTaskQuery().taskAssignee("user")
				.processDefinitionKey("execution").singleResult();
		processEngine.getTaskService().complete(task.getId());
		// 代码执行完毕,流程即进入历史,查看流程变量logs
		List<String> logs = (List<String>) processEngine.getHistoryService().createHistoricVariableInstanceQuery()
				.processInstanceId(processInstance.getId()).variableName("logs").singleResult().getValue();
		System.out.println("流程结束,日志内容为:"+logs);
	}
}
