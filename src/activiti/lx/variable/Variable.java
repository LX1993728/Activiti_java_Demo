package activiti.lx.variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class Variable {
	// 初始化测试环境
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/variable/variable.bpmn")
				.addClasspathResource("activiti/lx/variable/variable.png");
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署ID:" + deploy.getId() + "\t部署名称" + deploy.getName());
	}

	/**
	 * 1. 在流程启动时添加流程变量
	 */
	@Test
	public void startFlow() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();

		String processDefinitionKey = "QJLC";
		Map<String, Object> variables = new HashMap<>();
		variables.put("请假天数", "3");
		variables.put("请假原因", "家里有事");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
		System.out.println("pid: " + processInstance.getId());
	}

	/**
	 * 1.2 在办理任务和任务提交时添加流程变量
	 */
	@Test
	public void completeTask_setVar() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		String taskId = "606";
		Map<String, Object> variables1 = new HashMap<>();
		variables1.put("请假日期", "明天");
		/**
		 * 在任务办理的过程中提交流程变量
		 */
		processEngine.getTaskService().setVariables(taskId, variables1);
		// 一次只能设置一个变量
		processEngine.getTaskService().setVariable(taskId, "请假送礼", "两盒红塔山");

		/**
		 * 在任务提交时，设置流程变量
		 */
		Map<String, Object> variables2 = new HashMap<>();
		variables2.put("批注", "请领导批准");
		processEngine.getTaskService().complete(taskId, variables2);
	}

	/**
	 * 1.3 为指定的流程实例设置流程变量
	 */
	@Test
	public void execution_setVar() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		String executionId = "601";
		Map<String, Object> variables = new HashMap<>();
		variables.put("value", "流程变量");
		// 在流程实例的办理过程中设置流程变量
		runtimeService.setVariables(executionId, variables);
		// runtimeService.setVariable(executionId, variableName, value);

		// 在流程实例提交时设置流程变量
		// runtimeService.signal(executionId, variables);
	}

	/**
	 * 2.1 获取流程变量(使用RuntimeService)
	 */
	@Test
	public void runtimeServ_getVar() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		System.out.println("=======指定单个属性获取变量=========");
		String executionId = "601";
		String variableName = "请假原因";
		String value = (String) runtimeService.getVariableLocal(executionId, variableName);
		System.out.println(variableName + "=" + value);

		System.out.println("=======指定多个属性获取变量=========");
		Collection<String> variableNames = new ArrayList<>();
		variableNames.add("请假天数");
		variableNames.add("请假日期");
		Map<String, Object> variables = runtimeService.getVariables(executionId, variableNames);
		for (String key : variables.keySet()) {
			System.out.println(key + "=" + variables.get(key));
		}

		System.out.println("=======指定全部属性获取变量=========");
		Map<String, Object> variables2 = runtimeService.getVariables(executionId);
		for (String key : variables2.keySet()) {
			System.out.println(key + "=" + variables2.get(key));
		}
	}

	/**
	 * 2.2 通过TaskService 获取
	 */
	@Test
	public void taskSer_getVar() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		String taskId = "705";
		Map<String, Object> variables = processEngine.getTaskService().getVariables(taskId);
		for (String key : variables.keySet()) {
			System.out.println(key + "=" + variables.get(key));
		}
	}

	/**
	 * 3.1 设置JavaBean流程变量
	 */
	@Test
	public void setJavaBeanVar() {
		String executionId = "601";
		Map<String, Object> variables = new HashMap<>();
		variables.put("user", new User(1L, "小明"));
		ProcessEngines.getDefaultProcessEngine().getRuntimeService().setVariables(executionId, variables);
	}

	/**
	 * 3.2 获取JavaBean流程变量
	 */
	@Test
	public void getJavaBeanVar() {
		String executionId = "601";
		Map<String, Object> variables = ProcessEngines.getDefaultProcessEngine().getRuntimeService()
				.getVariables(executionId);
        for (Entry<String, Object> entry : variables.entrySet()) {
			if (entry.getKey().equals("user")) {
				User user = (User) entry.getValue();
				System.out.println(user.getId());
				System.out.println(user.getName());
			}
		}
	}

}
