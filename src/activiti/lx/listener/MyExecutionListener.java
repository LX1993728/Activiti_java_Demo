package activiti.lx.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 自定义执行监听器
 * 
 * @author liuxun
 *
 */
public class MyExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// 获取日志流程变量
		List<String> logs = (List<String>) execution.getVariable("logs");
		if (logs == null) { //第一次则新建
            logs = new ArrayList<>();
		}
		// 把当前节点名称和监听事件名称添加至流程变量中
		logs.add(execution.getCurrentActivityName()+" "+execution.getEventName());
		// 将更新的日志变量放入流程
		execution.setVariable("logs", logs);
	}

}
