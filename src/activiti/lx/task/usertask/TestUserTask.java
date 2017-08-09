package activiti.lx.task.usertask;

import static org.junit.Assert.assertNotNull;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestUserTask {
	// 初始化测试环境
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("activiti/lx/task/usertask/userTask.bpmn")
				.addClasspathResource("activiti/lx/task/usertask/userTask.png").name("测试用户任务");
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署ID:" + deploy.getId() + "\t部署名称" + deploy.getName());
	}

	// 启动流程实例
	@Test
	public void startProcessInst() {
		String processDefinitionKey = "userTaskProcess";
		ProcessInstance processInstance = ProcessEngines.getDefaultProcessEngine().getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID:" + processInstance.getId());
	}

	// 办理任务(查询任务节点"提交申请"张三的任务并进行办理)
	@Test
	public void completeSecond() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Task task = processEngine.getTaskService().createTaskQuery().taskAssignee("张三").singleResult();
		System.out.println(task.getId());
		if (task != null) {
			processEngine.getTaskService().complete(task.getId());
		}
	}

	// 候选人拾取公共任务 并进行办理("审核1" 任务节点)
	@Test
	public void claimTask() {
		String processDefinitionKey = "userTaskProcess";
		Task task = ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery()
				.processDefinitionKey(processDefinitionKey).taskCandidateUser("王五").singleResult();
		assertNotNull(task);
		ProcessEngines.getDefaultProcessEngine().getTaskService().claim(task.getId(), "王五"); // 拾取任务
		ProcessEngines.getDefaultProcessEngine().getTaskService().complete(task.getId()); // 办理任务
	}

	// 候选组获取任务并进行办理("审核2")
	@Test
	public void candidateUserResolveTask() {
		String processDefinitionKey = "userTaskProcess";
		Task task = ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.taskCandidateGroup("group1")
				.singleResult();
		assertNotNull(task);
	
		IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
		User user = identityService.newUser("groupUser1");
		user.setFirstName("赵");
		user.setLastName("六");
		Group group = identityService.newGroup("group1");
		group.setName("群组测试");
        identityService.saveUser(user);
        identityService.saveGroup(group);
		identityService.createMembership(user.getId(),group.getId());
		ProcessEngines.getDefaultProcessEngine().getTaskService().claim(task.getId(), user.getId());
		ProcessEngines.getDefaultProcessEngine().getTaskService().complete(task.getId());
	}
	// 办理任务(查询任务节点"审核3"马云的任务并进行办理)
	@Test
	public void completeEnd() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Task task = processEngine.getTaskService().createTaskQuery().taskAssignee("马云").singleResult();
		System.out.println(task.getId());
		if (task != null) {
			processEngine.getTaskService().complete(task.getId());
		}
	}

}
