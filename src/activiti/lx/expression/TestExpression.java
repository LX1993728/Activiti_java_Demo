package activiti.lx.expression;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import activiti.lx.variable.User;

/**
 * 测试表达式
 * 
 * @author liuxun
 *
 */
public class TestExpression {
	@Test
	public void expressionStart() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 部署规则
		processEngine.getRepositoryService().createDeployment()
				.addClasspathResource("activiti/lx/expression/expression.bpmn").name("测试uel表达式").deploy();
		// 启动实例
		String processDefinitionKey = "expression";
		String businessKey = "111"; // 业务逻辑单元标识，与具体业务一一对应的
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("assignee", "张三"); // 设置变量
		variables.put("user", new User(1L, "李四"));
		variables.put("id", "1000");
		// 必须在任务启动时设置流程变量，才能解析对应的uel表达式
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

		Task task1 = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(businessKey)
				.processInstanceId(processInstance.getId())
				.singleResult();
		assertNotNull(task1.getId());
		processEngine.getTaskService().complete(task1.getId()); // 办理第一个任务节点的任务

		Task task2 = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(businessKey)
				.processInstanceId(processInstance.getId())
				.singleResult();
		assertNotNull(task2.getId());
		processEngine.getTaskService().complete(task2.getId()); // 办理第二个任务节点的任务

		Task task3 = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(businessKey)
				.processInstanceId(processInstance.getId())
				.singleResult();
		assertNotNull(task3.getId());
		processEngine.getTaskService().complete(task3.getId()); // 办理第三个任务节点的任务

		// 任务查询完毕，查询历史数据
		List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.processInstanceId(processInstance.getId())
				.list();
		for (HistoricTaskInstance hti : list) {
			System.out.print("任务ID:"+hti.getId() + ",");
			System.out.print("任务名称:"+hti.getName() + ",");
			System.out.println("代理人:"+hti.getAssignee() + ",");
		}
	}

}
