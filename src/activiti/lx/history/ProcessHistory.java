package activiti.lx.history;
/**
 * 查看流程历史数据
 * @author liuxun
 *
 */

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.junit.Test;

public class ProcessHistory {
	/**
	 * 1. 查看历史流程实例
	 * 查看系统一共按照某个规则执行了多少次流程
	 */
	@Test
	public void queryHistoryInstance() {
		// 创建流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		HistoryService historyService = processEngine.getHistoryService();
		// 创建流程实例查询对象
		HistoricProcessInstanceQuery instanceQuery = historyService.createHistoricProcessInstanceQuery();
		// 指定流程定义ID
		String processDefinitionId = "myProcess:1:4";
		// 添加过滤条件
		instanceQuery.processDefinitionId(processDefinitionId);
		// 排序条件
		instanceQuery.orderByProcessInstanceStartTime().desc();
		// 查询已经完成的实例
		instanceQuery.finished();
		// 分页查询
		instanceQuery.listPage(0, 10);
		// 执行查询
		List<HistoricProcessInstance> list = instanceQuery.list();
		for (HistoricProcessInstance hp : list) {
			System.out.print("ID: "+hp.getProcessDefinitionId()+", ");
			System.out.print("开始活动ID: "+hp.getStartActivityId()+", ");
			System.out.print("开始时间: "+hp.getStartTime()+", ");
			System.out.println("结束时间: "+hp.getEndTime()+", ");
		}
	}
	/**
	 * 2.查看历史流程活动
	 * 查看某次流程执行过程中所经历的步骤
	 */
	@Test
	public void queryHistoryActiviti() {
		// 获取流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		HistoryService historyService = processEngine.getHistoryService();
		// 查看历史流程实例活动对象
		HistoricActivityInstanceQuery activityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
		// 添加过滤条件
		String processInstanceId = "101"; // 指定流程实例ID
		activityInstanceQuery.processInstanceId(processInstanceId);
		// activityInstanceQuery.finished(); // 查询已经执行完实例中的活动
		// activityInstanceQuery.unfinished(); // 查询已经正在执行未完成实例中的活动
		// 执行查询
		List<HistoricActivityInstance> list = activityInstanceQuery.list();
		for (HistoricActivityInstance ha : list) {
			System.out.print("流程定义ID: "+ha.getProcessDefinitionId()+", ");
			System.out.print("开始活动ID: "+ha.getActivityId()+", ");
			System.out.print("开始活动名称: "+ha.getActivityName()+", ");
			System.out.print("开始时间: "+ha.getStartTime()+", ");
			System.out.println("结束时间: "+ha.getEndTime()+", ");
		}
		
	}
	/**
	 * 3.查看历史任务数据
	 * 任务与活动的区别：活动是一个动作，从A->B节点 是一个活动
	 * 任务是执行某个节点
	 */
	@Test
	public void queryHistorTask() {
		// 获取流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		HistoryService historyService = processEngine.getHistoryService();
		// 查看历史流程实例任务
		HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		// 添加过滤条件
		String processInstanceId = "101"; // 指定流程实例ID
		taskInstanceQuery.processInstanceId(processInstanceId);
		// 执行查询
		List<HistoricTaskInstance> list = taskInstanceQuery.list();
		for (HistoricTaskInstance taskInstance : list) {
			System.out.print("任务名称:"+taskInstance.getName()+", ");
			System.out.print("执行者:"+taskInstance.getAssignee()+", ");
			System.out.println("任务开始时间:"+taskInstance.getStartTime());
		}
		
	}
	  
}
