package activiti.lx.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTasksListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		// 获取日志流程变量
		List<String> logs = (List<String>) delegateTask.getVariable("logs");
		if (logs == null) { // 第一次创建
			logs = new ArrayList<>();
		}
		// 把当前节点名称和监听事件名称添加到流程变量中
		logs.add(delegateTask.getName()+" "+delegateTask.getEventName());
		// 将更新的日志变量放入流程
		delegateTask.setVariable("logs", logs);
	}

}
