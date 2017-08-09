package activiti.lx.processinstance;
/**
 * 管理流程实例
 * @author liuxun
 *
 */

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class TestProcessInst {
	// 1.启动流程实例
	@Test
	public void startProcess() {
		// 创建流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 获取流程引擎服务对象
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// 启动流程，返回流程实例对象
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
		// 显示相关信息
		System.out.print("pid= "+processInstance.getId()); //流程实例ID
        System.out.println("activityId= "+processInstance.getActivityId());		
	}
	/**
	 * 2.1 查看指定用户的代办任务
	 */
	@Test
	public void findUnfinishedTask() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 获取个人的代办信息
		TaskService taskService = processEngine.getTaskService();
		// 创建流程任务查询对象
		TaskQuery taskQuery = taskService.createTaskQuery();
		String assignee = "员工";
		// 添加过滤条件
		taskQuery.taskAssignee(assignee);
		// 添加分页条件
		taskQuery.listPage(0, 10);
		// 添加过滤条件
		taskQuery.orderByTaskCreateTime().desc();
		// 执行查询
		List<Task> list = taskQuery.list();
		System.out.println("===============【"+assignee+"】的个人任务列表===============");
		// 迭代结果，查看个人任务
		for (Task task : list) {
			System.out.print("id="+task.getId()+","); //获取任务的ID
			System.out.print("name="+task.getName()+",");//获取任务的名称
			System.out.print("assign="+task.getAssignee()+",");//查询任务的代办人
			System.out.print("createTime="+task.getCreateTime()+",");//查询任务的创建时间
			System.out.println("executionId="+task.getExecutionId());//获取流程执行对象的ID
		}
	}
	/**
	 * 2.2 查看指定用户的可接任务(公共任务)
	 */
	@Test
	public void findCanTakeTask() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 获取个人的代办信息
		TaskService taskService = processEngine.getTaskService();
		// 创建流程任务查询对象
		TaskQuery taskQuery = taskService.createTaskQuery();
		String candidateUser = "员工";
		// 添加过滤条件
		taskQuery.taskCandidateUser(candidateUser );
		// 添加分页条件
		taskQuery.listPage(0, 10);
		// 添加过滤条件
		taskQuery.orderByTaskCreateTime().desc();
		// 执行查询
		List<Task> list = taskQuery.list();
		System.out.println("===============【"+candidateUser+"】的可接收任务列表===============");
		// 迭代结果，查看个人任务
		for (Task task : list) {
			System.out.print("id="+task.getId()+","); //获取任务的ID
			System.out.print("name="+task.getName()+",");//获取任务的名称
			System.out.print("assign="+task.getAssignee()+",");//查询任务的代办人
			System.out.print("createTime="+task.getCreateTime()+",");//查询任务的创建时间
			System.out.println("executionId="+task.getExecutionId());//获取流程执行对象的ID
		}
	}
	/**
	 * 3.认领任务(将公共任务变为个人任务)
	 */
	@Test
	public void takeTask() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 接手的任务ID
		String taskId = "304";
		// 让"员工1"认领ID为"304"的任务
		String userId = "员工1";
		processEngine.getTaskService().claim(taskId, userId);
	}
	/**
	 * 3.2退回任务(将个人任务变成公共任务)
	 * 
	 */
	@Test
	public void backTask() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String taskId = "304";
        String userId = "员工1";
		processEngine.getTaskService().setAssignee(taskId, userId);		
	}
	/**
	 * 4.办理任务(完成任务后，让流程往后移)
	 */
	@Test
	public void completeTask() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 指定任务ID
		String taskId = "302";
		// 完成任务
		processEngine.getTaskService().complete(taskId );
	}
	/**
	 * 5. 验证流程是否结束
	 */
	@Test
	public void checkProcessEnded() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		// 通过流程定义ID获取到流程定义实例对象
		String procId = "101";
		ProcessInstance processInstance = processInstanceQuery.processDefinitionId(procId).singleResult();
		// 查看流程定义是否已经完成
		if (processInstance!= null) {
			System.out.println("当前活动ID为："+processInstance.getActivityId());
		} else {
            System.out.println("ID为【"+procId+"】的流程实例已经结束");
		}
	}
}
